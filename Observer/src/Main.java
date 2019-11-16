import il.co.ilrd.observer.*;

import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        Thermo<Integer> s = new Thermo<>();
        User<Integer> matan = new User<>();
        User<Integer> ariav = new User<>();
        User<Integer> bardugi = new User<>(new CallBack<Integer>(new Consumer<Integer>() {
            @Override
            public void accept(Integer var1) {
                System.out.println("my temp: " + var1);
            }
        }, ()-> System.out.println("your dead")
        ));

        s.getSubject().register(ariav.call);
        s.getSubject().register(matan.call);
        s.getSubject().register(bardugi.call);
        s.getSubject().notifyAllObservers(13);
        s.getSubject().unregister(ariav.call);
        s.getSubject().notifyAllObservers(13);
        bardugi.call.stop();
        s.getSubject().notifyAllObservers(13);
        s.getSubject().stop();
        s.getSubject().notifyAllObservers(13);
    }
}
