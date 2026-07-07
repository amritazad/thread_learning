import java.util.concurrent.*;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            Future<int[]> future = executor.submit(new ThreadMergeSort(arr, executor));
            int[] sortedArray = future.get();
            System.out.println("Sorted Array: " + Arrays.toString(sortedArray));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }finally {
            executor.shutdown();
        }
    }
}
