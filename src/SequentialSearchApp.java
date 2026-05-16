import java.nio.file.Path;

public class SequentialSearchApp {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Uso: java SequentialSearchApp <diretorio> <nome> [algoritmo]");
            System.out.println("Algoritmos: NAIVE (padrao), KMP");
            return;
        }

        Path rootDir = Path.of(args[0]);
        String target = args[1];
        SearchAlgorithm algorithm = args.length >= 3 ? SearchAlgorithm.fromString(args[2]) : SearchAlgorithm.NAIVE;

        SearchReport report = SequentialSearch.run(rootDir, target, algorithm);

        System.out.println("=== BUSCA SEQUENCIAL ===");
        System.out.println("Diretorio: " + rootDir);
        System.out.println("Nome buscado: " + target);
        System.out.println("Algoritmo: " + algorithm);
        System.out.println("Arquivos analisados: " + report.getFileCount());
        System.out.println("Ocorrencias encontradas: " + report.getMatches().size());
        System.out.printf("Tempo: %.3f ms%n", report.elapsedMillis());

        for (SearchResult result : report.getMatches()) {
            System.out.println(result);
        }
    }
}
