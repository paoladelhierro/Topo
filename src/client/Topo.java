
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hca
 */
public class Topo extends Thread{
    private javax.swing.JRadioButton[] radiobuttons = new javax.swing.JRadioButton[9];
    private javax.swing.JDialog alerta;
    private javax.swing.ButtonGroup buttons;
    private boolean keepGoing = true;
    int mole;
    
    public Topo(javax.swing.JRadioButton[] radiobuttons, javax.swing.ButtonGroup buttons, javax.swing.JDialog alerta){
        this.radiobuttons = radiobuttons;
        this.buttons = buttons;
        this.alerta = alerta;
    }
    
    private int changeMole(int mole){
        int m = (int)(Math.random()*9);
        radiobuttons[mole].setText((mole+1) + ".");
        radiobuttons[m].setText("(u.u)");
        return m;
    }

    @Override
    public void run(){
        mole = (int)(Math.random()*9);
        while(keepGoing){
            mole = changeMole(mole);
            try {
                if(mole>-1){
                    this.sleep(500);
                    if(radiobuttons[mole].isSelected()){
                        alerta.setTitle("¡Felicidades! Ganaste");
                        radiobuttons[mole].setSelected(false);
                        radiobuttons[mole].setText((mole+1)+".");
                        keepGoing=  false;   
                    }
                    buttons.clearSelection();
                }else{
                    keepGoing = false;
                    alerta.setTitle("Lo siento, perdiste");
                }
            } catch (InterruptedException ex) {
            Logger.getLogger(Topo.class.getName()).log(Level.SEVERE, null, ex);
            }   
        }
        alerta.setVisible(true);
    }
 
    
}
