package il.co.ilrd.factory;

import java.util.HashMap;
import java.util.function.Function;

public class Factory<K,T,R> {
    HashMap<K, Function<T, ? extends R>> map = new HashMap<>();

    public void add(K key, Function<T, ? extends R> funcRef) {
        map.put(key, funcRef);
    }

    public R create(K key, T args) {
        return (map.get(key).apply(args));
    }
}