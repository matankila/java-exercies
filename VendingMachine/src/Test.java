import java.util.*;

public class Test implements Runnable{
    private Thread t;
    private String threadName;

    Test(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void run() {
        int op = 0;
        Scanner myObj = new Scanner(System.in);
        VendingMachine vm = new VendingMachine(new VendingMachine.Iprint() {
            @Override
            public void print(String str) {
                System.out.println(str);
            }
        }
        );

        while (4 != op) {
            System.out.println("enter: 1) choose product, 2) insert coins");
            System.out.println("3) eject money, 4) exit ");
            op = myObj.nextInt();
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
                case 4:
                    break;
                default:
                    System.out.println("ileagal operation!");
                    break;
            }
        }
    }

    public void start () {
        System.out.println("Starting " +  threadName );
        if (t == null) {
            t = new Thread(this, threadName);
            t.start ();
        }
    }

    public static void main(String[] args) {
        Test vm1 = new Test( "VM1");
        vm1.start();

/*        Test vm2 = new Test( "VM2");
        vm2.start();*/
    }
}
