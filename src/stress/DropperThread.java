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
public class DropperThread implements Runnable {
    int drops;
    private String id, serverIP;
    private int serverPort;

    public DropperThread(int drops, String id, String serverIP, int serverPort) {
        this.drops = drops;
        this.id = id;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
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
            
            for (int i = 0; i < drops; i++) {
                request = new TCPComms(TCPComms.LOGIN_REQUEST, id);
                out.writeObject(request);
                in.readObject();
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                request = new TCPComms(TCPComms.LOGOFF_REQUEST, id);
                out.writeObject(request);
            }

            request = new TCPComms(TCPComms.CLOSE_CONNECTION, null);
            out.writeObject(request);
            
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