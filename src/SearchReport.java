import java.util.List;

public class SearchReport {
    private final List<SearchResult> matches;
    private final long elapsedNanos;
    private final int fileCount;

    public SearchReport(List<SearchResult> matches, long elapsedNanos, int fileCount) {
        this.matches = matches;
        this.elapsedNanos = elapsedNanos;
        this.fileCount = fileCount;
    }

    public List<SearchResult> getMatches() {
        return matches;
    }

    public long getElapsedNanos() {
        return elapsedNanos;
    }

    public int getFileCount() {
        return fileCount;
    }

    public double elapsedMillis() {
        return elapsedNanos / 1_000_000.0;
    }
}
