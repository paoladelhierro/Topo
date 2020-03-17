package server;

import java.io.Serializable;

/**
 * TCPComms - Clase para manejar la comunicacion con el servidor TCP
 */
public class TCPComms implements Serializable{
    // Constantes publicas estaticas para identificar el tipo de peticion
    private static final long serialVersionUID = 1L;
    public static final int LOGIN_REQUEST = 1;
    public static final int LOGIN_FAIL = 1000;
    public static final int LOGIN_RESPONSE = 1001;
    public static final int LOGOFF_REQUEST = 2;
    public static final int FINISH_GAME = 3;
    public static final int CLOSE_CONNECTION = 4;
    
    // Entero para identificar el tipo de solicitud
    private int type;
    // Objeto serializable para la solicitud
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

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }
    
    /**
     * @param payload the payload to set
     */
    public void setPayload(Serializable payload) {
        this.payload = payload;
    }
}