package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import interfaces.WAMRoom;
import java.io.IOException;

/**
 * GameServer - Clase principal para el servidor del juego
 */
public class GameServer {

    public static void main(String[] args) {
        // Socket TCP para recibir conexiones de clientes
        ServerSocket listenSocket = null;
        try {
            

            // Levantar un socket TCP en el puerto 8888, y levantar el registro RMI en el servidor.
            int serverPort = 8888;
            LocateRegistry.createRegistry(1099);
            listenSocket = new ServerSocket(serverPort);

            // Levantar instancia de Whack A Mole en RMI
            WAM wam = new WAM(3);
            Registry reg = LocateRegistry.getRegistry("localhost");
            WAMRoom stub = (WAMRoom) UnicastRemoteObject.exportObject(wam, 0);
            reg.rebind("WAM", stub);

            // Eschuchar para nuevas conexiones
            while (true) {
                //System.out.println("Waiting for connection...");
                Socket clientSocket = listenSocket.accept();
                Thread c = new Thread(new Connection(clientSocket));
                c.start();
            }
        } catch (IOException e) {
            System.out.println("Listen: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (listenSocket != null)
                    listenSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * Connection
 */
class Connection implements Runnable {

    private static ArrayList<String> users = new ArrayList<String>();
    private static String address = "";
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket clientSocket;

    public Connection(Socket aClientSocket) {
        // Inicializacion de la conexion
        try {
            clientSocket = aClientSocket;
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    private static synchronized void init() {
        // Setup para inicializar el hilo de ejecucion del juego
        if (address.equals("")) {
            try {
                // System.out.println("GS: Empezando un juego nuevo.");
                // Se definen la direccion IP, puerto y direccion multicast para el juego.
                String hostIP = Inet4Address.getLocalHost().getHostAddress();
                int hostPort = 7777;
                String multiIP = "228.229.230.231";

                // Levantar hilo para control del juego
                Thread gt = new Thread(new GameThread(hostPort, multiIP));
                gt.start();

                // Guardar IPs para distribuir a nuevas conexiones.
                address = hostIP + "," + Integer.toString(hostPort) + "," + multiIP;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static synchronized void reset() {
        // Reiniciar el servidor una vez que termina el juego.
        if (!address.equals("")) {
            
            // Borrar la direccion del juego, la lista de usuarios en el juego y reiniciar el juego WAM en RMI
            users.clear();
            address = "";

        }
    }

    private static synchronized boolean drop(String uid){
        return users.remove(uid);
    }

    private static synchronized boolean add(String uid){
        return users.add(uid);
    }

    @Override
    public void run() {
        // Hilo de ejecucion para atender conexiones
        String uid = null;
        try {
            
            // Obtener el registro RMI
            Registry reg = LocateRegistry.getRegistry("localhost");
            
            // Leer un objeto de la conexion TCP
            TCPComms r;
            

            // Decidir accion dependiendo del tipo de request que se recibio
            int request_type;
            boolean done = false;

            while(!done){
                r = (TCPComms) in.readObject();
                request_type = r.getType();
                switch (request_type) {
                    case TCPComms.LOGIN_REQUEST:
                        // Request para agregar un jugador al juego.
                        uid = (String) r.getPayload();
                        if(!users.contains(uid) && !uid.equals("")){
                            // Si el username esta disponible, agregarlo al arreglo de usernames y al juego
                            // Inicializar el juego.
                            init();
                            // System.out.println("GS: " + uid + " login");
                            add(uid);
                            ((WAMRoom) reg.lookup("WAM")).addUser(uid);
                            // Contestar a la solicitud de login con las direcciones IP del juego
                            TCPComms response = new TCPComms(TCPComms.LOGIN_RESPONSE, address);
                            out.writeObject(response);
                        }else{
                            // Si el username esta tomado, contestar a la solicitud inicial con un fracaso
                            // System.out.println("GS: " + uid + " login failed");
                            TCPComms response = new TCPComms(TCPComms.LOGIN_FAIL, null);
                            out.writeObject(response);
                        }
                        break;
                    case TCPComms.LOGOFF_REQUEST:
                        // Solicitud para liberar un username. Esto no borra al usuario del juego, solo libera el username.
                        uid = (String) r.getPayload();
                        
                        // if(users.remove(uid)) System.out.println("GS: " + uid + " logoff");
                        drop(uid);
                        break;
                    case TCPComms.FINISH_GAME:
                        // Reiniciar el servidor para empezar otro juego
                        reset();
                    case TCPComms.CLOSE_CONNECTION:
                        done = true;
                        break;
                    default:
                        break;
                }
            }
            

        } catch (Exception e) {
            if(uid != null){
                // System.out.println("User " + uid + " connection error: " + e.getMessage());
                drop(uid);
            }else{
                e.printStackTrace();
            }
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}