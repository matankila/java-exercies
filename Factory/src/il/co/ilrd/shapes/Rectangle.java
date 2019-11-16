package il.co.ilrd.shapes;

public class Rectangle implements Shape {
    private String color;

    void setColor(String color) {
        this.color = color;
    }

    @Override
    public void print() {
        System.out.println("Rectangle: ");
        System.out.println("color: " + color);
    }
}
