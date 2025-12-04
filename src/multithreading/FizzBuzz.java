package multithreading;

class FizzBuzzHelper {
    private final int limit = 10;
    private int counter = 1;

    public void fizz() throws InterruptedException {
        while (true) {
            synchronized (this) {
                while (!(counter % 3 == 0 && counter % 5 != 0)) {
                    wait();
                }

                if (counter > limit) {
                    break;
                }

                System.out.println(counter + " Fizz");
                counter++;
                notifyAll();
            }
        }
    }

    public synchronized void buzz() throws InterruptedException {
        while (true) {
            synchronized (this) {
                while (!(counter % 3 != 0 && counter % 5 == 0)) {
                    wait();
                }

                if (counter > limit) {
                    break;
                }

                System.out.println(counter + " Buzz");
                counter++;
                notifyAll();
            }
        }
    }

    public synchronized void fizzBuzz() throws InterruptedException {
        while (true) {
            synchronized (this) {
                while (!(counter % 3 == 0 && counter % 5 == 0)) {
                    wait();
                }

                if (counter > limit) {
                    break;
                }

                System.out.println(counter + " FizzBuzz");
                counter++;
                notifyAll();
            }
        }
    }

    public synchronized void number() throws InterruptedException {
        while (true) {
            synchronized (this) {
                while (!(counter % 3 != 0 && counter % 5 != 0)) {
                    wait();
                }

                if (counter > limit) {
                    break;
                }

                System.out.println(counter + " number");
                counter++;
                notifyAll();
            }
        }
    }
}

public class FizzBuzz {
    public static void main(String[] args) {

        FizzBuzzHelper fizzBuzzHelper = new FizzBuzzHelper();
        Thread fizz = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fizzBuzzHelper.fizz();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread buzz = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fizzBuzzHelper.buzz();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread fizzBuzz = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fizzBuzzHelper.fizzBuzz();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread number = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fizzBuzzHelper.number();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // start threads
        fizz.start();
        buzz.start();
        fizzBuzz.start();
        number.start();
    }
}
