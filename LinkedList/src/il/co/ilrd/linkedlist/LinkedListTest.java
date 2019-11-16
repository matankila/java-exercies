package il.co.ilrd.linkedlist;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static org.junit.Assert.*;

public class LinkedListTest {

    @Test
    public void testPopOnEmptyList() {
        LinkedList<String> ls = new LinkedList<>();
        try {
            ls.popFront();
        }
        catch (Exception e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testSize() {
        LinkedList<Integer> ls = new LinkedList<>();
        for (int i = 0; i < 100; ++i) {
            ls.pushFront(i);
            ls.pushFront(i);
            ls.pushFront(i);
            for (int j = 0; j < 2; ++j) {
                try {
                    ls.popFront();
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
        }

        assertEquals(ls.size(), 100);
    }

    @Test
    public void testEmptiness() {
        LinkedList<Integer> ls = new LinkedList<>();
        assertEquals(ls.isEmpty(), true);
        for (int i = 0; i < 100; ++i) {
            ls.pushFront(i);
            ls.pushFront(i);
            ls.pushFront(i);
            assertEquals(ls.isEmpty(), false);
            for (int j = 0; j < 2; ++j) {
                try {
                    ls.popFront();
                    assertEquals(ls.isEmpty(), false);
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
        }

        assertEquals(ls.size(), 100);
        assertEquals(ls.isEmpty(), false);
        for (int i = 0; i < 100; ++i) {
            try {
                assertEquals(ls.isEmpty(), false);
                ls.popFront();
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }

        assertEquals(ls.isEmpty(), true);

    }

    @Test
    public void testGetAll() {
        LinkedList<Integer> listAfter = new LinkedList<>();
        LinkedList<Integer> list1 = new LinkedList<>();
        LinkedList<Integer> list2 = new LinkedList<>();
        int[] arr1 = new int[6];
        int[] arr2 = new int[6];
        int index = 0;

        list1.pushFront(1);
        list1.pushFront(2);
        list1.pushFront(3);
        list2.pushFront(22);
        list2.pushFront(33);
        list2.pushFront(69);

        listAfter.pushFront(22);
        listAfter.pushFront(33);
        listAfter.pushFront(69);
        listAfter.pushFront(1);
        listAfter.pushFront(2);
        listAfter.pushFront(3);
        list1.addAll(list2);
        for (int i: list1) {
            arr1[index] = i;
            ++index;
        }

        index = 0;
        for (int i: listAfter) {
            arr2[index] = i;
            ++index;
        }

        for (int i: arr2) {
            System.out.println(i);
        }

        for (int i: arr1) {
            System.out.println(i);
        }

        assertArrayEquals(arr1, arr2);
    }

    @Test
    public void testReverse() {
        LinkedList<Integer> list1 = new LinkedList<>();
        LinkedList.reverse(list1);
        int[] exp = {22, 33, 69, 1, 2, 3};
        int[] actual = new int[6];
        int index = 0;

        list1.pushFront(22);
        list1.pushFront(33);
        list1.pushFront(69);
        list1.pushFront(1);
        list1.pushFront(2);
        list1.pushFront(3);
        list1 = LinkedList.reverse(list1);
        for (int i: list1) {
            actual[index] = i;
            ++index;
        }

        assertArrayEquals(actual, exp);
    }

}
