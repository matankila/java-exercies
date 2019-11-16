package il.co.ilrd.observer;

import java.util.function.Consumer;

/**
 * @author Infinity-Labs.
 */
public class CallBack<T> {
    Dispatcher<T> dispatcher;
    private final Consumer<T> cons;
    private final Runnable notifyDeath;

    public CallBack(Consumer<T> cons, Runnable notifyDeath) {
        this.cons = cons;
        this.notifyDeath = notifyDeath;
    }

    public CallBack(Consumer<T> cons) {
        this.cons = cons;
        this.notifyDeath = ()->{};
    }

    /**
     * update for dispatcher use.
     * @param msg to update.
     */
    void update(T msg) {
        cons.accept(msg);
    }

    /**
     * notifyDeath for dispacher use.
     */
    void notifyDeath() {
        notifyDeath.run();
    }

    /**
     * stop method for consumer(observer) use.
     */
    public void stop() {
        dispatcher.unregister(this);
    }
}
