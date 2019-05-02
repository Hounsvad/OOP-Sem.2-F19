/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.domain;

import com.sun.javaws.exceptions.InvalidArgumentException;
import java.util.List;
import server.persistance.PersistanceInterface;
import server.persistance.PersistanceInterfaceImpl;

/**
 *
 * @author Oliver
 */
public class DomainInterfaceImpl implements DomainInterface {

    private final PersistanceInterface persistanceInterface = new PersistanceInterfaceImpl();
    private String full_name = null;
    private String userId = null;
    private String ip = null;

    public DomainInterfaceImpl(String ip) {
        this.ip = ip;
    }

    @Override
    public List<String[]> parseQuery(String[] query) {
        try {
            List<String[]> data = persistanceInterface.parseQuery(new String[]{"checkCredentials", query[1], query[2]});
            if (!data.isEmpty()) {
                switch (query[0]) {
                    case "login":
                        addActivity(query[0], "", ip, userId);
                        return data;
                    case "getCalendar":
                        addActivity(query[0], query[3] + ";:;" + query[4] + ";:;" + query[5], ip, userId);
                        return persistanceInterface.parseQuery(new String[]{query[3], query[4], query[5]});
                    case "getEventParticipants":
                        return persistanceInterface.parseQuery(new String[]{query[3]});
                    case "addCalendarEvent":
                        String eventId = persistanceInterface.parseQuery(new String[]{query[3]});
                    case "getMessages":
                        return persistanceInterface.parseQuery(query);
                    case "getUsers":
                        List<String[]> userData = persistanceInterface.parseQuery(new String[]{"login", query[1], query[2]});
                        if (!userData.isEmpty()) {
                            return persistanceInterface.parseQuery(new String[]{query[0], userData.get(0)[3]});
                        }
                        break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException | InvalidArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addActivity(String type, String specifics, String ip, String userId) {
        persistanceInterface.parseQuery(new String[]{type, specifics, ip, userId});
    }
}
