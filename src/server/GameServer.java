package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * GameServer
 */
public class GameServer {

    public static void main(String[] args) {
        
    }
}

/**
 * TCPRequestThread
 */
class TCPRequestThread implements Runnable{
    
    private static ArrayList<String> users;
    private static String address;
    private static ObjectInputStream in;
    private static ObjectOutputStream out;

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }
}