import java.util.*;

public class VendingMachine extends Thread{
    private int moneyInserted = 0;
    private int selectedSlot = 0;
    public volatile States currentState = States.INIT;
    private final List<Slot> products = new ArrayList<Slot>();
    private Thread t;
    private volatile boolean enable = true;
    private volatile long lastTime = 0;
    private Iprint print;

    public VendingMachine(Iprint printVar) {
        print = printVar;
        TimedTask task = new TimedTask(this);
        t = new Thread(task);
        t.start();
        fillMachine();
        currentState.allOk(this);
    }

    private enum States {
        INIT {
            @Override
            void allOk(VendingMachine vm) {
                handelCurrentState(vm, States.IDLE);
            }
        },
        IDLE {
            @Override
            void insertCoin(VendingMachine vm, int amount) {
                vm.moneyInserted = amount;
                handelCurrentState(vm, States.COLLECT);
            }

            @Override
            void eject(VendingMachine vm) {
                System.out.println("take your money");
                vm.moneyInserted = 0;
                handelCurrentState(vm, States.IDLE);
            }
        },
        COLLECT {
            @Override
            void eject(VendingMachine vm) {
                vm.print.print("take your money");
                vm.moneyInserted = 0;
                handelCurrentState(vm, States.IDLE);
            }

            @Override
            void insertCoin(VendingMachine vm, int amount) {
                vm.moneyInserted += amount;
                handelCurrentState(vm, States.COLLECT);
            }

            @Override
            void selectSlot(VendingMachine vm, int slot) {
                if ((vm.products.size() - 1) < slot) {
                    vm.print.print("product id non existing choose again");
                } else if (!vm.products.get(slot).isProdAvailable()) {
                    vm.print.print("product isnt avaliable");
                } else if(vm.products.get(slot).getProdPrice() > vm.moneyInserted) {
                    vm.print.print("insert more money");
                } else {
                    vm.products.get(slot).decAmount();
                    vm.selectedSlot = slot;
                    vm.print.print("thanks for buying :)");
                    handelCurrentState(vm, States.TRANSACTION_COMPLETE);
                }
            }
        },
        TRANSACTION_COMPLETE {
            @Override
            void timeOut(VendingMachine vm) {
                long currentTime = Calendar.getInstance().getTimeInMillis();

                if (currentTime - vm.lastTime >= 5000) {
                    handelCurrentState(vm, States.IDLE);
                    vm.currentState.eject(vm);
                }
            }
        };

        void insertCoin(VendingMachine vm, int amount) {
            vm.print.print("Busy..");
        }

        void allOk(VendingMachine vm) {
            vm.print.print("Busy..");
        }

        void eject(VendingMachine vm) {
            vm.print.print("Busy..");
        }

        void timeOut(VendingMachine vm) {
        }

        void selectSlot(VendingMachine vm, int slot) {
            vm.print.print("Busy..");
        }

        void handelCurrentState(VendingMachine vm, States nextStatus) {
            vm.currentState = nextStatus;
            vm.lastTime = Calendar.getInstance().getTimeInMillis();
        }
    }

    public static class Slot {
        private double prodPrice;
        private int prodAmount;

        Slot(double prodPrice, int prodAmount) {
            this.prodPrice = prodPrice;
            this.prodAmount = prodAmount;
        }

        private boolean isProdAvailable() {
            return (prodAmount > 0);
        }

        private double getProdPrice() {
            return prodPrice;
        }

        private void decAmount() {
            --prodAmount;
        }
    }

    private void fillMachine() {
        products.add(new Slot(12, 4));
        products.add(new Slot(3, 2));
        products.add(new Slot(5.5, 9));
    }

    public void insertMoney(int amount) {
        currentState.insertCoin(this, amount);
    }

    public void ejectMoney() {
        currentState.eject(this);
    }

    public void chooseProduct(int productId) {
        currentState.selectSlot(this, productId);
    }

    public interface Iprint {
        void print(String str) ;
    }

    private class TimedTask implements Runnable {
        private VendingMachine vm;

        TimedTask(VendingMachine vm) {
            this.vm = vm;
        }

        @Override
        public void run() {
            while (enable) {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    System.out.println(e);
                }

                currentState.timeOut(vm);
            }
        }
    }
}