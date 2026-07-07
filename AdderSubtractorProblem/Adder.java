package AdderSubtractorProblem;

public class Adder implements Runnable
{
    Counter counter;
    int limit;

    public Adder(Counter counter, int limit) {
        this.counter = counter;
        this.limit = limit;
    }

    @Override
    public void run() {
        for (int i = 0; i < limit; i++) {
            counter.increment();
        }
    }
}
