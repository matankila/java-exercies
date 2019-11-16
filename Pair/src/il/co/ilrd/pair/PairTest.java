package il.co.ilrd.pair;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import static org.junit.Assert.*;

public class PairTest {
    @Test
    public void testminmax() {
        Integer[] arr = {-999999999,3,-1,99,1000,11,1000009,-999};
        Pair<Integer, Integer> p = Pair.minMax(arr);
        Integer i = -999999999;
        assertEquals(p.getKey(), i);
    }

    @Test
    public void testGetAndSet() {
        Integer i = 1;
        Integer ii = 2;

        Pair<Integer, Integer> p = Pair.of(1, 1);
        assertEquals(p.getKey(), i);
        assertEquals(p.getValue(), i);
        assertEquals(p.setValue(2), i);
        assertEquals(p.getValue(), ii);
    }

    @Test
    public void testSwap() {
        Pair<Integer, Integer> p = Pair.of(-2, 1);
        p = Pair.swap(p);
        Pair<Integer, Integer> pp = Pair.of(1, -2);
        assertTrue(p.equals(pp));
    }

    @Test
    public void testEq() {
        Pair<Integer, Integer> p = Pair.of(null, null);
        assertFalse(p.equals(Pair.of("str", 1.0)));
    }
}
