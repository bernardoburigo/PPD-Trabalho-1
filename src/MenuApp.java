import java.nio.file.Path;
import java.util.Scanner;

public class MenuApp {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            printMenu();
            int option = readInt("Escolha uma opcao: ", -1);
            System.out.println();

            try {
                switch (option) {
                    case 1 -> generateData();
                    case 2 -> runSequential();
                    case 3 -> runParallel();
                    case 4 -> runBenchmark();
                    case 5 -> runAll();
                    case 0 -> {
                        System.out.println("Encerrando...");
                        return;
                    }
                    default -> System.out.println("Opcao invalida. Tente novamente.");
                }
            } catch (Exception e) {
                System.out.println("Erro na execucao: " + e.getMessage());
            }

            System.out.println();
            System.out.println("Pressione ENTER para voltar ao menu...");
            SCANNER.nextLine();
        }
    }

    private static void printMenu() {
        System.out.println("============================================");
        System.out.println(" Trabalho 1 - Busca em TXT (Java)");
        System.out.println("============================================");
        System.out.println("1) Gerar arquivos TXT de teste");
        System.out.println("2) Executar busca sequencial");
        System.out.println("3) Executar busca paralela (Threads)");
        System.out.println("4) Executar benchmark de speedup");
        System.out.println("5) Executar fluxo completo (1 -> 4)");
        System.out.println("0) Sair");
    }

    private static void generateData() throws Exception {
        Path outputDir = Path.of(readText("Diretorio de saida [data/txt]: ", "data/txt"));
        int fileCount = readInt("Quantidade de arquivos [40]: ", 40);
        int linesPerFile = readInt("Linhas por arquivo [5000]: ", 5000);
        String target = readText("Nome alvo [Mariana]: ", "Mariana");
        double hitRate = readDouble("Taxa de ocorrencia por linha [0.03]: ", 0.03);

        String[] appArgs = {
            outputDir.toString(),
            String.valueOf(fileCount),
            String.valueOf(linesPerFile),
            target,
            String.valueOf(hitRate)
        };

        GenerateTxtDataApp.main(appArgs);
    }

    private static void runSequential() throws Exception {
        Path rootDir = Path.of(readText("Diretorio para busca [data/txt]: ", "data/txt"));
        String target = readText("Nome a buscar [Mariana]: ", "Mariana");
        String algorithm = readText("Algoritmo (NAIVE/KMP) [KMP]: ", "KMP");

        String[] appArgs = {rootDir.toString(), target, algorithm};
        SequentialSearchApp.main(appArgs);
    }

    private static void runParallel() throws Exception {
        Path rootDir = Path.of(readText("Diretorio para busca [data/txt]: ", "data/txt"));
        String target = readText("Nome a buscar [Mariana]: ", "Mariana");
        int threads = readInt("Numero de threads [8]: ", 8);
        String algorithm = readText("Algoritmo (NAIVE/KMP) [KMP]: ", "KMP");
        String strategy = readText("Estrategia (PER_FILE/BATCHED) [BATCHED]: ", "BATCHED");

        String[] appArgs = {
            rootDir.toString(),
            target,
            String.valueOf(threads),
            algorithm,
            strategy
        };
        ParallelSearchApp.main(appArgs);
    }

    private static void runBenchmark() throws Exception {
        Path rootDir = Path.of(readText("Diretorio para benchmark [data/txt]: ", "data/txt"));
        String target = readText("Nome a buscar [Mariana]: ", "Mariana");
        int threads = readInt("Numero de threads [8]: ", 8);

        String[] appArgs = {rootDir.toString(), target, String.valueOf(threads)};
        BenchmarkApp.main(appArgs);
    }

    private static void runAll() throws Exception {
        Path outputDir = Path.of(readText("Diretorio base [data/txt]: ", "data/txt"));
        int fileCount = readInt("Quantidade de arquivos [40]: ", 40);
        int linesPerFile = readInt("Linhas por arquivo [5000]: ", 5000);
        String target = readText("Nome alvo [Mariana]: ", "Mariana");
        double hitRate = readDouble("Taxa de ocorrencia por linha [0.03]: ", 0.03);
        int threads = readInt("Numero de threads [8]: ", 8);

        GenerateTxtDataApp.main(new String[] {
            outputDir.toString(),
            String.valueOf(fileCount),
            String.valueOf(linesPerFile),
            target,
            String.valueOf(hitRate)
        });

        System.out.println();
        SequentialSearchApp.main(new String[] {outputDir.toString(), target, "KMP"});

        System.out.println();
        ParallelSearchApp.main(new String[] {outputDir.toString(), target, String.valueOf(threads), "KMP", "PER_FILE"});

        System.out.println();
        ParallelSearchApp.main(new String[] {outputDir.toString(), target, String.valueOf(threads), "KMP", "BATCHED"});

        System.out.println();
        BenchmarkApp.main(new String[] {outputDir.toString(), target, String.valueOf(threads)});
    }

    private static String readText(String prompt, String defaultValue) {
        System.out.print(prompt);
        String value = SCANNER.nextLine().trim();
        return value.isEmpty() ? defaultValue : value;
    }

    private static int readInt(String prompt, int defaultValue) {
        String value = readText(prompt, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println("Valor invalido, usando padrao: " + defaultValue);
            return defaultValue;
        }
    }

    private static double readDouble(String prompt, double defaultValue) {
        String value = readText(prompt, String.valueOf(defaultValue));
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.out.println("Valor invalido, usando padrao: " + defaultValue);
            return defaultValue;
        }
    }
}
