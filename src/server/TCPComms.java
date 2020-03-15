package server;

import java.io.Serializable;

/**
 * TCPComms
 */
public class TCPComms implements Serializable{

    private static final long serialVersionUID = 1L;
    public static final int LOGIN_REQUEST = 1;
    public static final int LOGIN_FAIL = 1000;
    public static final int LOGIN_RESPONSE = 1001;
    public static final int LOGOFF_REQUEST = 2;
    public static final int FINISH_GAME = 3;
    
    private int type;
    private Serializable payload;

    public TCPComms(int type, Serializable payload){
        this.type = type;
        this.payload = payload;
    }

    /**
     * @return the payload
     */
    public Serializable getPayload() {
        return payload;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }
}