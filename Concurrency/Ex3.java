import org.hamcrest.Condition;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class Ex3 {
    public static AtomicBoolean flag = new AtomicBoolean();
    public static Semaphore pingSem = new Semaphore(0);
    public static Semaphore pongSem = new Semaphore(1);

    static {
        flag.set(false);
    }

    public static void ping() {
        while (true) {
            try {
                pongSem.acquire();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("ping");
            pingSem.release();
        }
    }

    public static void pong() {
        while (true) {
            try {
                pingSem.acquire();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("pong");
            pongSem.release();
        }
    }

    public static void runTest() {
        Thread t1 = new Thread(()->ping());
        Thread t2 = new Thread(()->pong());

        t1.start();
        t2.start();
    }

    public static void main(String[] args) {
        Ex3.runTest();
    }
}
