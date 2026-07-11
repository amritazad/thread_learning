package AddingLargeNoOfArrayUsingMultyCoreCodeSemaphore;

import java.util.*;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        int[] array = new int[(int)1e9];
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) random.nextInt(100);
        }

        int numThreads = Runtime.getRuntime().availableProcessors();
        int chunkSize = array.length / numThreads;
        int remaining = array.length % numThreads;

        Semaphore semaphore = new Semaphore(numThreads);
        int startIndex = 0;
        int endIndex = chunkSize - 1;
        Long totalSum = 0L;
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Future<Integer>> futures = new ArrayList<>();
        while(endIndex < array.length) {
            try {
                semaphore.acquire();
                AdditionOfArray additionTask = new AdditionOfArray(array, startIndex, endIndex);
                Future<Integer> future = executorService.submit(additionTask);
                futures.add(future);
                startIndex = endIndex + 1;
                endIndex = Math.min(startIndex + chunkSize - 1, array.length - 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            startIndex = endIndex + 1;
            endIndex = Math.min(startIndex + chunkSize - 1, array.length - 1);
        }
        for (Future<Integer> future : futures) {
            try {
                totalSum += future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    
}
