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
     * parse query to data-layer and get response
     * @param query matching the format of the agreed-upon in the excelsheet "Stukture.xlsx"
     * @return List<String[]> a list of the returned tuples
     */
    public List<String[]> parseQuery(String[] query);
    
}
