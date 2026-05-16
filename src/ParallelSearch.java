import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class ParallelSearch {
    private ParallelSearch() {
    }

    public static SearchReport run(Path rootDir, String target, SearchAlgorithm algorithm, int threadCount, ParallelStrategy strategy)
        throws IOException, InterruptedException {

        List<Path> files = FileDiscovery.findTxtFiles(rootDir);
        if (files.isEmpty()) {
            return new SearchReport(List.of(), 0L, 0);
        }

        int workers = Math.max(1, threadCount);
        long start = System.nanoTime();

        List<SearchResult> matches;
        if (strategy == ParallelStrategy.PER_FILE) {
            matches = perFileStrategy(files, target, algorithm, workers);
        } else {
            matches = batchedStrategy(files, target, algorithm, workers);
        }

        long end = System.nanoTime();
        return new SearchReport(matches, end - start, files.size());
    }

    private static List<SearchResult> perFileStrategy(List<Path> files, String target, SearchAlgorithm algorithm, int workers)
        throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(workers);
        try {
            List<Future<List<SearchResult>>> futures = new ArrayList<>();
            for (Path file : files) {
                futures.add(pool.submit(() -> FileSearcher.searchInFile(file, target, algorithm)));
            }

            List<SearchResult> allMatches = new ArrayList<>();
            for (Future<List<SearchResult>> future : futures) {
                allMatches.addAll(getFutureResult(future));
            }
            return allMatches;
        } finally {
            pool.shutdown();
        }
    }

    private static List<SearchResult> batchedStrategy(List<Path> files, String target, SearchAlgorithm algorithm, int workers)
        throws InterruptedException {
        int batchSize = Math.max(1, (int) Math.ceil((double) files.size() / workers));
        List<List<Path>> batches = new ArrayList<>();

        for (int i = 0; i < files.size(); i += batchSize) {
            int end = Math.min(i + batchSize, files.size());
            batches.add(files.subList(i, end));
        }

        ExecutorService pool = Executors.newFixedThreadPool(Math.min(workers, batches.size()));
        try {
            List<Callable<List<SearchResult>>> tasks = new ArrayList<>();
            for (List<Path> batch : batches) {
                tasks.add(() -> {
                    List<SearchResult> local = new ArrayList<>();
                    for (Path file : batch) {
                        local.addAll(FileSearcher.searchInFile(file, target, algorithm));
                    }
                    return local;
                });
            }

            List<Future<List<SearchResult>>> futures = pool.invokeAll(tasks);
            List<SearchResult> allMatches = new ArrayList<>();
            for (Future<List<SearchResult>> future : futures) {
                allMatches.addAll(getFutureResult(future));
            }
            return allMatches;
        } finally {
            pool.shutdown();
        }
    }

    private static List<SearchResult> getFutureResult(Future<List<SearchResult>> future) throws InterruptedException {
        try {
            return future.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException ioException) {
                throw new RuntimeException("Erro de I/O durante busca paralela: " + ioException.getMessage(), ioException);
            }
            throw new RuntimeException("Erro durante busca paralela", cause);
        }
    }
}
