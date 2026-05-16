import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class FileSearcher {
    private FileSearcher() {
    }

    public static List<SearchResult> searchInFile(Path file, String target, SearchAlgorithm algorithm) throws IOException {
        List<SearchResult> results = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (TextMatcher.contains(line, target, algorithm)) {
                    results.add(new SearchResult(file.toString(), lineNumber, line));
                }
            }
        }

        return results;
    }
}
