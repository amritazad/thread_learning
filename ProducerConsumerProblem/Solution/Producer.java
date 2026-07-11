package ProducerConsumerProblem.Solution;

public class Producer implements Runnable {
    
    Store store;

    public Producer(Store store) {
        this.store = store;
    }

    @Override
    public void run() {
        while (true) {
            if (!store.isFull()) {
                Object item = new Object(); // Replace with actual item creation logic
                store.add(item);
                System.out.println("Produced: Current thread: " + Thread.currentThread().getName());
            }
        }
    }

}
