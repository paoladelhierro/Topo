package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Random;

import interfaces.WAMRoom;

/**
 * GameThread
 */
public class GameThread implements Runnable{
    private int hostPort;
    private String multiIP;

    public GameThread(int hostPort, String multiIP){
        this.hostPort = hostPort;
        this.multiIP = multiIP;
    }

    @Override
    public void run() {
        MulticastSocket multiSocket = null;
        DatagramSocket UDPsocket = null;

        try {
            // Crear socket multicast
            InetAddress group = InetAddress.getByName(multiIP);
            multiSocket = new MulticastSocket(hostPort + 1);
            multiSocket.joinGroup(group);

            // Crear socket udp
            UDPsocket = new DatagramSocket(hostPort);
            // Timeout de 30s para cada ronda
            UDPsocket.setSoTimeout(30000);

            // Conectarse a RMI
            Registry r = LocateRegistry.getRegistry("localhost");
            WAMRoom game = (WAMRoom) r.lookup("WAM");

            // Esperar 30 segundos para que se unan jugadores
            System.out.println("Durmiendo 30s");
            Thread.sleep(1000);

            // Whack a Mole
            byte[] mtcBuf;
            DatagramPacket msgOut;
            
            byte[] UDPbuffer = new byte[1000];
            DatagramPacket msgIn = new DatagramPacket(UDPbuffer, UDPbuffer.length);

            GameUpdate gu;
            String uid;
            int nextPos;
            Random rng = new Random();
            boolean finished = false;
            
            System.out.println("Iniciando el juego!");
            while(!finished){
                // Calcular una nueva posicion y enviar el update a todos los jugadores
                nextPos = rng.nextInt(9) + 1;
                gu = new GameUpdate(game.getScore(), nextPos);
                mtcBuf = gu.toString().getBytes();
                msgOut = new DatagramPacket(mtcBuf, mtcBuf.length, group, hostPort + 1);
                multiSocket.send(msgOut);
                
                // Esperar una respuesta por 30 segundos
                try {
                    UDPsocket.receive(msgIn);
                    //uid = new String(Arrays.copyOfRange(msgIn.getData(), 0, msgIn.getLength()));
                    uid = (new String(msgIn.getData())).trim();
                    System.out.println("Punto para: " + uid);
                    game.addPoint(uid);
                    finished = game.done();
                } catch (SocketTimeoutException e) {
                    
                }
            }
            
            // Al terminar, envia -1 como siguiente posicion y los puntajes finales
            nextPos = -1;
            gu = new GameUpdate(game.getScore(), nextPos);
            mtcBuf = gu.toString().getBytes();
            msgOut = new DatagramPacket(mtcBuf, mtcBuf.length, group, hostPort + 1);
            multiSocket.send(msgOut);
            

            // Aviza al servidor que el juego termino
            Socket s = new Socket("localhost", 8888); 
            ObjectOutputStream out = new ObjectOutputStream( s.getOutputStream());
            ObjectInputStream in = new ObjectInputStream( s.getInputStream()); 
            TCPComms request = new TCPComms(TCPComms.FINISH_GAME, null);
            out.writeObject(request);
            
            s.close();

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            
            if(UDPsocket != null) UDPsocket.close();
            if(multiSocket != null) multiSocket.close();
        }
    }
}