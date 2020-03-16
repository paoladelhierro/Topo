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
 * GameServer
 */
public class GameServer {

    public static void main(String[] args) {
        ServerSocket listenSocket = null;
        try{
            int serverPort = 8888;
            LocateRegistry.createRegistry(1099);
            listenSocket = new ServerSocket(serverPort);
            while(true) {
                System.out.println("Waiting for messages..."); 
                Socket clientSocket = listenSocket.accept();
                Thread c = new Thread(new Connection(clientSocket));
                c.start();
            }
        } catch(IOException e) {
            System.out.println("Listen :"+ e.getMessage());
        }finally{
            try {
                if(listenSocket != null) listenSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * Connection
 */
class Connection implements Runnable{
    
    private static ArrayList<String> users = new ArrayList<String>();
    private static String address = "";
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket clientSocket;

    public Connection (Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch(IOException e)  {System.out.println("Connection:" + e.getMessage());}
    }

    @Override
    public void run() {
        try {
            if(address.equals("")){
                // Setup en la primera connexion al servidor
                String hostIP = Inet4Address.getLocalHost().getHostAddress();
                int hostPort = 7777;
                String multiIP = "228.229.230.231";
                
                // Levantar instancia de WAM en RMI
                WAM wam = new WAM(5);
                Registry reg = LocateRegistry.getRegistry("localhost");
                WAMRoom stub = (WAMRoom) UnicastRemoteObject.exportObject(wam, 0);
                reg.rebind("WAM", stub);

                // Levantar hilo para control del juego
                Thread gt = new Thread(new GameThread(hostPort, multiIP));
                gt.start();

                // Guardar IPs
                address =  hostIP + "," + Integer.toString(hostPort) + "," + multiIP;
            }

            Registry reg = LocateRegistry.getRegistry("localhost");
            TCPComms r = (TCPComms) in.readObject();
            String uid;
            switch (r.getType()) {
                case TCPComms.LOGIN_REQUEST:
                    uid = (String) r.getPayload();
                    if(!users.contains(uid)){
                        users.add(uid);
                        ((WAMRoom) reg.lookup("WAM")).addUser(uid);
                        TCPComms response = new TCPComms(TCPComms.LOGIN_RESPONSE, address);
                        out.writeObject(response);
                    }else{
                        TCPComms response = new TCPComms(TCPComms.LOGIN_FAIL, null);
                        out.writeObject(response);
                    }
                    break;
                case TCPComms.LOGOFF_REQUEST:
                    uid = (String) r.getPayload();
                    users.remove(uid);
                    break;
                case TCPComms.FINISH_GAME:
                    address = "";
                    users = new ArrayList<String>();
                    ((WAMRoom) reg.lookup("WAM")).reset();
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}