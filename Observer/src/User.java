import java.util.function.Consumer;
import il.co.ilrd.observer.*;

public class User<T> implements Consumer<T>, Runnable {
    private T userTemp;
    CallBack<T> call;

    public User(CallBack<T> call) {
        this.call = call;
    }

    public User() {
        call = new CallBack<>(this::accept, this::run);
    }

    @Override
    public void accept(T var1) {
        userTemp = var1;
        System.out.println("user: " + this + ", my temp: " + userTemp);
    }

    @Override
    public void run() {
        System.out.println("dead");
    }

    public void setUserTemp(T newTemp) {
        userTemp = newTemp;
    }
}
