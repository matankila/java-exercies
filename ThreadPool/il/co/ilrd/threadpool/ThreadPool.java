package il.co.ilrd.threadpool;

import il.co.ilrd.waitablepq.WaitablePQ;
import java.util.Objects;
import java.util.concurrent.*;

public class ThreadPool {
    private volatile int numOfThread;
    private volatile boolean paused = false;
    private volatile boolean shutdown = false;
    private final Semaphore pauseSem = new Semaphore(0);
    private final Semaphore terminate = new Semaphore(0);
    private final Object pauseResumeLock = new Object();
    private WaitablePQ<Task<?>> workQ = new WaitablePQ<>(((task, t1) -> Objects.compare(t1.priority.getNumVal(),
            task.priority.getNumVal(),
            (x, y)->x.compareTo(y))));

    /**************************************************** CTOR ********************************************************/
    public ThreadPool(int numOfThread) {
        this.numOfThread = numOfThread;
    }

    /****************************************** Helper Private Methods ************************************************/
    private void shutNumOfThreads(int numToShut) {
        for (int i = 0; i < numToShut; ++i) {
            submit(()-> {
                ((WorkerThread) Thread.currentThread()).running = false;
                terminate.release();
            }, SystemUsePriority.SHUTDOWN);
        }
    }

    private void addNewThreads(int numToAdd) {
        for (int i = 0; i < numToAdd; ++i) {
            new WorkerThread().start();
        }
    }

    /******************************** Private Classes/Interfaces/Enums for inner use *********************************/
    private class WorkerThread extends Thread {
        private boolean running = true;

        @Override
        public void run() {
            while (running) {
                try {
                    workQ.dequeue().taskRun();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Task<T> {
        private final Callable<T> callable;
        private final Priority priority;
        private final TaskFuture future = new TaskFuture();
        private final Object lockForTask = new Object();
        private State state = State.WAITING;

        private class TaskFuture implements Future<T> {
            private Object lockForResult = new Object();
            private T result;

            private void setResult(T t) {
                result = t;
                state = State.DONE;
                synchronized (lockForResult) {
                    lockForResult.notify();
                }
            }

            @Override
            public boolean cancel(boolean b) {
                if (state == State.WAITING) {
                    synchronized (lockForResult) {
                        if (state == State.WAITING) {
                            state = State.CANCELLED;

                            return true;
                        }
                    }
                }

                return false;
            }

            @Override
            public boolean isCancelled() {
                return (state == State.CANCELLED);
            }

            @Override
            public boolean isDone() {
                return (state == State.DONE);
            }

            @Override
            public T get() throws InterruptedException, ExecutionException {
                if (!isDone()) {
                    synchronized (lockForResult) {
                        if (!isDone()) {
                            lockForResult.wait();
                        }
                    }
                }

                return result;
            }

            @Override
            public T get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
                if (!isDone()) {
                    synchronized (lockForResult) {
                        if (!isDone()) {
                            lockForResult.wait(TimeUnit.MILLISECONDS.convert(l, timeUnit));
                            if (!isDone()) {
                                throw new TimeoutException();
                            }
                        }
                    }
                }

                return result;
            }
        }

        private Task(Callable<T> callable, Priority priority) {
            this.callable = callable;
            this.priority = priority;
        }

        private void taskRun() {
            if (!future.isCancelled()) {
                try {
                    future.setResult(callable.call());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private enum State {WAITING, DONE, CANCELLED}

    private interface Priority {
        int getNumVal();
    }

    private enum SystemUsePriority implements Priority {
        SHUTDOWN(0),
        PAUSE(11);

        private final int numVal;

        SystemUsePriority(int numVal) {
            this.numVal = numVal;
        }

        @Override
        public int getNumVal() {
            return numVal;
        }
    }

    /***************************************************** API methods ************************************************/
    public enum UserPriority implements Priority {
        MIN(1),
        DEFAULT(5),
        MAX(10);

        private final int numVal;

        UserPriority(int numVal) {
            this.numVal = numVal;
        }

        @Override
        public int getNumVal() {
            return numVal;
        }
    }

    public void executePool() {
        for (int i = 0; i < numOfThread; ++i) {
            new WorkerThread().start();
        }
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return terminate.tryAcquire(numOfThread, timeout, unit);
    }

    public void shutdown() {
        shutNumOfThreads(numOfThread);
        shutdown = true;
    }

    public void setNumOfThreads(int numOfThreads) {
        if (numOfThreads > numOfThread) {
            if (!paused) {
                addNewThreads(numOfThreads - numOfThread);
            }

            numOfThread = numOfThreads;

            return;
        }

        shutNumOfThreads((numOfThread - numOfThreads));
    }

    public void pause() {
        synchronized (pauseResumeLock) {
            paused = true;
            for (int i = 0; i < numOfThread; ++i) {
                submit(() -> {
                    try {
                        pauseSem.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }, SystemUsePriority.PAUSE);
            }
        }
    }

    public void resume() {
        synchronized (pauseResumeLock) {
            paused = false;
            pauseSem.release(numOfThread);
        }
    }

    public Future<Void> submit(Runnable task, Priority priority) {
        return submit(task, priority, null);
    }

    public <T> Future<T> submit(Runnable task, Priority priority, T value) {
        return submit(Executors.callable(task, value), priority);
    }

    public <T> Future<T> submit(Callable<T> call, Priority priority) {
        Objects.requireNonNull(priority, "submit, priority cant be null.");
        if (shutdown) { throw new RejectedExecutionException("submit, thread pool is shutdown."); }

        Task<T> t = new Task<>(call, priority);
        workQ.enqueue(t);

        return t.future;
    }

    public <T> Future<T> submit(Callable<T> call) {
        return submit(call, UserPriority.DEFAULT);
    }

    public static void main(String[] args) {
        int i = 0;

        while (i < 10) {
            ThreadPool tp = new ThreadPool(2);

            tp.submit(() -> {
                System.out.println("hello Regular priority");
                return 0;
            });
            tp.submit(() -> {
                System.out.println("hello Max priority");
                return 0;
            }, UserPriority.MAX);

            ++i;
            tp.executePool();
            tp.shutdown();
            try {
                tp.awaitTermination(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}