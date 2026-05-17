import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public class Pesquisa3App {
    public static void main(String[] args) throws Exception {
        Path rootDir = args.length >= 1 ? Path.of(args[0]) : Path.of("data", "txt");
        String target = args.length >= 2 ? args[1] : "Mariana";
        int threads = args.length >= 3 ? Integer.parseInt(args[2]) : Runtime.getRuntime().availableProcessors();

        SearchReport report = run(rootDir, target, threads);
        ReportPrinter.print("PESQUISA 3 - POOL FIXO DE THREADS", rootDir, target, threads, report);
    }

    static SearchReport run(Path rootDir, String target, int threadCount) throws IOException, InterruptedException {
        List<Path> files = FileDiscovery.findTxtFiles(rootDir);
        int workers = Math.max(1, threadCount);
        ExecutorService pool = Executors.newFixedThreadPool(workers);
        List<String> errors = new ArrayList<>();
        List<SearchResult> allMatches = new ArrayList<>();
        long totalLines = 0;

        long start = System.nanoTime();
        try {
            List<Future<FileSearchResult>> futures = new ArrayList<>();
            for (Path file : files) {
                futures.add(pool.submit(() -> FileSearcher.searchWithContains(file, target)));
            }

            for (Future<FileSearchResult> future : futures) {
                try {
                    FileSearchResult fileResult = future.get();
                    allMatches.addAll(fileResult.matches());
                    totalLines += fileResult.linesRead();
                } catch (ExecutionException e) {
                    errors.add(e.getCause().getMessage());
                }
            }
        } finally {
            pool.shutdown();
        }
        long elapsed = System.nanoTime() - start;

        return new SearchReport(files.size(), files.size() - errors.size(), totalLines, allMatches, errors, elapsed);
    }
}

final class FileDiscovery {
    private FileDiscovery() {
    }

    static List<Path> findTxtFiles(Path rootDir) throws IOException {
        if (!Files.exists(rootDir)) {
            throw new IllegalArgumentException("Diretorio nao encontrado: " + rootDir);
        }

        try (Stream<Path> stream = Files.walk(rootDir)) {
            return stream
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().toLowerCase().endsWith(".txt"))
                .sorted(Comparator.comparing(Path::toString))
                .toList();
        }
    }
}

final class FileSearcher {
    private FileSearcher() {
    }

    static FileSearchResult searchWithContains(Path file, String target) throws IOException {
        List<SearchResult> matches = new ArrayList<>();
        long linesRead = 0;

        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                linesRead++;
                if (line.contains(target)) {
                    matches.add(new SearchResult(file.toString(), linesRead, line));
                }
            }
        }

        return new FileSearchResult(matches, linesRead);
    }
}

record FileSearchResult(List<SearchResult> matches, long linesRead) {
}

record SearchResult(String filePath, long lineNumber, String lineContent) {
    @Override
    public String toString() {
        return "Arquivo: " + filePath + " | Linha: " + lineNumber + " | Conteudo: " + lineContent;
    }
}

record SearchReport(
    int filesFound,
    int txtFilesProcessed,
    long totalLines,
    List<SearchResult> matches,
    List<String> errors,
    long elapsedNanos
) {
    double elapsedMillis() {
        return elapsedNanos / 1_000_000.0;
    }
}

final class ReportPrinter {
    private ReportPrinter() {
    }

    static void print(String title, Path rootDir, String target, int threads, SearchReport report) {
        System.out.println("=== " + title + " ===");
        System.out.println("Diretorio: " + rootDir);
        System.out.println("Texto buscado: " + target);
        System.out.println("Threads do pool: " + threads);
        System.out.println("Arquivos encontrados: " + report.filesFound());
        System.out.println("Arquivos .txt processados: " + report.txtFilesProcessed());
        System.out.println("Total de linhas analisadas: " + report.totalLines());
        System.out.println("Ocorrencias encontradas: " + report.matches().size());
        System.out.println("Erros de leitura: " + report.errors().size());
        System.out.printf("Tempo de execucao paralelo: %.3f ms%n", report.elapsedMillis());

        for (SearchResult result : report.matches()) {
            System.out.println(result);
        }
        for (String error : report.errors()) {
            System.out.println("Erro: " + error);
        }
    }
}
