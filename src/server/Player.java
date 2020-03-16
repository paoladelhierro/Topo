package server;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Player
 */
public class Player implements Serializable, Comparable<Player>{

    private static final long serialVersionUID = 1L;
    private String uid;
    private int score;

    public Player(String uid) {
        this.uid = uid;
        this.score = 0;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    public int addPoint(){
        return ++this.score;
    }

    @Override
    public int compareTo(Player o) {
        return this.uid.compareTo(o.uid);
    }

    @Override
    public boolean equals(Object obj) {
        return this.uid.equals(((Player) obj).uid);
    }

    public static class SortByScore implements Comparator<Player>{
        @Override
        public int compare(Player o1, Player o2) {
            return o1.score - o2.score;
        }
    }

    @Override
    public String toString() {
        return String.format("%s\t%d", uid, score);
    }


}