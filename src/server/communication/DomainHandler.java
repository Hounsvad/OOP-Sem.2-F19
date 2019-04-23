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
    private static DomainHandler domainHandler;
    private final DomainInterface domainInterface;
    
    private DomainHandler(){
        domainInterface = new DomainInterfaceImpl();
    }
    
    public static DomainHandler getDomainHandler(){
        if(domainHandler == null){
            domainHandler = new DomainHandler();
        }
        return domainHandler;
    }
    
    public synchronized List<String[]> parseQuery(String[] query){
        return domainInterface.parseQuery(query);
    }
}
