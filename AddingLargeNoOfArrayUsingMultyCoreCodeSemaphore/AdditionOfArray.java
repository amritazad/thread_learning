package AddingLargeNoOfArrayUsingMultyCoreCodeSemaphore;

import java.util.concurrent.Callable;

public class AdditionOfArray implements Callable<Integer> {
    private int[] array;
    private int startIndex;
    private int endIndex;

    public AdditionOfArray(int[] array, int startIndex, int endIndex) {
        this.array = array;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = startIndex; i < endIndex; i++) {
            sum += array[i];
        }
        return sum;
    }
    
}
