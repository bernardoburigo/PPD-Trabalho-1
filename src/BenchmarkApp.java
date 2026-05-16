import java.nio.file.Path;

public class BenchmarkApp {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Uso: java BenchmarkApp <diretorio> <nome> [threads]");
            return;
        }

        Path rootDir = Path.of(args[0]);
        String target = args[1];
        int threads = args.length >= 3 ? Integer.parseInt(args[2]) : Runtime.getRuntime().availableProcessors();

        SearchAlgorithm algorithm = SearchAlgorithm.KMP;

        SearchReport sequential = SequentialSearch.run(rootDir, target, algorithm);
        SearchReport parallelPerFile = ParallelSearch.run(rootDir, target, algorithm, threads, ParallelStrategy.PER_FILE);
        SearchReport parallelBatched = ParallelSearch.run(rootDir, target, algorithm, threads, ParallelStrategy.BATCHED);

        System.out.println("=== BENCHMARK SPEEDUP ===");
        System.out.println("Algoritmo: " + algorithm);
        System.out.println("Threads: " + threads);
        System.out.println("Arquivos analisados: " + sequential.getFileCount());
        System.out.println("Ocorrencias encontradas: " + sequential.getMatches().size());
        System.out.printf("Tempo sequencial: %.3f ms%n", sequential.elapsedMillis());
        System.out.printf("Tempo paralelo (PER_FILE): %.3f ms%n", parallelPerFile.elapsedMillis());
        System.out.printf("Tempo paralelo (BATCHED): %.3f ms%n", parallelBatched.elapsedMillis());

        double speedupPerFile = sequential.getElapsedNanos() / (double) Math.max(1, parallelPerFile.getElapsedNanos());
        double speedupBatched = sequential.getElapsedNanos() / (double) Math.max(1, parallelBatched.getElapsedNanos());

        System.out.printf("Speedup PER_FILE: %.3f%n", speedupPerFile);
        System.out.printf("Speedup BATCHED: %.3f%n", speedupBatched);
    }
}
