package stress;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import server.TCPComms;

/**
 * DropperThread - Thread para estresar servicio de login
 */
public class LoggerThread implements Runnable {
    int drops;
    private String id, serverIP;
    private int serverPort, totalClients;

    public LoggerThread(int drops, String id, String serverIP, int serverPort, int totalClients) {
        this.drops = drops;
        this.id = id;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.totalClients = totalClients;
    }

    @Override
    public void run() {

        Socket s = null;
        try {
            s = new Socket(serverIP, serverPort);
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());

            // Realizar login
            TCPComms request;
            long tic, toc;
            double sum, sum2;
            sum = 0;
            sum2 = 0;

            for (int i = 0; i < drops; i++) {
                tic = System.currentTimeMillis();
                request = new TCPComms(TCPComms.LOGIN_REQUEST, id);
                out.writeObject(request);
                in.readObject();
                request = new TCPComms(TCPComms.LOGOFF_REQUEST, id);
                out.writeObject(request);
                toc = System.currentTimeMillis() - tic;
                sum += toc;
                sum2 += toc * toc;
            }

            request = new TCPComms(TCPComms.CLOSE_CONNECTION, null);
            out.writeObject(request);

            double avg = (sum)/drops;
            double std = Math.sqrt((sum2)/drops - avg*avg);
            System.out.println(String.format("%d,%g,%g", totalClients, avg, std));
            
       	} catch (UnknownHostException e) {
            System.out.println("Sock:"+e.getMessage()); 
        } catch (IOException e) {
            System.out.println("IO: "+e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            if(s!=null) 
                try {
                    s.close();
                } catch (IOException e){System.out.println("close:"+e.getMessage());}
            }
    }
}