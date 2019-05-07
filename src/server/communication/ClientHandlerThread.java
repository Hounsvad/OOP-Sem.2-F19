/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.communication;

import com.frohno.pseudossl.PseudoSSLClient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duffy
 */
public class ClientHandlerThread extends Thread {

    private final Socket clientSocket;
    
    /**
     * Constructs the ClientHandlerSocket with the given Socket
     * @param clientSocket 
     */
    public ClientHandlerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * Overrides the Run method for the thread
     */
    @Override
    public void run() {

        PseudoSSLClient pseudoSSLClient = new PseudoSSLClient(clientSocket);
        pseudoSSLClient.sendObject(DomainHandler.getDomainHandler().parseQuery((String[]) pseudoSSLClient.recieveObject())); 

    }

}
