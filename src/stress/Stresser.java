package stress;

/**
 * Stresser
 */
public class Stresser {

    public static void main(String[] args) {
        int n;
        if(args.length >= 1){
            n = Integer.parseInt(args[0]);
        }else{
            n = 5;
        }
        String TCPHost = "localhost";
        int TCPPort = 8888;
        
        Thread t;
        String id, id2;
        int drops = 25;

        for (int i = 0; i < n; i++) {
            // if(i%10 == 0){
            //     // Por cada 10 hilos, levanta un hilo 'dropper', que estresa el login
            //     id2 = "d" + Integer.toString(i);
            //     t = new Thread(new DropperThread(drops, id2, TCPHost, TCPPort));
            //     t.start();
            // }
            id = "p" + Integer.toString(i);
            t = new Thread(new StressThread(id, TCPHost, TCPPort, n));
            t.start();
        }
        
    }
}