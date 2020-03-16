/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import interfaces.WAMRoom;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Mike
 */
public class test {
    public static void main(String[] args) throws RemoteException{
        WAMRoom w = new WAM(5);
        w.addUser("p1");
        w.addPoint("p1");
    }
}
