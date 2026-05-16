import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public final class FileDiscovery {
    private FileDiscovery() {
    }

    public static List<Path> findTxtFiles(Path rootDir) throws IOException {
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
