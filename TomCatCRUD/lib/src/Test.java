import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
    private static final int n_prod = 10;
    private final Object prodLock = new Object();
    private final Semaphore semForCons = new Semaphore(0);
    private final AtomicInteger counter = new AtomicInteger(0);

    void prod() {
        System.out.println("ping");
        counter.incrementAndGet();
        if (Objects.equals(counter.get(), n_prod)) {
            semForCons.release();
        }

        synchronized (prodLock) {
            try {
                prodLock.wait();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void cons() {
        try {
            semForCons.acquire();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("pong");
        counter.set(0);
        synchronized (prodLock) {
            prodLock.notifyAll();
        }
    }

    public static void main(String[] args) {
        Test t = new Test();

        Thread ttt = new Thread(()-> {
            while (true) {
                t.cons();
            }
        });
        ttt.start();
        for (int i = 0; i < 10; ++i) {
            Thread tt = new Thread(()->
            {
                while (true) {
                    t.prod();
                }
            });
            tt.start();
        }
    }
}
