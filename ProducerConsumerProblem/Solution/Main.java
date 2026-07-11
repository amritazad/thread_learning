package ProducerConsumerProblem.Solution;

public class Main {
    public static void main(String[] args) {
        Store store = new Store(5); // Set the capacity of the store

        Thread producerThread1 = new Thread(new Producer(store), "Producer-1");
        Thread producerThread2 = new Thread(new Producer(store), "Producer-2");
        Thread consumerThread1 = new Thread(new Consumer(store), "Consumer-1");
        Thread consumerThread2 = new Thread(new Consumer(store), "Consumer-2");

        producerThread1.start();
        producerThread2.start();
        consumerThread1.start();
        consumerThread2.start();
    }
}
