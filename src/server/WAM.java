package server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;

import interfaces.WAMRoom;

/**
 * WAM
 */
public class WAM implements WAMRoom{

    private ArrayList<Player> scoreboard;
    private boolean finished;
    private int goal;

    public WAM(int goal){
        this.scoreboard = new ArrayList<Player>();
        this.goal = goal;
        this.finished = false;
    }

    @Override
    public void addUser(String user) throws RemoteException {
        Player p = new Player(user);
        if(!this.scoreboard.contains(p)){
            this.scoreboard.add(p);
        }
    }

    @Override
    public int addPoint(String user) throws RemoteException {
        int score = -1;
        Player p = new Player(user);
        Player p2 = scoreboard.get(0);
        boolean f = p.equals(p2);
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
        Collections.sort(this.scoreboard, new Player.SortByScore());
        return this.scoreboard;
    }

    @Override
    public boolean done() throws RemoteException {
        return this.finished;
    }

    @Override
    public void reset() throws RemoteException {
        this.scoreboard = new ArrayList<Player>();
        this.finished = false;
    }

    
}