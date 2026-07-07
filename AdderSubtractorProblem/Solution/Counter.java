package AdderSubtractorProblem.Solution;

/**
 * Counter
 */
public class Counter {
    int count = 0;
    int offset;

    public Counter(int offset) {
        this.offset = offset;
    }

    public void increment() {
        count += offset;
    }

    public void decrement() {
        count -= offset;
    }

    public int getCount() {
        return count;
    }
}
