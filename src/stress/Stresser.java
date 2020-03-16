package stress;

/**
 * Stresser
 */
public class Stresser {

    public static void main(String[] args) {
        Thread t1 = new Thread(new StressThread("p1", "localhost", 8888));
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}