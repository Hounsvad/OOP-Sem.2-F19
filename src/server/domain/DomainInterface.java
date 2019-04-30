/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.domain;

import java.util.List;

/**
 *
 * @author prikn
 */
public interface DomainInterface {
    
    /**
     * Parse the query by the command in the query
     * @param query The query for the database
     * @return The data from the database
     */
    public List<String[]> parseQuery(String[] query);
    
}
