package il.co.ilrd.shapes;

public class Circle implements Shape {
    private String color;

    void setColor(String color) {
        this.color = color;
    }

    static Circle setC(String color) {
        Circle r = new Circle();
        r.setColor(color);

        return r;
    }

    @Override
    public void print() {
        System.out.println("Circle: ");
        System.out.println("color: " + color);
    }
}
