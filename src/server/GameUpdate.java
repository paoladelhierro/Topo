package server;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * GameUpdate
 */
public class GameUpdate implements Serializable{

    private static final long serialVersionUID = 1L;
    private ArrayList<Player> scoreboard;
    private int nextPos;

    public GameUpdate(ArrayList<Player> scoreboard, int nextPos){
        this.scoreboard = scoreboard;
        this.nextPos = nextPos;
    }

    /**
     * @return the nextPos
     */
    public int getNextPos() {
        return nextPos;
    }

    /**
     * @return the scoreboard
     */
    public ArrayList<Player> getScoreboard() {
        return scoreboard;
    }

    @Override
    public String toString() {
        return Integer.toString(nextPos) + "-" + scoreboard.toString();
    }
}