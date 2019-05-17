/* 
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package server.persistence;

import java.util.List;

/**
 *
 * @author Sanitas Solutions
 */
public interface PersistanceInterface {

    /**
     *
     * @param query
     * @return
     */
    public List<String[]> parseQuery(String... query);

}
