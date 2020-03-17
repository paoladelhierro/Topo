
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.TCPComms;

/**
 *
 * @author hca
 */
public class Topo extends Thread{
    private javax.swing.JRadioButton[] radiobuttons = new javax.swing.JRadioButton[9];
    private javax.swing.JDialog alerta;
    private javax.swing.ButtonGroup buttons;
    private javax.swing.JLabel scoreboard;
    private Pantallita pantallita;
    private TCPComms response;
    private String id;
    private boolean played;
    private int mole;
    private DatagramSocket udpSocket;
    private MulticastSocket mtcSocket;
    private DatagramPacket msgIn, msgOut;
    private String roomIP;
    private int roomPort;
    
    public Topo(javax.swing.JRadioButton[] radiobuttons, javax.swing.ButtonGroup buttons, javax.swing.JDialog alerta){
        this.radiobuttons = radiobuttons;
        this.buttons = buttons;
        this.alerta = alerta;
    }
    
    public Topo(Pantallita pantallita){
        this.pantallita = pantallita;
        radiobuttons = this.pantallita.getRadiobuttons();
        buttons = this.pantallita.getButtonGroup1();
        alerta = this.pantallita.getAlerta();
        scoreboard = this.pantallita.getScore();
        response = this.pantallita.getResponse();
        id = this.pantallita.getId();
        
        String[] address = ((String) response.getPayload()).split(",");
        roomIP = address[0];
        roomPort = Integer.parseInt(address[1]);
        String mtcIP = address[2];

        try {
            // Sockets para comms con el juego
            udpSocket = new DatagramSocket();
            InetAddress group = InetAddress.getByName(mtcIP); // destination multicast group 
            mtcSocket = new MulticastSocket(roomPort + 1);
            mtcSocket.joinGroup(group);
            
            byte[] mtcBuffer = new byte[5000];
            msgIn = new DatagramPacket(mtcBuffer, mtcBuffer.length);

        } catch (SocketException ex) {
            Logger.getLogger(Topo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Topo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Topo.class.getName()).log(Level.SEVERE, null, ex);
        }
  

    }
    
    public void actionListeners(){
        for(int i=0; i<9; i++){
            radiobuttons[i].putClientProperty("index", i);
            radiobuttons[i].addActionListener((java.awt.event.ActionEvent evt) -> {
               int j = (Integer)((javax.swing.JRadioButton)evt.getSource()).getClientProperty( "index" );
               if(j == mole && !played){
                    try {
                        byte[] udpBuffer = (Integer.toString(mole) + "," + id).getBytes();
                        msgOut = new DatagramPacket(udpBuffer, udpBuffer.length, InetAddress.getByName(roomIP), roomPort);
                        udpSocket.send(msgOut);
                        played = true;
                   } catch (IOException ex) {
                       Logger.getLogger(Topo.class.getName()).log(Level.SEVERE, null, ex);
                   }
               }
            });
        }
    }
    
    


    @Override
    public void run(){
        actionListeners();
        try {
            //Entrar en el juego
            System.out.println("estoy en el try catch");
            String scores;
            String[] message;
            
            mtcSocket.receive(msgIn);
            
            message = (new String(msgIn.getData())).trim().split("&", 2);
            mole = Integer.parseInt(message[0]);
            scores = message[1];
            
            scoreboard.setText(scores);
            
            while(mole != -1){
                System.out.println(mole);
                radiobuttons[mole].setText("(u.u)");
                Thread.sleep(2000);
                mtcSocket.receive(msgIn);
                message = (new String(msgIn.getData())).trim().split("&", 2);
                buttons.clearSelection();
                radiobuttons[mole].setText((mole + 1) +".");
                mole = Integer.parseInt(message[0]);
                scores = message[1];
                scoreboard.setText(scores);
                played =false;
            }  
            alerta.setTitle("Acabo el juego");
            alerta.add(scoreboard);
            alerta.setVisible(true);
            pantallita.dispose();
        } catch (SocketException ex) {
            Logger.getLogger(Topo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Topo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Topo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Topo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
 
    
}
