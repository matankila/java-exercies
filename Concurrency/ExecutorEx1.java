import java.util.concurrent.*;

public class ExecutorEx1 {
    static ExecutorService executor = Executors.newFixedThreadPool(4);

    public static void main(String[] args) {
        Callable<String> task = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "hey i just met you";
            }
        };

        Future<String> future = executor.submit(task);
        try {
            System.out.println(future.get());
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }
}