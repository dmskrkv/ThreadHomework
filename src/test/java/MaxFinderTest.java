import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MaxFinderTest {
    @Test
    void testOne() throws IOException {
        Path testPath = Files.createTempFile("test1", ".txt");
        String testContent = "1 3 5 7 9\n 10 2 4 6 8 0";
        Files.write(testPath, testContent.getBytes(StandardCharsets.UTF_8));
        String path = testPath.toAbsolutePath().toString();
        File testFile = new File(path);
        Assertions.assertTrue(testFile.exists());

        int max;
        MaxFinder mf = new MaxFinder(path);
        max = mf.findMax();
        Assertions.assertEquals(max, 10);
    }

    @Test
    void testTwo() throws IOException {
        Path testPath = Files.createTempFile("test2", ".txt");
        String testContent = "10 30 50 70 90 10 20 40\n 60 80 0";
        Files.write(testPath, testContent.getBytes(StandardCharsets.UTF_8));
        String path = testPath.toAbsolutePath().toString();

        ExecutorService es = Executors.newCachedThreadPool();
        Future<Integer> f;
        f = es.submit(new MaxFinder(path));

        try {
            System.out.println(f.get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        es.shutdown();
    }

    @Test
    void testThree() throws IOException, ExecutionException, InterruptedException {
        Path test3Path = Files.createTempFile("test3", ".txt");
        String testContent = "1 3 5 7 9\n 10 2 4 6 8 0";
        Files.write(test3Path, testContent.getBytes(StandardCharsets.UTF_8));
        String path3 = test3Path.toAbsolutePath().toString();
        Path test3Path2 = Files.createTempFile("test3_2", ".txt");
        String testContent2 = "10 30 50 70 90 10 20 40\n 60 80 0";
        Files.write(test3Path2, testContent2.getBytes(StandardCharsets.UTF_8));
        String path3_2 = test3Path2.toAbsolutePath().toString();
        String[] array = {path3, path3_2};

        ExecutorService es = Executors.newFixedThreadPool(2);
        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            futures.add(es.submit(new MaxFinder(array[i])));
        }
        List<Integer> results = new ArrayList<>();
        for (Future<Integer> f : futures) {
            results.add(f.get());
        }
        int max = -1;
        for (Integer result : results) {
            if (max < result) {
                max = result;
            }
        }
        System.out.println(max);
    }

    @Test
    void testFour() throws IOException, ExecutionException, InterruptedException {
        Path test4Path = Files.createTempFile("test4", ".txt");
        String testContent = "1 3 5 7 9\n 10 2 4 6 8 0";
        Files.write(test4Path, testContent.getBytes(StandardCharsets.UTF_8));
        String path4 = test4Path.toAbsolutePath().toString();
        Path test4Path2 = Files.createTempFile("test4_2", ".txt");
        String testContent2 = "1321 300 50 74\n 91 100500 2 40\n 607 808 0 101";
        Files.write(test4Path2, testContent2.getBytes(StandardCharsets.UTF_8));
        String path4_2 = test4Path2.toAbsolutePath().toString();
        Path test4Path3 = Files.createTempFile("test4_3", ".txt");
        String testContent3 = "10 30 50 70 90 10 20 40\n 60 80 0";
        Files.write(test4Path3, testContent3.getBytes(StandardCharsets.UTF_8));
        String path4_3 = test4Path3.toAbsolutePath().toString();
        String[] array = {path4, path4_2, path4_3};
        MaxFinder mf = new MaxFinder();
        int res = mf.findMax(array);
        Assertions.assertEquals(res,100500);
    }
}
