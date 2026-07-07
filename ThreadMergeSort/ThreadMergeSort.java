import java.util.concurrent.*;
public class ThreadMergeSort implements Callable<int[]>
{
    private int[] array;
    ExecutorService executor;
    public ThreadMergeSort(int[] arr, ExecutorService executor)
    {
        this.array = arr;
        this.executor = executor;
    }
    @Override
    public int[] call() throws Exception {
        // Base case: if array has 1 element, it's already sorted
        if (array.length <= 1) {
            return array;
        }   
        
        // Implementation for thread-based merge sort
        int mid = array.length / 2;
        int[] left = new int[mid];
        int[] right = new int[array.length - mid];
        System.arraycopy(array, 0, left, 0, mid);
        System.arraycopy(array, mid, right, 0, array.length - mid);
        Future<int[]> leftFuture = executor.submit(new ThreadMergeSort(left, executor));
        Future<int[]> rightFuture = executor.submit(new ThreadMergeSort(right, executor));
        try{
            int[] sortedLeft = leftFuture.get();
            int[] sortedRight = rightFuture.get();
            Future<int[]> mergedFuture = executor.submit(new Merge(sortedLeft, sortedRight));
            System.out.println("Merge " + Thread.currentThread().getName());
            return mergedFuture.get();
        } catch(InterruptedException |  ExecutionException e){
            e.printStackTrace();
        }
        return null;
    }
}

class Merge implements Callable<int[]>{
    int[] leftArray, rightArray, MergedArray;
    int leftSize, rightSize, i, j;
    public Merge(int[] left, int[] right){
        this.leftArray = left;
        this.rightArray = right;
        this.leftSize = left.length;
        this.rightSize = right.length;
        this.MergedArray = new int[leftSize + rightSize];
        this.i = 0;
        this.j = 0;
    }
    @Override
    public int[] call() throws Exception{
        while(i < leftSize && j < rightSize){
            if(leftArray[i] <= rightArray[j]){
                MergedArray[i+j] = leftArray[i];
                i++;
            } else {
                MergedArray[i+j] = rightArray[j];
                j++;
            }
        }
        while(i < leftSize){
            MergedArray[i+j] = leftArray[i];
            i++;
        }
        while(j < rightSize){
            MergedArray[i+j] = rightArray[j];
            j++;
        }
        System.out.println("Merged Array: " + Thread.currentThread().getName());
        return MergedArray;
    }
}

