import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public class GenerateTxtDataApp {
    private static final List<String> NAMES = List.of(
        "Ana", "Bruno", "Carlos", "Daniela", "Eduarda", "Felipe", "Gabriela", "Helena", "Igor", "Joao",
        "Karen", "Lucas", "Mariana", "Natalia", "Otavio", "Paulo", "Renata", "Sofia", "Tiago", "Vitor"
    );

    private static final List<String> ACTIONS = List.of(
        "estudou computacao", "fez um experimento", "participou da aula", "escreveu o relatorio",
        "executou o benchmark", "otimizou o algoritmo", "analisou os resultados", "comparou as metricas"
    );

    public static void main(String[] args) throws Exception {
        Path outputDir = args.length >= 1 ? Path.of(args[0]) : Path.of("data", "txt");
        int fileCount = args.length >= 2 ? Integer.parseInt(args[1]) : 40;
        int linesPerFile = args.length >= 3 ? Integer.parseInt(args[2]) : 5000;
        String target = args.length >= 4 ? args[3] : "Mariana";
        double hitRate = args.length >= 5 ? Double.parseDouble(args[4]) : 0.03;

        generate(outputDir, fileCount, linesPerFile, target, hitRate);

        System.out.println("Arquivos gerados em: " + outputDir.toAbsolutePath());
        System.out.println("Quantidade de arquivos: " + fileCount);
        System.out.println("Linhas por arquivo: " + linesPerFile);
        System.out.println("Nome alvo inserido: " + target);
        System.out.println("Taxa aproximada de ocorrencia por linha: " + hitRate);
    }

    private static void generate(Path outputDir, int fileCount, int linesPerFile, String target, double hitRate) throws IOException {
        Files.createDirectories(outputDir);
        Random random = new Random(42);

        for (int i = 1; i <= fileCount; i++) {
            Path subDir = outputDir.resolve("lote_" + ((i - 1) / 10 + 1));
            Files.createDirectories(subDir);

            Path file = subDir.resolve(String.format("dados_%03d.txt", i));
            try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                for (int line = 1; line <= linesPerFile; line++) {
                    writer.write(buildLine(random, target, hitRate, line));
                    writer.newLine();
                }
            }
        }
    }

    private static String buildLine(Random random, String target, double hitRate, int lineNumber) {
        String name = random.nextDouble() < hitRate ? target : NAMES.get(random.nextInt(NAMES.size()));
        String action = ACTIONS.get(random.nextInt(ACTIONS.size()));
        int value = 1000 + random.nextInt(9000);
        return "linha=" + lineNumber + " | nome=" + name + " | acao=" + action + " | valor=" + value;
    }
}
