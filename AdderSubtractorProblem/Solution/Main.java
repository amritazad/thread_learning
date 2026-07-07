package AdderSubtractorProblem.Solution;

import java.util.concurrent.locks.ReentrantLock;

public class Main {
    
    public static void main(String[] args) {
        Counter counter = new Counter(1);
        int limit = 1000000;
        ReentrantLock lock = new ReentrantLock();
        Thread adderThread = new Thread(new Adder(counter, limit, lock));
        Thread decreserThread = new Thread(new Decreser(counter, limit, lock));

        adderThread.start();
        decreserThread.start();

        try {
            adderThread.join();
            decreserThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final count: " + counter.getCount());
    }
}
