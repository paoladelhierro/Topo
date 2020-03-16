package stress;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import server.GameUpdate;
import server.TCPComms;

/**
 * stressThread
 */
public class stressThread implements Runnable{

    private String id, serverIP;
    private int serverPort;

    public stressThread(String id, String serverIP, int serverPort){
        this.id = id;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        
        Socket s = null;
        MulticastSocket mtcSocket = null;
        DatagramSocket udpSocket = null;
        try {
            s = new Socket(serverIP, serverPort); 
            ObjectOutputStream out = new ObjectOutputStream( s.getOutputStream());
            ObjectInputStream in = new ObjectInputStream( s.getInputStream()); 
            
            // Realizar login
            TCPComms request = new TCPComms(TCPComms.LOGIN_REQUEST, id);
            out.writeObject(request); 
            TCPComms response = (TCPComms) in.readObject();
            while(response.getType() == TCPComms.LOGIN_FAIL){
                //Cambiar username y volver a intentar

            }

            String[] address = ((String) response.getPayload()).split(",");
            String roomIP = address[0];
            int roomPort = Integer.parseInt(address[1]);
            String mtcIP = address[2];

            // Sockets para comms con el juego
            udpSocket = new DatagramSocket();

            InetAddress group = InetAddress.getByName(mtcIP); // destination multicast group 
            mtcSocket = new MulticastSocket(roomPort + 1);
       	    mtcSocket.joinGroup(group);

            //Entrar en el juego
            byte[] mtcBuffer = new byte[5000];
            DatagramPacket msgIn = new DatagramPacket(mtcBuffer, mtcBuffer.length);
            ByteArrayInputStream byteStream = new ByteArrayInputStream(mtcBuffer);
            ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));

            byte[] udpBuffer = id.getBytes();
            DatagramPacket msgOut = new DatagramPacket(udpBuffer, udpBuffer.length, InetAddress.getByName(roomIP), roomPort);

            GameUpdate gu;
            long tic, toc;
            double sum, sum2;

            tic = System.currentTimeMillis();
            mtcSocket.receive(msgIn);
            toc = System.currentTimeMillis() - tic;
            sum = toc;
            sum2 = toc*toc;
            tic = System.currentTimeMillis();

            gu = (GameUpdate) is.readObject();
            
            while(gu.getNextPos() != -1){
                // Espera hasta un degundo antes de enviar tu respuesta
                udpSocket.send(msgOut);

                mtcSocket.receive(msgIn);
                toc = System.currentTimeMillis() - tic;
                sum = toc;
                sum2 = toc*toc;
                tic = System.currentTimeMillis();

                gu = (GameUpdate) is.readObject();
            };
            
            System.out.println(String.format("%g,%g", sum, sum2));
            is.close();

       	} catch (UnknownHostException e) {
            System.out.println("Sock:"+e.getMessage()); 
        } catch (EOFException e) {
            System.out.println("EOF:"+e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:"+e.getMessage());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            if(s!=null) 
                try {
                    s.close();
                } catch (IOException e){System.out.println("close:"+e.getMessage());}
            }
            if(udpSocket != null) udpSocket.close();
            if(mtcSocket != null) mtcSocket.close();
    }
}