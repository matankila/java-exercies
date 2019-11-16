package il.co.ilrd.shapes;

import java.util.function.Function;
import il.co.ilrd.factory.*;

public class ShapesFactory {

    public static void main(String[] args) {
        Factory<String, String, Shape> sf = new Factory<>();
        sf.add("Rec", new Function<String, Shape>() {
            @Override
            public Shape apply(String s) {
                Rectangle r = new Rectangle();
                r.setColor(s);
                return r;
            }
        });

        sf.create("Rec", "green").print();

        System.out.println("");

        sf.add("Circle", x->{
            Circle c = new Circle();
            c.setColor(x);

            return c;
        });

        sf.create("Circle", "pink").print();

        System.out.println("");

        sf.add("Rec1",ShapesFactory::creation);
        sf.create("Rec1", "crimson").print();

        System.out.println("");

        sf.add("Cir",Circle::setC);
        sf.create("Cir", "cyan").print();
    }

    private static Shape creation(String color) {
        Circle c = new Circle();
        c.setColor(color);

        return c;
    }
}
