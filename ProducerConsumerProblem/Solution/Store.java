package ProducerConsumerProblem.Solution;

import java.util.*;
import java.util.concurrent.*;

public class Store {
    List <Object> list;
    int capacity, index;
    Semaphore sem;

    public Store(int capacity) {
        this.capacity = capacity;
        this.list = new ArrayList<>(capacity);
        this.sem = new Semaphore(capacity);
        this.index = 0;
    }

    public synchronized void add(Object item){
        try{
            sem.acquire();
            list.add(item);
            index++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            sem.release();
        }
    }

    public synchronized Object remove(){
        try{
            sem.acquire();
            if (index > 0) {
                Object item = list.remove(index - 1);
                index--;
                return item;
            } else {
                return null; // or throw an exception
            }
        }catch (InterruptedException e){
            e.printStackTrace();
            return null;
        }finally{
            sem.release();
        }
    }

    public synchronized boolean isFull(){
        return index == capacity;
    }

    public synchronized boolean isEmpty(){
        return index == 0;
    }
}
