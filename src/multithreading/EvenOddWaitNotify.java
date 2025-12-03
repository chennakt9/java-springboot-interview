package multithreading;

public class EvenOddWaitNotify {
    private final int limit;
    private int current = 1;

    public EvenOddWaitNotify(int limit) {
        this.limit = limit;
    }

    public synchronized void printOdd() {
        while (current <= limit) {
            if (current % 2 == 0) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println(e.toString());
                }
            } else {
                System.out.println(current);
                current++;
                notify();
            }
        }
    }

    public synchronized void printEven() {
        while (current <= limit) {
            if (current % 2 == 1) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println(e.toString());
                }
            } else {
                System.out.println(current);
                current++;
                notify();
            }
        }
    }

    public static void main(String[] args) {
        EvenOddWaitNotify eo = new EvenOddWaitNotify(10);

        Thread odd = new Thread(new Runnable() {
            @Override
            public void run() {
                eo.printOdd();
            }
        });
        Thread even = new Thread(eo::printEven);

        odd.start();
        even.start();
    }
}
