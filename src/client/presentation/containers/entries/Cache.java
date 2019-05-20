/* 
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.containers.entries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Hounsvad
 */
public class Cache {

    private static Cache instance = null;
    private final Map<String, List<Entry>> cache;

    /**
     *
     * @param cache
     */
    private Cache() {
        cache = new HashMap<>();
    }

    public static Cache getInstance() {
        if (instance == null) {
            instance = new Cache();
        }
        return instance;
    }

    /**
     * Get the value of the cached list
     *
     * @return the value of string
     */
    public Map<String, List<Entry>> getCache() {
        return cache;
    }

}
