/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.communication;

import java.util.*;

/**
 *
 * @author Sanitas Solutions
 */
public interface CommunicationInterface {

    /**
     * Sends the query to the server
     *
     * @param query with the instructions for the database
     * @return the the data from the database
     */
    public List<String[]> sendQuery(String[] query);
}
