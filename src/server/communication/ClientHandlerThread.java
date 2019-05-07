/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.communication;

import com.frohno.pseudossl.PseudoSSLClient;
import java.net.Socket;

/**
 *
 * @author duffy
 */
public class ClientHandlerThread extends Thread {

    private final Socket clientSocket;

    /**
     * Constructs the ClientHandlerSocket with the given Socket
     *
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
        DomainHandler domainHandler = new DomainHandler(clientSocket.getInetAddress().getHostAddress());
        pseudoSSLClient.sendObject(domainHandler.parseQuery((String[]) pseudoSSLClient.recieveObject()));

    }

}
