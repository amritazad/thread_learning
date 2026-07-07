public class MergeSort {
    
    public static int[] mergeSort(int[] array) {
        // Base case: if array has 1 element, it's already sorted
        if (array.length <= 1) {
            return array;
        }   
        
        // Split the array into two halves
        int mid = array.length / 2;
        int[] left = new int[mid];
        int[] right = new int[array.length - mid];
        System.arraycopy(array, 0, left, 0, mid);
        System.arraycopy(array, mid, right, 0, array.length - mid);
        
        // Recursively sort both halves
        int[] sortedLeft = mergeSort(left);
        int[] sortedRight = mergeSort(right);
        
        // Merge the sorted halves
        return merge(sortedLeft, sortedRight);
    }
    
    private static int[] merge(int[] leftArray, int[] rightArray) {
        int leftSize = leftArray.length;
        int rightSize = rightArray.length;
        int[] mergedArray = new int[leftSize + rightSize];
        
        int i = 0, j = 0;
        
        while (i < leftSize && j < rightSize) {
            if (leftArray[i] <= rightArray[j]) {
                mergedArray[i + j] = leftArray[i];
                i++;
            } else {
                mergedArray[i + j] = rightArray[j];
                j++;
            }
        }
        
        while (i < leftSize) {
            mergedArray[i + j] = leftArray[i];
            i++;
        }
        
        while (j < rightSize) {
            mergedArray[i + j] = rightArray[j];
            j++;
        }
        
        return mergedArray;
    }

}
