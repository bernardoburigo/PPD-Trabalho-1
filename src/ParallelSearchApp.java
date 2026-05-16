import java.nio.file.Path;

public class ParallelSearchApp {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Uso: java ParallelSearchApp <diretorio> <nome> [threads] [algoritmo] [estrategia]");
            System.out.println("Algoritmos: NAIVE (padrao), KMP");
            System.out.println("Estrategias: PER_FILE (padrao), BATCHED");
            return;
        }

        Path rootDir = Path.of(args[0]);
        String target = args[1];
        int threads = args.length >= 3 ? Integer.parseInt(args[2]) : Runtime.getRuntime().availableProcessors();
        SearchAlgorithm algorithm = args.length >= 4 ? SearchAlgorithm.fromString(args[3]) : SearchAlgorithm.NAIVE;
        ParallelStrategy strategy = args.length >= 5 ? ParallelStrategy.fromString(args[4]) : ParallelStrategy.PER_FILE;

        SearchReport report = ParallelSearch.run(rootDir, target, algorithm, threads, strategy);

        System.out.println("=== BUSCA PARALELA ===");
        System.out.println("Diretorio: " + rootDir);
        System.out.println("Nome buscado: " + target);
        System.out.println("Threads: " + threads);
        System.out.println("Algoritmo: " + algorithm);
        System.out.println("Estrategia: " + strategy);
        System.out.println("Arquivos analisados: " + report.getFileCount());
        System.out.println("Ocorrencias encontradas: " + report.getMatches().size());
        System.out.printf("Tempo: %.3f ms%n", report.elapsedMillis());

        for (SearchResult result : report.getMatches()) {
            System.out.println(result);
        }
    }
}
