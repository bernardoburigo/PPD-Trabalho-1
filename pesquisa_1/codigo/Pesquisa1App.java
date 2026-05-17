import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Pesquisa1App {
    public static void main(String[] args) throws Exception {
        Path rootDir = args.length >= 1 ? Path.of(args[0]) : Path.of("data", "txt");
        String target = args.length >= 2 ? args[1] : "Mariana";

        SearchReport report = run(rootDir, target);
        ReportPrinter.print("PESQUISA 1 - BUSCA SEQUENCIAL SIMPLES", rootDir, target, report);
    }

    static SearchReport run(Path rootDir, String target) throws IOException {
        List<Path> files = FileDiscovery.findTxtFiles(rootDir);
        List<SearchResult> results = new ArrayList<>();
        long totalLines = 0;

        long start = System.nanoTime();
        for (Path file : files) {
            FileSearchResult fileResult = FileSearcher.searchWithContains(file, target);
            results.addAll(fileResult.matches());
            totalLines += fileResult.linesRead();
        }
        long elapsed = System.nanoTime() - start;

        return new SearchReport(files.size(), files.size(), totalLines, results, elapsed);
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
    long elapsedNanos
) {
    double elapsedMillis() {
        return elapsedNanos / 1_000_000.0;
    }
}

final class ReportPrinter {
    private ReportPrinter() {
    }

    static void print(String title, Path rootDir, String target, SearchReport report) {
        System.out.println("=== " + title + " ===");
        System.out.println("Diretorio: " + rootDir);
        System.out.println("Texto buscado: " + target);
        System.out.println("Arquivos encontrados: " + report.filesFound());
        System.out.println("Arquivos .txt processados: " + report.txtFilesProcessed());
        System.out.println("Total de linhas analisadas: " + report.totalLines());
        System.out.println("Ocorrencias encontradas: " + report.matches().size());
        System.out.printf("Tempo de execucao: %.3f ms%n", report.elapsedMillis());

        for (SearchResult result : report.matches()) {
            System.out.println(result);
        }
    }
}
