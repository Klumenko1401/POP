import java.util.concurrent.Semaphore;

public class Main {
    static Semaphore[] forks = new Semaphore[5];
    static Philosopher[] philosophers = new Philosopher[5];

    static class Philosopher extends Thread {
        int id;
        boolean locked = false;

        Philosopher(int id) {
            this.id = id;
        }

        void lockForks() {
            while (true) {
                if (id % 2 == 0) {
                    if (forks[id].tryAcquire() && forks[(id + 1) % 5].tryAcquire()) {
                        break;
                    } else {
                        forks[id].release();
                    }
                } else {
                    if (forks[(id + 1) % 5].tryAcquire() && forks[id].tryAcquire()) {
                        break;
                    } else {
                        forks[(id + 1) % 5].release();
                    }
                }
            }
            System.out.println("Philosopher " + id + " took left fork");
            System.out.println("Philosopher " + id + " took right fork");
        }


        void unlockForks() {
            forks[id].release();
            forks[(id + 1) % 5].release();
            System.out.println("Philosopher " + id + " put left fork");
            System.out.println("Philosopher " + id + " put right fork");
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                System.out.println("Philosopher " + id + " is thinking");

                lockForks();

                System.out.println("Philosopher " + id + " is eating");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                unlockForks();
            }
        }

    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            forks[i] = new Semaphore(1);
        }

        for (int i = 0; i < 5; i++) {
            philosophers[i] = new Philosopher(i);
        }

        for (Philosopher philosopher : philosophers) {
            philosopher.start();
        }

        for (Philosopher philosopher : philosophers) {
            try {
                philosopher.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}