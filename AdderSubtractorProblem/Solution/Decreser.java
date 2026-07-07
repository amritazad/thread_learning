package AdderSubtractorProblem.Solution;

import java.util.concurrent.locks.Lock;

public class Decreser implements Runnable
{
    Counter counter;
    int limit;
    Lock lock;
    public Decreser(Counter counter, int limit, Lock lock) {
        this.counter = counter;
        this.limit = limit;
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int i = 0; i < limit; i++) {
            lock.lock();
            counter.decrement();
            lock.unlock();
        }
    }
    
}

       
