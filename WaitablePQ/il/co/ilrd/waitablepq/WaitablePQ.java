package il.co.ilrd.waitablepq;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Infinity-Labs
 * Generic Waitable priority queue.
 */
public class WaitablePQ<E> {
    private final PriorityQueue<E> pq;
    private final Object lockForPQ = new Object();
    private Semaphore sem = new Semaphore(0);

    /**
     * Waitable priority queue CTOR(comperable).
     */
    public WaitablePQ() {
        pq = new PriorityQueue<>();
    }

    /**
     * Waitable priority queue CTOR(comparator).
     */
    public WaitablePQ(Comparator<? super E> comparator) {
        pq = new PriorityQueue<>(comparator);
    }

    /**
     * @param arg to enqueue to the queue.
     * @return boolean if operation succeeded.
     */
    public boolean enqueue(E arg) {
        boolean isAdded = false;

        synchronized(lockForPQ) {
            isAdded = pq.add(arg);
        }

        sem.release();

        return isAdded;
    }

    /**
     * @return E dequeued value.
     */
    public E dequeue() throws InterruptedException{
        sem.acquire();
        synchronized(lockForPQ) {
            return pq.poll();
        }
    }

    /**
     * @return E dequeuedWith TimeOut value.
     */
    public E dequeueWithTO(long time, TimeUnit unit)
            throws TimeoutException,InterruptedException{
        if (sem.tryAcquire(time, unit)) { }
        synchronized (lockForPQ) {
            return pq.poll();
        }
    }

    public boolean isEmpty() {
        return pq.isEmpty();
    }
}
