/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.communication;

import java.util.List;
import server.domain.DomainInterface;
import server.domain.DomainInterfaceImpl;

/**
 *
 * @author Oliver
 */
public class DomainHandler {
    /**
     * the DomainHandler
     */
    private static DomainHandler domainHandler;
    
    /**
     * the DomainInterface
     */
    private final DomainInterface domainInterface;
    
    /**
     * Constructs the Domainhandler and initializes the Interface
     */
    private DomainHandler(){
        domainInterface = new DomainInterfaceImpl();
    }
    
    /**
     * Gets the Domainhandler and creates one if not available
     * @return the DomainHandler 
     */
    public static DomainHandler getDomainHandler(){
        if(domainHandler == null){
            domainHandler = new DomainHandler();
        }
        return domainHandler;
    }
    /**
     * Parser query
     * @param query
     * @return the data from the Database as a List
     */
    public synchronized List<String[]> parseQuery(String[] query){
        return domainInterface.parseQuery(query);
    }
}
