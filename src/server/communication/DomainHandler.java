/* 
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package server.communication;

import java.util.List;
import server.domain.DomainInterface;
import server.domain.DomainInterfaceImpl;

/**
 *
 * @author Sanitas Solutions
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
