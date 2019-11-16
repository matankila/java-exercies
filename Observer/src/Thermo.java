import java.util.ArrayList;
import java.util.List;
import il.co.ilrd.observer.*;

public class Thermo<T> {
    private final Dispatcher<T> subject = new Dispatcher<>();

    public Dispatcher<T> getSubject() {
        return subject;
    }
}
