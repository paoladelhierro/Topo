package server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;

import interfaces.WAMRoom;

/**
 * WAM - Implementacion de la interfaz WAMRoom. Objeto con informacion del juego Whack A Mole
 */
public class WAM implements WAMRoom{

    // Arreglo de jugadores
    private ArrayList<Player> scoreboard;
    // Bandera de fin de juego
    private boolean finished;
    // Puntaje a alcanzar para ganar
    private int goal;

    public WAM(int goal){
        this.scoreboard = new ArrayList<Player>();
        this.goal = goal;
        this.finished = false;
    }

    @Override
    public void addUser(String user) throws RemoteException {
        // Agrega un jugador con id user al juego
        Player p = new Player(user);
        if(!this.scoreboard.contains(p)){
            this.scoreboard.add(p);
        }
    }

    @Override
    public int playerCount() throws RemoteException {
        return scoreboard.size();
    }

    @Override
    public int addPoint(String user) throws RemoteException {
        // Agrega 1 punto al jugador con id user
        int score = -1;
        Player p = new Player(user);
        
        int i = this.scoreboard.indexOf(p);
        if(i != -1 && !this.finished){
            score = this.scoreboard.get(i).addPoint();
        }
        if(score == goal){
            this.finished = true;
        }
        return score;
    }

    @Override
    public ArrayList<Player> getScore() throws RemoteException {
        // Regresa la lista de jugadores ordenados por puntaje
        Collections.sort(this.scoreboard, new Player.SortByScore());
        return this.scoreboard;
    }

    @Override
    public boolean done() throws RemoteException {
        return this.finished;
    }

    @Override
    public void reset() throws RemoteException {
        // Reinicia el juego con el mismo goal
        this.scoreboard = new ArrayList<Player>();
        this.finished = false;
    }

    
}