/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.domain;

import java.util.List;
import server.persistance.PersistanceInterface;
import server.persistance.PersistanceInterfaceImpl;

/**
 *
 * @author Oliver
 */
public class DomainInterfaceImpl implements DomainInterface {

    private PersistanceInterface persistanceInterface = new PersistanceInterfaceImpl();

    @Override
    public List<String[]> parseQuery(String[] query) {
        switch (query[0]) {
            case "login":
                return persistanceInterface.parseQuery(query);
            case "getMessages":
                return persistanceInterface.parseQuery(query);
            case "getUsers":
                List<String[]> userData = persistanceInterface.parseQuery(new String[]{"login", query[1], query[2]});
                if (!userData.isEmpty()) {
                    return persistanceInterface.parseQuery(new String[]{query[0], userData.get(0)[3]});
                }
        }
        return null;
    }

}
