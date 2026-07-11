package ProducerConsumerProblem.Problem;

public class Main {
    public static void main(String[] args) {
        Store store = new Store(10);
        Thread producerThread = new Thread(new Producer(store));
        Thread consumerThread = new Thread(new Consumer(store));

        producerThread.start();
        consumerThread.start();
    }
}
