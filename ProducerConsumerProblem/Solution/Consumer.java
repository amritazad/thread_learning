package ProducerConsumerProblem.Solution;

public class Consumer implements Runnable {
    
    Store store;

    public Consumer(Store store) {
        this.store = store;
    }

    @Override
    public void run() {
        while (true) {
            if (!store.isEmpty()) {
                Object item = store.remove();
                System.out.println("Consumed: Current thread: " + Thread.currentThread().getName());
            }
        }
    }
}
