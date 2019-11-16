/*
import org.junit.Test;

import java.util.Scanner;

public class TestTest implements Runnable{
    private Thread t;
    private String threadName;

    TestTest(String threadName) {
        this.threadName = threadName;
    }

    public void run() {
        Scanner myObj = new Scanner(System.in);
        VendingMachine vm = new VendingMachine();

        System.out.println("enter: 1) choose product, 2) insert coins");
        System.out.println("3) eject money, 4) exit ");
        int op = myObj.nextInt();
        while (4 != op) {
            switch (op) {
                case 1:
                    System.out.println("enter product id");
                    op = myObj.nextInt();
                    vm.chooseProduct(op);
                    break;
                case 2:
                    System.out.println("enter amount of coins to enter");
                    op = myObj.nextInt();
                    vm.insertMoney(op);
                    break;
                case 3:
                    vm.ejectMoney();
                    break;
                default:
                    System.out.println("ileagal operation!");
                    break;
            }

            System.out.println("enter: 1) choose product, 2) insert coins");
            System.out.println("3) eject money, 4) exit ");
            op = myObj.nextInt();
        }
    }

    public void start () {
        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }

    @Test
    public void TestMultipleVM() {
        TestTest vm1 = new TestTest( "VM1");
        vm1.start();

        TestTest vm2 = new TestTest( "VM2");
        vm2.start();
    }
}*/
