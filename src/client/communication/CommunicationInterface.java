/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.communication;

import java.util.*;

/**
 *
 * @author Oliver
 */
public interface CommunicationInterface {
    /**
     * Sends the query 
     * @param query
     * @return the the data from the database
     */
    public List<String[]> sendQuery(String[] query);
}
