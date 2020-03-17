package stress;

/**
 * LoggerStresser
 */
public class LoggerStresser {

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
        String id;
        int drops = 20;

        for (int i = 0; i < n; i++) {
            
            id = "p" + Integer.toString(i);
            t = new Thread(new LoggerThread(drops, id, TCPHost, TCPPort, n));
            t.start();
        }
        
    }
}