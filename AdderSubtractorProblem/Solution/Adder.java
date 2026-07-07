package AdderSubtractorProblem.Solution;

import java.util.concurrent.locks.Lock;

public class Adder implements Runnable
{
    Counter counter;
    int limit;
    Lock lock;
    public Adder(Counter counter, int limit, Lock lock) {
        this.counter = counter;
        this.limit = limit;
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int i = 0; i < limit; i++) {
            lock.lock();
            counter.increment();
            lock.unlock();
        }
    }
}
