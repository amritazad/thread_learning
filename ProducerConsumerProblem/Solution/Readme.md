# Producer–Consumer — Solution Notes

## Overview

This folder contains a working Solution for the Producer–Consumer problem implemented in Java. It demonstrates multiple producers and consumers sharing a bounded buffer (`Store`) with basic synchronization.

## Files

- [Producer.java](Producer.java): A `Runnable` that continuously produces items and calls `store.add(item)` when the store is not full.
- [Consumer.java](Consumer.java): A `Runnable` that continuously consumes items via `store.remove()` when the store is not empty.
- [Store.java](Store.java): The shared buffer. It stores items in an `ArrayList<Object>`, tracks `index` and `capacity`, and uses `synchronized` methods plus a `Semaphore` instance.
- [Main.java](Main.java): Starts two producer threads and two consumer threads and sets the `Store` capacity to 5.

## How this solution works

- `Main` constructs `Store(5)` and starts four threads: `Producer-1`, `Producer-2`, `Consumer-1`, `Consumer-2`.
- Each `Producer` loops forever and, if `store.isFull()` returns false, creates an `Object` and calls `store.add(item)`.
- Each `Consumer` loops forever and, if `store.isEmpty()` returns false, calls `store.remove()` and processes the returned item.
- `Store` uses `synchronized` on `add`, `remove`, and status methods to provide mutual exclusion for the `ArrayList` and index manipulation.

## Synchronization details (what's in the code)

- `synchronized` methods ensure only one thread at a time can execute `add` or `remove`, protecting `list` and `index` from race conditions.
- A `Semaphore` is present and is acquired/released within `add` and `remove`. However, as written the code acquires and then always releases the same semaphore in the same method, so the semaphore does not enforce blocking behavior between producers and consumers.

## Known issues and limitations

- Busy-waiting: Producers and consumers poll `isFull()` / `isEmpty()` in tight infinite loops, which wastes CPU when the store is full or empty.
- Semaphore misuse: The current `Semaphore` usage does not provide the intended flow-control. Because each method acquires and then releases the same semaphore, producers and consumers won't block correctly when the buffer is full or empty.
- Partial capacity guarding: `add()` and `remove()` are `synchronized`, but checks like `isFull()` and the subsequent `add()` call are separate calls; between them another thread could change the state. That said, the `synchronized` modifier on `add()` and `remove()` prevents concurrent modification during the actual operation.

## Correct approaches / Suggested improvements

- Use `ArrayBlockingQueue` (recommended): Replace `Store` with `java.util.concurrent.ArrayBlockingQueue<Object>` or `LinkedBlockingQueue<Object>`. The queue implementations handle blocking producers/consumers and simplify the code:

```java
BlockingQueue<Object> store = new ArrayBlockingQueue<>(5);
Thread p = new Thread(() -> { try { while (true) store.put(new Object()); } catch (InterruptedException ignored) {} });
Thread c = new Thread(() -> { try { while (true) store.take(); } catch (InterruptedException ignored) {} });
```

- Fix semaphore usage (if keeping custom `Store`): Use two semaphores or a pair of semaphores that track empty slots and filled slots:

```java
Semaphore emptySlots = new Semaphore(capacity); // initially capacity
Semaphore filledSlots = new Semaphore(0);       // initially 0

// producer
emptySlots.acquire();     // wait for space
addToList(item);          // synchronized modification
filledSlots.release();    // signal there is an item

// consumer
filledSlots.acquire();    // wait for item
Object item = removeFromList();
emptySlots.release();     // signal space available
```

- Use `Condition` objects with `ReentrantLock` to `await()`/`signal()` producers/consumers instead of busy-wait polling.
- Handle `InterruptedException` and provide a graceful shutdown path instead of infinite loops.

## How to build & run

From the workspace root run:

```bash
cd ProducerConsumerProblem/Solution
javac -d out $(find . -name "*.java")
java -cp out ProducerConsumerProblem.Solution.Main
```

Or run the `Main` class from your IDE; package is `ProducerConsumerProblem.Solution`.

## Next steps I can implement

- Replace the custom `Store` with an `ArrayBlockingQueue` for a compact, correct solution.
- Refactor `Store` to use two semaphores (`emptySlots` / `filledSlots`) or `ReentrantLock` + `Condition` so producers/consumers block correctly.
- Add interrupt handling and graceful shutdown of threads.

Tell me which option you'd like and I will implement it for you.
