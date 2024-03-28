import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Крок: ");
        int step = scanner.nextInt();
        System.out.print("Кiлькiсть потокiв: ");
        int numThreads = scanner.nextInt();
        int permissionInterval = 10000;

        SummingThread[] threads = new SummingThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new SummingThread(i, step);
            threads[i].start();
        }

        try {
            Thread.sleep(permissionInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < numThreads; i++) {
            threads[i].setRunning(false);
        }
    }

    private static class SummingThread extends Thread {
        private final int id;
        private final int step;
        private volatile boolean running = true;

        public SummingThread(int id, int step) {
            this.id = id;
            this.step = step;
        }

        public void run() {
            double sum = 0;
            double count = 0;
            double current = 0;

            while (running) {
                sum += current;
                count++;
                current += step;
            }

            System.out.printf("Потiк %d: сума=%f кiлькiсть доданкiв=%f2\n", id + 1, sum, count);
        }

        public void setRunning(boolean running) {
            this.running = running;
        }
    }
}
