/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.communication;

import com.frohno.pseudossl.PseudoSSLClient;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

/**
 *
 * @author hende den sidste
 */
public class CommunicationInterfaceImpl implements CommunicationInterface {

    /**
     * Sends the query to the server through a TCP connection
     *
     * @param query the query for the database
     * @return the data from the database
     */
    @Override
    public List<String[]> sendQuery(String[] query) {

        try {
            Socket clientSocket = new Socket("localhost", 1025);
            PseudoSSLClient ps = new PseudoSSLClient(clientSocket);
            ps.sendObject(query);
            return (List<String[]>) ps.recieveObject();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<String[]>() {
            {
                add(new String[]{"Error", "Client-side network error"});
            }
        };
    }
}
