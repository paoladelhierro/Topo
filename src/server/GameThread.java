package server;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

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
            WAM game = (WAM) r.lookup("WAM");

            // Esperar 30 segundos para que se unan jugadores
            Thread.sleep(30000);

            // Whack a Mole
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream(5000);
            ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
            byte[] mtcBuf;
            DatagramPacket msgOut;
            
            byte[] UDPbuffer = new byte[1000];
            DatagramPacket msgIn = new DatagramPacket(UDPbuffer, UDPbuffer.length);

            GameUpdate gu;
            String uid;
            int nextPos;
            Random rng = new Random();
            boolean finished = false;
            
            
            while(!finished){
                // Calcular una nueva posicion y enviar el update a todos los jugadores
                nextPos = rng.nextInt(9) + 1;
                gu = new GameUpdate(game.getScore(), nextPos);
                os.flush();
                os.writeObject(gu);
                os.flush();
                mtcBuf = byteStream.toByteArray();
                byteStream.flush();
                msgOut = new DatagramPacket(mtcBuf, mtcBuf.length);
                multiSocket.send(msgOut);
                
                // Esperar una respuesta por 30 segundos
                try {
                    UDPsocket.receive(msgIn);
                    uid = new String(msgIn.getData());
                    game.addPoint(uid);
                    finished = game.done();
                } catch (SocketTimeoutException e) {
                    
                }
            }
            
            // Al terminar, envia -1 como siguiente posicion y los puntajes finales
            nextPos = -1;
            gu = new GameUpdate(game.getScore(), nextPos);
            os.flush();
            os.writeObject(gu);
            os.flush();
            mtcBuf = byteStream.toByteArray();
            byteStream.flush();
            msgOut = new DatagramPacket(mtcBuf, mtcBuf.length);
            multiSocket.send(msgOut);
            
            os.close();

            // Aviza al servidor que el juego termino

        } catch (Exception e) {
            //TODO: handle exception
        }finally{
            if(UDPsocket != null) UDPsocket.close();
            if(multiSocket != null) multiSocket.close();
        }
    }
}