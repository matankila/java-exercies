package il.co.ilrd.hashmap;

import java.util.*;
import java.util.stream.Collectors;

public class HashMap<K, V> implements Map<K, V>
{
    /************************ Static variables *******************************/
    private static final int DEFAULT_CAPACITY = 64;

    /************************ Local variables ********************************/
    private final List<List<Entry<K, V>>> buckets;
    private int capacity;
    private SetEntry setOfEntry;
    private SetKey setOfKeys;
    private SetValues setOfValues;
    private int modCount = 0;

    /******************************** CTORS **********************************/
    public HashMap() {
        this(DEFAULT_CAPACITY);
    }

    public HashMap(int capacity) {
        this.capacity = capacity;
        buckets = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; ++i) {
            buckets.add(i, new LinkedList<>());
        }
    }

    /********************** Private Inner classes *****************************/
    private class SetEntry extends AbstractSet<Entry<K, V>> {
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new Iterator<>() {
                private int index = 0;
                private boolean hasNextValue = false;
                private Iterator<Entry<K, V>> valuesIter =  buckets.get(0).iterator();
                private Iterator<List<Entry<K, V>>> keysIter =  buckets.iterator();
                private int knownMod = modCount;

                @Override
                public boolean hasNext() {
                    hasNextValue = valuesIter.hasNext();

                    if (!hasNextValue) {
                        while (index < capacity && keysIter.hasNext() && !valuesIter.hasNext()) {
                            ++index;
                            if (index >= capacity) { break; }
                            valuesIter =  buckets.get(index).iterator();
                            keysIter.next();
                        }

                        hasNextValue = keysIter.hasNext();
                    }

                    return (index < capacity && hasNextValue);
                }

                @Override
                public Entry<K, V> next() {
                    if (knownMod != modCount) { throw new ConcurrentModificationException(); }
                    if (!hasNext()) { throw new NoSuchElementException(); }

                    return valuesIter.next();
                }
            };
        }

        @Override
        public int size() {
            return HashMap.this.size();
        }
    }

    private class SetValues extends AbstractCollection<V> {
        @Override
        public Iterator<V> iterator() {
            return new Iterator<>() {
                private Iterator<Entry<K, V>> s = setOfEntry.iterator();
                private int knownMod = modCount;

                @Override
                public boolean hasNext() {
                    return s.hasNext();
                }

                @Override
                public V next() {
                    if (knownMod != modCount) { throw new ConcurrentModificationException(); }
                    if (!hasNext()) { throw new NoSuchElementException(); }

                    return s.next().getValue();
                }
            };
        }

        @Override
        public int size() {
            return HashMap.this.size();
        }
    }

    private class SetKey extends AbstractSet<K> {
        @Override
        public Iterator<K> iterator() {
            return new Iterator<>() {
                private Iterator<Entry<K, V>> s = setOfEntry.iterator();
                private int knownMod = modCount;

                @Override
                public boolean hasNext() {
                    return s.hasNext();
                }

                @Override
                public K next() {
                    if (knownMod != modCount) { throw new ConcurrentModificationException(); }
                    if (!hasNext()) { throw new NoSuchElementException(); }

                    return s.next().getKey();
                }
            };
        }

        @Override
        public int size() {
            return HashMap.this.size();
        }
    }

    /****************** private fucntion for internal use *****************/
    private final int hash(Object key)
    {
        return (Math.abs(Objects.hashCode(key) % capacity));
    }

    /************************** overriden functions ***********************/
    @Override
    public int size() {
        return (int)buckets.stream().filter(x->!x.isEmpty()).count();
    }

    @Override
    public boolean isEmpty() {
        return !buckets.stream().anyMatch(x->!x.isEmpty());
    }

    @Override
    public boolean containsKey(Object key) {
        return buckets.get(hash(key)).stream().anyMatch(x->Objects.equals(key,x.getKey()));

    }

    @Override
    public boolean containsValue(Object value) {
        return buckets.stream().anyMatch(x->x.stream().anyMatch(y->Objects.equals(value,y.getValue())));
    }

    @Override
    public V get(Object key) {
        return buckets.get(hash(key)).stream().filter(x->Objects.equals(x.getKey(), key)).findAny().get().getValue();
    }

    @Override
    public V put(K k, V v) {
        V oldVal = null;
        List<Entry<K, V>> listToAddEntryTo = buckets.get(hash(k));
        Optional<Entry<K,V>> o = listToAddEntryTo.stream().filter(x->Objects.equals(x.getKey(), k)).findAny();

        ++modCount;
        if (o.isPresent()) {
            oldVal =  o.get().getValue();
            o.get().setValue(v);
        }
        else { listToAddEntryTo.add(Pair.of(k, v)); }

        return oldVal;
    }

    @Override
    public V remove(Object key) {
        List<Entry<K, V>> listToAddEntryTo = buckets.get(hash(key));
        Optional<Entry<K,V>> o = listToAddEntryTo.stream().filter(x->Objects.equals(x.getKey(), key)).findAny();

        if (!o.isPresent()) { return null; }

        ++modCount;
        V deletedVal = o.get().getValue();
        listToAddEntryTo.remove(o.get());

        return deletedVal;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        Objects.requireNonNull(map, "putAll, specified map cant be null");
        map.entrySet().forEach(x->this.put(x.getKey(), x.getValue()));
    }

    @Override
    public void clear() {
        buckets.forEach(List::clear);
        ++modCount;
    }

    @Override
    public Set<K> keySet() {
        setOfEntry = ((setOfEntry == null) ? setOfEntry = new SetEntry() : setOfEntry);

        return (setOfKeys == null ? setOfKeys = new SetKey() : setOfKeys);
    }

    @Override
    public Collection<V> values() {
        setOfEntry = ((setOfEntry == null) ? setOfEntry = new SetEntry() : setOfEntry);

        return (setOfValues == null ? setOfValues = new SetValues() : setOfValues);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return (setOfEntry == null ? setOfEntry = new SetEntry() : setOfEntry);
    }
}