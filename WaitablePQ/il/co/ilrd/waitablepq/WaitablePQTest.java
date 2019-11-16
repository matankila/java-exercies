package il.co.ilrd.waitablepq;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

public class WaitablePQTest {
    static WaitablePQ<Integer> w;
    static List<Runnable> l;
    static int i;

    public static void enq(int n) {
        for (int i = 0; i < n; ++i) {
            System.out.println("hey im now enq" + (i * 31 + 31));
            w.enqueue(i * 31 + 31);
        }
    }

    public static void deq(int n) {
        for (int i = 0; i < n; ++i) {
            try {
                System.out.println("hey im now deq" + w.dequeue());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void timedDeq(int n) {
        for (int i = 0; i < n; ++i) {
            try {
                System.out.println("hey im now deq " + w.dequeueWithTO(10, TimeUnit.SECONDS));
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (TimeoutException ee) {
                ee.printStackTrace();
            }
        }
    }

    @BeforeAll
    public static void bar() {
        w = new WaitablePQ<>();
        l = new ArrayList<>();

        for (i = 0; i < 2; ++i) {
            if (0 != i % 2) {
                l.add(()->enq(2));
            }
            else {
                l.add(()->timedDeq(2));
            }
        }
    }

    @Test
    public void foo(){
        for (int i = 0; i < 2; ++i) {
            Thread t = new Thread(l.get(i));
            t.start();
        }
    }
}
