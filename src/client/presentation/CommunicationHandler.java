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
 *
 * @author Oliver
 */
public class CommunicationHandler {
    private static CommunicationHandler communicationHandler;
    private String name;
    private CommunicationInterface communicationInterface;
    
    private CommunicationHandler()
    {
        communicationInterface = new CommunicationInterfaceImpl();
    }
    
    public static CommunicationHandler getInstance()
    {
        if (communicationHandler == null)
        {
            communicationHandler = new CommunicationHandler();
        }
        return communicationHandler;
    }
    
    public List<String[]> sendQuery(String[] query)
    {
        List<String[]> returnVariable = communicationInterface.sendQuery(query);
        name = returnVariable == null ? "error" : returnVariable.get(0)[1];
        return returnVariable;
    }
    
    public String getName()
    {
        return name;
    }
}
