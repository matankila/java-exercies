package il.co.ilrd.pair;

import java.util.*;

/**
 * Pair implementation.
 * @author Matan keler.
 * @param <K> key.
 * @param <V> value.
 */
public class Pair<K, V> implements Map.Entry<K, V> {
    private final K key;
    private V value;

    private Pair(K key, V value) {
        this.value =value;
        this.key = key;
    }
    
    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V oldValue = this.value;
        this.value = value;

        return oldValue;
    }

    @Override
    public String toString() {
        // return "<key: " + Objects.toString(key) + ", value: " + Objects.toString(value) + ">";
        return "<key: " + ((null == key) ? "null" : key.toString()) + ", value: " + ((null == value) ? "null" : value.toString()) + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (null == o) { return false; }
        if(!(o instanceof Pair)) { return false; }

        Pair<?, ?> pair = (Pair<?, ?>) o;
        if (!(key == pair.key || key != null && key.equals(pair.key))) { return false; }

        return (value == pair.value || value != null && value.equals(pair.value));
    }

    @Override
    public int hashCode() {
        //version 1:
        return Objects.hash(key, value);
        //version 2:
        /* return 31 * ((null == key) ? 0 : key.hashCode()) + ((null == value) ? 0 : value.hashCode()); */
    }

    /**
     * create new pair with the key and value swapped.
     * @param p pair
     * @return new pair with the key and value swapped.
     */
    public static <K, V> Pair<V, K> swap(Pair<K, V> p) {
        Objects.requireNonNull(p, "swap, p cant be null");
        return (new Pair<V, K>(p.value, p.key));
    }

    /**
     * create new instance of Pair by given values.
     * @param key
     * @param value
     * @return new Pair with the inserted values.
     */
    public static <K, V> Pair<K, V> of(K key, V value) {
        return (new Pair<K, V>(key, value));
    }

    private static <T> Pair<T, T> minMaxImp(T[] a, Comparator<? super T> cons) {
        Objects.requireNonNull(a, "minmix(T[] arr, Comparator<? super T> cons), arr cant be null");
        Objects.requireNonNull(cons, "minmix(T[] arr, Comparator<? super T> cons), cons cant be null");
        T min;
        T max;

        if (a.length < 1) {
            return null;
        }
        else if (a.length == 1) {
            return new Pair<>(a[0], a[0]);
        }

        if (cons.compare(a[0], a[1]) > 0){
            max = a[0];
            min = a[1];
        }
        else {
            max = a[1];
            min = a[0];
        }

        for (int i = 2; i <= a.length - 1; i++) {
            if (cons.compare(a[i], max) > 0) {
                max = a[i];
            }
            else if (cons.compare(min, a[i]) > 0) {
                min = a[i];
            }
        }

        return (new Pair<>(min, max));
    }

    /**
     * create new pir where the min value is the key and the value is maximum.
     * @param arr array of T.
     * @throws NullPointerException if the array is null refernce.
     * @return new pir where the min value is the key and the value is maximum, if the array is empty return null.
     */
    public static <T extends Comparable<? super T>> Pair<T, T> minMax(T[] arr) {
        return (minMaxImp(arr, (a,b)->a.compareTo(b)));
        /* possible to change to: T::compareTo */
    }

    /**
     * create new pir where the min value is the key and the value is maximum.
     * @param arr array of T.
     * @param cons comperator supplied by the user.
     * @throws NullPointerException if the array/ comparator is null refernce.
     * @return new pir where the min value is the key and the value is maximum, if the array is empty return null.
     */
    public static <T> Pair<T, T> minMax(T[] arr, Comparator<? super T> cons) {
        return (minMaxImp(arr, cons));
    }
}
