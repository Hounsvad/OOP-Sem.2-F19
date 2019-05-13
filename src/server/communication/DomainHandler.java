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
     *
     * @param ip
     */
    public DomainHandler(String ip) {
        domainInterface = new DomainInterfaceImpl(ip);
    }

    /**
     * Parser query
     *
     * @param query
     * @return the data from the Database as a List
     */
    public List<String[]> parseQuery(String[] query) {
        return domainInterface.parseQuery(query);
    }
}
