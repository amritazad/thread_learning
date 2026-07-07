package AdderSubtractorProblem;

public class Main {
    
    public static void main(String[] args) {
        Counter counter = new Counter(1);
        int limit = 1000000;

        Thread adderThread = new Thread(new Adder(counter, limit));
        Thread decreserThread = new Thread(new Decreser(counter, limit));

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
