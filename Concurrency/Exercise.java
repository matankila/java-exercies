import org.junit.experimental.theories.Theories;

import java.util.LinkedList;
import java.util.List;

public class Exercise {
    private static List<Integer> l = new LinkedList<>();
    private static final Object lockForWriteReadOp = new Object();

    public static void prod() {
        synchronized (lockForWriteReadOp) {
            l.add(100);
            //semaphore post
        }
    }

    public static void cons() {
        synchronized (lockForWriteReadOp) {
            if (l.isEmpty()) {
                //semaphore wait
                return;
            }

            System.out.println(l.get(0));
            l.remove(0);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 3; ++i) {
            Thread t1 = new Thread(() -> prod());
            Thread t2 = new Thread(() -> cons());

            t1.start();
            t2.start();
        }
    }
}
