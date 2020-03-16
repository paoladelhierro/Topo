/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;

/**
 *
 * @author Mike
 */
public class test {
    public static void main(String[] args) {
        ArrayList<Player> l = new ArrayList<>();
        l.add(new Player("p1"));
        l.get(0).addPoint();
        System.out.println(l.indexOf(new Player("p1")));
    }
}
