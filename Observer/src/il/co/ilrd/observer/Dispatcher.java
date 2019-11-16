package il.co.ilrd.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Dispatcher<T> {
    private final List<CallBack<T>> callBacks = new ArrayList<>();

    public void unregister(CallBack<T> callBack) {
        callBacks.remove(callBack);
        callBack.dispatcher = null;
    }

    public void register(CallBack<T> callBack) {
        callBacks.add(callBack);
        callBack.dispatcher = this;
    }

    public void notifyAllObservers(T t) {
        for (CallBack<T> c : callBacks) {
            c.update(t);
        }
    }

    public void stop() {
        for (CallBack<T> cb : callBacks) {
            cb.notifyDeath();
        }

        callBacks.clear();
    }
}
