
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
    }


    @Override
    public void run(){
   
        String[] address = ((String) response.getPayload()).split(",");
        String roomIP = address[0];
        int roomPort = Integer.parseInt(address[1]);
        String mtcIP = address[2];
        System.out.println("ya estoy en topo");
        System.out.println(mtcIP);
        try {
            // Sockets para comms con el juego
            DatagramSocket udpSocket = new DatagramSocket();
            InetAddress group = InetAddress.getByName(mtcIP); // destination multicast group 
            MulticastSocket mtcSocket = new MulticastSocket(roomPort + 1);
            mtcSocket.joinGroup(group);
            

            //Entrar en el juego
            byte[] mtcBuffer = new byte[5000];
            DatagramPacket msgIn = new DatagramPacket(mtcBuffer, mtcBuffer.length);


            byte[] udpBuffer = id.getBytes();
            DatagramPacket msgOut = new DatagramPacket(udpBuffer, udpBuffer.length, InetAddress.getByName(roomIP), roomPort);
            
            int mole;
            String scores;
            String[] message;
            
            mtcSocket.receive(msgIn);
            
            message = (new String(msgIn.getData())).trim().split("&", 2);
            mole = Integer.parseInt(message[0]);
            scores = message[1];
            
            scoreboard.setText(scores);
            
            while(mole != -1){
                radiobuttons[mole].setText("(u.u)");
                this.sleep(700);
                
                if(radiobuttons[mole].isSelected())
                    udpSocket.send(msgOut);
                
                mtcSocket.receive(msgIn);
                message = (new String(msgIn.getData())).trim().split("&", 2);
                buttons.clearSelection();
                radiobuttons[mole].setText((mole + 1) +".");
                mole = Integer.parseInt(message[0]);
                scores = message[1];
                scoreboard.setText(scores);
            }
            alerta.setTitle("Acabo el juego");
            alerta.add(scoreboard);
            alerta.setVisible(true);
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
