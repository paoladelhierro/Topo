/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import server.TCPComms;

/**
 *
 * @author hca
 */
public class Pantallita extends javax.swing.JFrame {
    private TCPComms response;
    private String id;

    /**
     * Creates new form Pantallita
     */
    public Pantallita(TCPComms response, String id) {
        initComponents();
        groupButtons();
        this.response = response;
        this.id = id;

        alerta.setSize(300, 100);
        javax.swing.JLabel empiezaOtra = new javax.swing.JLabel();
        empiezaOtra.setText("Puntaje Final:");
        empiezaOtra.setSize(300, 100);
        alerta.add(empiezaOtra);
        buttonGroup1.clearSelection();
        jLabel1.setText("Jugando...");
        topoThread = new Topo(this);
        topoThread.start();
    }

    private Topo topoThread;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        alerta = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        jRadioButton9 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        score = new javax.swing.JTextArea();

        javax.swing.GroupLayout alertaLayout = new javax.swing.GroupLayout(alerta.getContentPane());
        alerta.getContentPane().setLayout(alertaLayout);
        alertaLayout.setHorizontalGroup(alertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 400, Short.MAX_VALUE));
        alertaLayout.setVerticalGroup(alertaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 300, Short.MAX_VALUE));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 100, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 100, Short.MAX_VALUE));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("1. ");
        jRadioButton1.setName(""); // NOI18N
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("2.");

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("3.");

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setText("4.");

        buttonGroup1.add(jRadioButton5);
        jRadioButton5.setText("5.");

        buttonGroup1.add(jRadioButton6);
        jRadioButton6.setText("6.");

        buttonGroup1.add(jRadioButton7);
        jRadioButton7.setText("7.");

        buttonGroup1.add(jRadioButton8);
        jRadioButton8.setText("8.");
        jRadioButton8.setToolTipText("");

        buttonGroup1.add(jRadioButton9);
        jRadioButton9.setText("9.");

        jLabel2.setText("¡PEGALE AL MONSTRUO!");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addGap(69, 69, 69)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jRadioButton3).addComponent(jRadioButton2)
                                                .addComponent(jRadioButton1))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup().addComponent(jRadioButton6)
                                                        .addGap(18, 18, 18).addComponent(jRadioButton9))
                                                .addGroup(layout.createSequentialGroup().addComponent(jRadioButton4)
                                                        .addGap(18, 18, 18).addComponent(jRadioButton7))
                                                .addGroup(layout.createSequentialGroup().addComponent(jRadioButton5)
                                                        .addGap(18, 18, 18).addComponent(jRadioButton8)))
                                        .addGap(67, 67, 67)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel1)
                                                .addGroup(layout.createSequentialGroup().addGap(14, 14, 14)
                                                        .addComponent(score, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                137, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(116, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
                .createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup().addGap(28, 28, 28).addComponent(score,
                                javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup().addGap(36, 36, 36).addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jRadioButton1).addComponent(jRadioButton4)
                                        .addComponent(jRadioButton7))))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jRadioButton8).addComponent(jRadioButton5).addComponent(jRadioButton2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jRadioButton3).addComponent(jRadioButton6).addComponent(jRadioButton9)
                        .addComponent(jLabel1))
                .addContainerGap(52, Short.MAX_VALUE)));

        jLabel1.getAccessibleContext().setAccessibleName("presionaStart");
        score.getAccessibleContext().setAccessibleName("score");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void groupButtons() {
        radiobuttons[0] = jRadioButton1;
        radiobuttons[1] = jRadioButton2;
        radiobuttons[2] = jRadioButton3;
        radiobuttons[3] = jRadioButton4;
        radiobuttons[4] = jRadioButton5;
        radiobuttons[5] = jRadioButton6;
        radiobuttons[6] = jRadioButton7;
        radiobuttons[7] = jRadioButton8;
        radiobuttons[8] = jRadioButton9;
    }

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jRadioButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Pantallita.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pantallita.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pantallita.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pantallita.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }
        // </editor-fold>

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog alerta;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private JTextArea score;
    // End of variables declaration//GEN-END:variables

  private javax.swing.JRadioButton[] radiobuttons = new javax.swing.JRadioButton[9];

    public TCPComms getResponse() {
        return response;
    }

    public void setResponse(TCPComms response) {
        this.response = response;
    }

    public JDialog getAlerta() {
        return alerta;
    }

    public void setAlerta(JDialog alerta) {
        this.alerta = alerta;
    }

    public ButtonGroup getButtonGroup1() {
        return buttonGroup1;
    }

    public void setButtonGroup1(ButtonGroup buttonGroup1) {
        this.buttonGroup1 = buttonGroup1;
    }

    public JTextArea getScore() {
        return score;
    }

    public void setScore(JTextArea score) {
        this.score = score;
    }

    public JRadioButton[] getRadiobuttons() {
        return radiobuttons;
    }

    public void setRadiobuttons(JRadioButton[] radiobuttons) {
        this.radiobuttons = radiobuttons;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
  
    
  
}

