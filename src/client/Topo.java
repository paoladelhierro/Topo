
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
    private boolean keepGoing = true;
    int mole;
    
    public Topo(javax.swing.JRadioButton[] radiobuttons){
        this.radiobuttons = radiobuttons;
        
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
                this.sleep(500);
                if(radiobuttons[mole].isSelected()){
                    System.out.println("Gano");
                    radiobuttons[mole].setSelected(false);
                    radiobuttons[mole].setText((mole+1)+".");
                    keepGoing=  false;   
                    break;
                }
                
            } catch (InterruptedException ex) {
            Logger.getLogger(Topo.class.getName()).log(Level.SEVERE, null, ex);
            }   
        } 
    }
 
    
}
