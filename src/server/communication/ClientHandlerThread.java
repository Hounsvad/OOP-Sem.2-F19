/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.communication;

import com.frohno.pseudossl.PseudoSSLServer;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sanitas Solutions
 */
public class ClientHandlerThread extends Thread {

    private final Socket clientSocket;

    /**
     * Constructs a handler for the action requested by the user
     *
     * @param clientSocket the socket returned by the server upon accepting a
     *                     client
     */
    public ClientHandlerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * Client request handling
     */
    @Override
    public void run() {
        PseudoSSLServer pseudoSSLServer = new PseudoSSLServer(clientSocket);
        DomainHandler domainHandler = new DomainHandler(clientSocket.getInetAddress().getHostAddress());
        String[] query = (String[]) pseudoSSLServer.recieveObject();
        List<String[]> returnValue = domainHandler.parseQuery(query);
        pseudoSSLServer.sendObject(new ArrayList<>(returnValue));
    }

}
