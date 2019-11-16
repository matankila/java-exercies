package il.co.ilrd.observer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class Subject<T> {
    List<CallBack<T>> callBacks = new ArrayList<>();

    public void unregister(CallBack<T> callBack) {
        callBacks.remove(callBack);
    }

    public void register(CallBack<T> callBack) {
        callBacks.add(callBack);
        callBack.subject = this;
    }

    public void notifyAllObservers(T t) {
        for (CallBack<T> c : callBacks) {
            c.update(t);
        }
    }

    public void stop() {
        Iterator<CallBack<T>> i = callBacks.iterator();

        while (i.hasNext()) {
            CallBack<T> c = i.next();
            c.unregister();
            c.stop();
            i = callBacks.iterator();
        }
    }
}
