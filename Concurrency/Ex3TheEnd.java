import java.util.concurrent.Semaphore;

public class Ex3TheEnd {
    static final Object lockForProdCons = new Object();
    static final Semaphore sem = new Semaphore(0);
    static volatile int x;
    static int numOfReaders = 4;

    public static void prod() {
        x = 123;
        while (x >= 0) {
            for (int i = 0; i < numOfReaders; ++i) {
                try {
                    sem.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            synchronized (lockForProdCons) {
                --x;
                lockForProdCons.notifyAll();
            }
        }
    }

    public static void cons() {
        while (x >= 0) {
            synchronized (lockForProdCons) {
                System.out.println(x);
                try {
                    sem.release();
                    lockForProdCons.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread tt = new Thread(()->prod());

        tt.start();
        for (int i = 0; i < numOfReaders; ++i) {
            Thread t = new Thread(()->cons());

            t.start();
        }
    }
}
