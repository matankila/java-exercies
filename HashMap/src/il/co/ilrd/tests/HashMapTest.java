package il.co.ilrd.tests;

import il.co.ilrd.hashmap.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Iterator;
import java.util.Objects;

import static org.junit.Assert.*;

public class HashMapTest {

    @Test
    public void size() {
        HashMap<Integer, Integer> h = new HashMap<>();

        assertEquals(h.size(), 0);
        for (int i = 0; i < 64; ++i) {
            h.put(i, Objects.hashCode(i));
            assertFalse(h.isEmpty());
        }

        assertEquals(h.size(), 64);
        h.clear();
        assertEquals(h.size(), 0);
    }

    @Test
    public void isEmpty() {
        HashMap<Integer, Integer> h = new HashMap<>();

        assertTrue(h.isEmpty());
        for (int i = 0; i < 64; ++i) {
            h.put(i, Objects.hashCode(i));
            assertFalse(h.isEmpty());
            assertEquals(h.size(), i + 1);
        }

        assertFalse(h.isEmpty());
        h.clear();
        assertTrue(h.isEmpty());
    }

    @Test
    public void containsKey() {
        HashMap<Integer, Integer> h = new HashMap<>();

        assertFalse(h.containsKey(null));
        for (int i = 0; i < 64; ++i) {
            h.put(i, Objects.hashCode(i));
            assertEquals(h.size(), i + 1);
            assertFalse(h.isEmpty());
        }

        for (int i = 0; i < 64; ++i) {
            assertTrue(h.containsKey(i));
        }

        assertFalse(h.containsKey(null));
        assertFalse(h.containsKey(65));
    }

    @Test
    public void containsValue() {
        HashMap<Integer, Integer> h = new HashMap<>();
        assertFalse(h.containsValue(null));
        for (int i = 0; i < 64; ++i) {
            h.put(i, Objects.hashCode(i));
            assertFalse(h.isEmpty());
            assertEquals(h.size(), i + 1);
        }

        for (int i = 0; i < 64; ++i) {
            assertTrue(h.containsValue(Objects.hashCode(i)));
        }

        assertFalse(h.containsValue(null));
    }

    @Test
    public void get() {
        HashMap<Integer, Integer> h = new HashMap<>();

        for (int i = 0; i < 64; ++i) {
            h.put(i, Objects.hashCode(i));
            assertEquals(h.size(), i + 1);
            assertFalse(h.isEmpty());
        }

        for (int i = 0; i < 64; ++i) {
            assertEquals((Object) h.get(i), (Object) Objects.hashCode(i));
        }
    }

    @Test
    public void put() {
        HashMap<Integer, Integer> h = new HashMap<>();

        for (int i = 0; i < 64; ++i) {
            if (0 == i) {
                assertEquals((long)h.put(1, i), i);
            }
            else {
                assertEquals((long) h.put(1, i), i - 1);
            }
            assertEquals(h.size(), 1);
            assertFalse(h.isEmpty());
        }
    }

    @Test
    public void remove() {
        HashMap<Integer, Integer> h = new HashMap<>();

        for (int i = 0; i < 64; ++i) {
            h.put(i, Objects.hashCode(i));
            assertEquals(h.size(), i + 1);
            assertFalse(h.isEmpty());
        }

        for (int i = 0; i < 64; ++i) {
            h.remove(i);
            assertEquals(h.size(), 64 - i - 1);
            if (i != 63) { assertFalse(h.isEmpty()); }
            else { assertTrue(h.isEmpty());}
        }
    }

    @Test
    public void putAll() {
        HashMap<Integer, Integer> h = new HashMap<>();
        HashMap<Integer, Integer> h2 = new HashMap<>();

        for (int i = 0; i < 64; ++i) {
            h.put(i, Objects.hashCode(i));
            assertEquals(h.size(), i + 1);
            assertFalse(h.isEmpty());
            h2.put(i, Objects.hashCode(i));
            assertEquals(h2.size(), i + 1);
            assertFalse(h2.isEmpty());
        }

        int size = h.size();
        h.putAll(h2);
        assertEquals(h.size(), size);
        assertFalse(h.containsKey(null));
        assertFalse(h.containsValue(null));
        h.put(null, null);
        assertTrue(h.containsKey(null));
        assertTrue(h.containsValue(null));
    }

    @Test
    public void clear() {
        HashMap<Integer, Integer> h = new HashMap<>();

        for (int i = 0; i < 64; ++i) {
            h.put(i, Objects.hashCode(i));
            assertEquals(h.size(), i + 1);
            assertFalse(h.isEmpty());
        }

        h.clear();
        assertTrue(h.isEmpty());
        assertEquals(h.size(), 0);

    }

    @Test
    public void keySet() {
        HashMap<Integer, Integer> h = new HashMap<>();

        for (int i = 0; i < 64; ++i) {
            h.put(i, Objects.hashCode(i));
            assertEquals(h.size(), i + 1);
            assertFalse(h.isEmpty());
        }

        int i = 0;
        Iterator iter = h.keySet().iterator();
        while (iter.hasNext()) {
            assertEquals(iter.next(), i);
            ++i;
        }
    }

    @Test
    public void values() {
        HashMap<Integer, Integer> h = new HashMap<>();

        for (int i = 0; i < 64; ++i) {
            h.put(i, Objects.hashCode(i));
            assertEquals(h.size(), i + 1);
            assertFalse(h.isEmpty());
        }

        int i = 0;
        Iterator iter = h.values().iterator();
        while (iter.hasNext()) {
            assertEquals(iter.next(), Objects.hashCode(i));
            ++i;
        }
    }

    @Test
    public void entrySet() {
        HashMap<Integer, Integer> h = new HashMap<>();

        for (int i = 0; i < 64; ++i) {
            h.put(i, Objects.hashCode(i));
            assertEquals(h.size(), i + 1);
            assertFalse(h.isEmpty());
        }

        int i = 0;
        Iterator iter = h.entrySet().iterator();
        while (iter.hasNext()) {
            assertEquals(iter.next(),Pair.of(i, Objects.hashCode(i)));
            ++i;
        }
    }
}
