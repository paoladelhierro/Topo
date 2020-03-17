package server;

import java.io.IOException;
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
import java.util.Random;

import interfaces.WAMRoom;

/**
 * GameThread - Hilo de ejecucion para el juego Whack A Mole
 */
public class GameThread implements Runnable {
    private int hostPort;
    private String multiIP;

    public GameThread(int hostPort, String multiIP) {
        this.hostPort = hostPort;
        this.multiIP = multiIP;
    }

    @Override
    public void run() {
        
        // Socket para enviar mensajes multicast a todos los jugadores
        MulticastSocket multiSocket = null;
        // Socket para recibir mensajes UDP de los jugadores
        DatagramSocket UDPsocket = null;
        // Socket TCP para la comunicacion con el servidor principal
        Socket s = null;

        try {
            // Crear socket multicast
            InetAddress group = InetAddress.getByName(multiIP);
            multiSocket = new MulticastSocket(hostPort + 1);
            multiSocket.joinGroup(group);

            // Crear socket udp
            UDPsocket = new DatagramSocket(hostPort);
            // Timeout de 10s para cada ronda
            UDPsocket.setSoTimeout(10000);

            // Conectarse a RMI y conseguir la instancia del juego
            Registry r = LocateRegistry.getRegistry("localhost");
            WAMRoom game = (WAMRoom) r.lookup("WAM");

            // Esperar 15 segundos para que se unan jugadores
            // System.out.println("GT: Durmiendo 30s para esperar a los jugadores.");
            Thread.sleep(15000);
            // Esperar a que haya al menos 1 jugador
            int playerCount = game.playerCount();
            while(playerCount == 0){
                playerCount = game.playerCount();
            }

            // Inicializar buffer para los mensajes multicast de salida
            byte[] mtcBuf;
            DatagramPacket msgOut;

            // Inicializar buffer para los mensajes UDP de entrada
            byte[] UDPbuffer = new byte[1000];
            DatagramPacket msgIn = new DatagramPacket(UDPbuffer, UDPbuffer.length);

            // gu contiene las actualizaciones del juego: la siguiente posicion para el topo
            // y el puntaje de cada jugador
            String gu;
            int nextPos, rand;
            nextPos = -1;
            rand = -1;

            // uid guarda el primer usuario en pegarle al topo
            String[] message;
            int hitPos;
            String uid = null;
            // Generador de numeros aleatorios para determinar la siguiente posicion
            Random rng = new Random();
            // Bandera de fin de juego
            boolean finished = false;

            // System.out.println("GT: Iniciando el juego!");
            while (!finished) {
                // Calcular una nueva posicion (0-8) sin repetir y crear un GameUpdate
                while(rand == nextPos){
                    rand = rng.nextInt(9);
                }
                nextPos = rand;
                gu = Integer.toString(nextPos) + "&" + game.getScore();

                // Enviar un mensaje multicast con la actualizacion del juego
                mtcBuf = gu.getBytes();
                msgOut = new DatagramPacket(mtcBuf, mtcBuf.length, group, hostPort + 1);
                multiSocket.send(msgOut);

                // Esperar una respuesta por 30 segundos
                try {
                    
                    do{
                        // Leer el primer datagrama UDP en llegar
                        UDPsocket.receive(msgIn);

                        // Identificar que usuario consiguio el punto
                        message = (new String(msgIn.getData())).trim().split(",", 2);
                        hitPos = Integer.parseInt(message[0]);
                        uid = message[1];
                    }while(hitPos != nextPos);
                    

                    // System.out.println("GT: Punto para: " + uid);

                    // Agregar un punto al usuario ganador
                    game.addPoint(uid);

                    // Revisar si ha terminado el juego
                    finished = game.done();

                    // Esperar 500ms para empezar la siguiente ronda
                    Thread.sleep(500);
                } catch (SocketTimeoutException e) {
                    // Si se llega a un timeout sin conseguir respuesta, nadie consigue el punto y
                    // se va a la siguiente ronda
                }
            }
            // Imprimir el usuario ganador en consola
            // if (uid != null) System.out.println("GT: Gano: " + uid);

            // Al terminar, envia -1 como siguiente posicion y los puntajes finales
            nextPos = -1;
            gu = Integer.toString(nextPos) + "&" + game.getScore();;
            mtcBuf = gu.getBytes();
            msgOut = new DatagramPacket(mtcBuf, mtcBuf.length, group, hostPort + 1);
            multiSocket.send(msgOut);

            game.reset();

            // Aviza al servidor TCP que el juego termino para poder empezar uno nuevo
            s = new Socket("localhost", 8888);
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());
            
            TCPComms request = new TCPComms(TCPComms.FINISH_GAME, null);
            out.writeObject(request);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (s != null)
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if(UDPsocket != null) UDPsocket.close();
            if(multiSocket != null) multiSocket.close();
        }
    }
}