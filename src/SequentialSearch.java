import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class SequentialSearch {
    private SequentialSearch() {
    }

    public static SearchReport run(Path rootDir, String target, SearchAlgorithm algorithm) throws IOException {
        List<Path> files = FileDiscovery.findTxtFiles(rootDir);
        List<SearchResult> allMatches = new ArrayList<>();

        long start = System.nanoTime();
        for (Path file : files) {
            allMatches.addAll(FileSearcher.searchInFile(file, target, algorithm));
        }
        long end = System.nanoTime();

        return new SearchReport(allMatches, end - start, files.size());
    }
}
