import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MaxFinder implements Callable<Integer> {
    String filePath;

    public MaxFinder(String path) {
        this.filePath = path;
    }

    public MaxFinder() {
    }

    int findMax() throws IOException {
        int max = -1;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String[] words;
            int number;
            while ((line = br.readLine()) != null) {
                words = line.trim().split("\\s+");
                for (String word : words) {
                    number = Integer.parseInt(word);
                    if (number > max) {
                        max = number;
                    }
                }
            }
        }
        System.out.println("Поток: " + Thread.currentThread() + " Максимум в потоке: " + max);
        return max;
    }

    int findMax(String[] filePath) throws ExecutionException, InterruptedException {
        int maximum = -1;
        int numOfThreads = filePath.length;
        ExecutorService es = Executors.newFixedThreadPool(numOfThreads);
        List<Future<Integer>> futures = new ArrayList<>();
        for (String s : filePath) {
            futures.add(es.submit(new MaxFinder(s)));
        }
        List<Integer> results = new ArrayList<>();
        for (Future<Integer> f : futures) {
            results.add(f.get());
        }
        for (Integer result : results) {
            if (maximum < result) {
                maximum = result;
            }
        }
        return maximum;
    }

    @Override
    public Integer call() throws IOException {
        return findMax();
    }
}
