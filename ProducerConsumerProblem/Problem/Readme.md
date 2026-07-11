# Producer–Consumer Problem — Implementation Notes

## Overview

This small example demonstrates a classic Producer–Consumer concurrency problem implemented in Java. The project shows a simple shared buffer (`Store`) and two worker types: producers (`Producer`) that add items and consumers (`Consumer`) that remove items.

## Files

- [Producer.java](Producer.java): A `Runnable` that continuously produces items and appends them to the `Store` when space is available.
- [Consumer.java](Consumer.java): A `Runnable` that continuously consumes items from the `Store` when available.
- [Store.java](Store.java): The shared buffer implementation. Uses a `ReentrantLock` to guard internal state.
- [Main.java](Main.java): Creates a `Store` and starts one producer and one consumer thread.

## How this implementation works

- `Store` keeps an `ArrayList<Object>` as the buffer with a fixed `capacity`. It uses a `ReentrantLock` to protect updates to `container` and the `currInd` index.
- `Producer.run()` continuously checks `store.isFull()` and, if not full, creates a new `Object` and calls `store.add(item)`.
- `Consumer.run()` continuously checks `store.isEmpty()` and, if not empty, calls `store.remove()`.

Important behavior notes:
- The producer and consumer use polling (busy-waiting) — they repeatedly test `isFull()` / `isEmpty()` inside an infinite loop and only act when the condition changes.
- `Store` methods (`add`, `remove`, `isFull`, `isEmpty`) acquire the `ReentrantLock` briefly to perform the operation, so each operation is thread-safe.

## How to build & run

From the workspace root, compile and run using `javac`/`java` with package paths. Example commands (macOS / Unix):

```bash
cd ProducerConsumerProblem/Problem
javac -d out $(find . -name "*.java")
java -cp out ProducerConsumerProblem.Problem.Main
```

Or use your IDE to run the `Main` class in package `ProducerConsumerProblem.Problem`.

## Known issues and limitations

- Busy-waiting: The producer and consumer loops spin continuously, which will consume CPU even when the buffer is full/empty. This is inefficient and can degrade performance.
- Notification missing: There is no mechanism to block a producer when the buffer is full or to block a consumer when the buffer is empty. Instead, the code relies on repeated checks.
- Capacity-check race window: Although `Store` methods are individually synchronized with a lock, the `isFull()` check in the producer and the subsequent `add()` call are two separate lock acquisitions. This can lead to a producer observing `!isFull()` and then failing to add because another thread filled the buffer between the calls. Currently `add()` will still throw a runtime exception if `ArrayList` remove/add out-of-bounds occurs; the code does not guard against capacity in `add()` itself.

## Suggested improvements

- Use a blocking queue: Replace `Store` with `java.util.concurrent.ArrayBlockingQueue` or `LinkedBlockingQueue`. These classes handle blocking producers/consumers and are well-tested.

- Use `Condition` objects: If you want to keep the custom `Store`, use `ReentrantLock` plus `Condition` variables to `await()` when full/empty and `signal()` when items are added/removed. This avoids busy-waiting and is more efficient.

- Example (high-level change):

1) Replace internal polling with `ArrayBlockingQueue<Object>`:

```java
// simple replacement inside Main
BlockingQueue<Object> store = new ArrayBlockingQueue<>(10);
Thread producer = new Thread(() -> {
	try { while (true) store.put(new Object()); } catch (InterruptedException ignored) {}
});
Thread consumer = new Thread(() -> {
	try { while (true) { Object o = store.take(); } catch (InterruptedException ignored) {}
});
```

2) Or convert `Store` to use a `Condition` pair:

```java
private final Lock lock = new ReentrantLock();
private final Condition notFull = lock.newCondition();
private final Condition notEmpty = lock.newCondition();

public void add(Object item) throws InterruptedException {
	lock.lock();
	try {
		while (currInd == capacity) notFull.await();
		// add item
		notEmpty.signal();
	} finally { lock.unlock(); }
}

public Object remove() throws InterruptedException {
	lock.lock();
	try {
		while (currInd == 0) notEmpty.await();
		// remove item
		notFull.signal();
	} finally { lock.unlock(); }
}
```

## Next steps I can help with

- Convert `Store` to a blocking implementation (Condition-based) or swap to `ArrayBlockingQueue`.
- Add a graceful shutdown signal (interrupt handling) and unit tests.

If you want, I can implement the `ArrayBlockingQueue` version or refactor `Store` to use `Condition` and update the producer/consumer to handle `InterruptedException` properly.
