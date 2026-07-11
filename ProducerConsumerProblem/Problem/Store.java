package ProducerConsumerProblem.Problem;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class Store {
    private List<Object> container;
    private int capacity, currInd;
    private Lock l = new ReentrantLock();

    public Store(int capacity) {
        this.capacity = capacity;
        this.container = new ArrayList<>(capacity);
        this.currInd = 0;
    }

    public void add(Object item){
        l.lock();
        try{
            container.add(item);
            currInd++;
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            l.unlock();
        }
        
    }

    public Object remove(){
        l.lock();
        try{
            return container.remove(--currInd);
        }finally{
            l.unlock();
        }
    }

    public boolean isFull(){
        l.lock();
        try{
            return currInd == capacity;
        }finally{
            l.unlock();
        }
    }

    public boolean isEmpty(){
        l.lock();
        try{
            return currInd == 0;
        }finally{
            l.unlock();
        }
    }
}
