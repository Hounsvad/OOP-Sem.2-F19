/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation;

import client.communication.CommunicationInterface;
import client.communication.CommunicationInterfaceImpl;
import java.util.List;

/**
 * A Singleton class
 * @author Oliver
 */
public class CommunicationHandler {

    /**
     * The CommunicationHandler
     */
    private static CommunicationHandler communicationHandler;

    /**
     * ???
     */
    private String name;

    /**
     * The CommunicationInterface
     */
    private CommunicationInterface communicationInterface;
    
    /**
     * Constructs the CommunicationHandler
     */
    private CommunicationHandler() {
        communicationInterface = new CommunicationInterfaceImpl();
    }

    /**
     * Creates an instance of the CommunicationHandler if none exists
     * @return The instanceof the CommunicationHandler
     */
    public static CommunicationHandler getInstance() {
        if (communicationHandler == null) {
            communicationHandler = new CommunicationHandler();
        }
        return communicationHandler;
    }

    /**
     * Sends the query to the database
     * @param query The query for the database
     * @return The data from the database
     */
    public List<String[]> sendQuery(String[] query) {
        List<String[]> returnVariable = communicationInterface.sendQuery(query);
        name = returnVariable == null ? "error" : returnVariable.get(0)[1];
        return returnVariable;
    }

    /**
     * @return The name ???
     */
    public String getName() {
        return name;
    }
}
