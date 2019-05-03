/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import server.persistence.PersistanceInterface;
import server.persistence.PersistanceInterfaceImpl;

/**
 *
 * @author Oliver
 */
public class DomainInterfaceImpl implements DomainInterface {

    private final PersistanceInterface persistenceInterface = new PersistanceInterfaceImpl();
    private String full_name = null;
    private String userId = null;
    private String ip = null;
    private List<String> rights = null;

    public DomainInterfaceImpl(String ip) {
        this.ip = ip;
    }

    /**
     * Parses the query by passing an altered version to the domain layer
     *
     * @param query is the query received from the client
     * @return the expected result based on the parseble quarries
     * <p>
     * In the event that the system does not return data from the database a
     * message is returned in the form of
     * {@literal List<String[]>} with the first index in the first array being
     * a single word descriptor and the second index being a message associated
     * <p>
     * <p>
     * <pre>
     * Descriptors:
     *  * Error - In the event of errors
     *  * Success - In The event of sucess
     * </pre>
     */
    @Override
    public List<String[]> parseQuery(String[] query) {
        try {
            List<String[]> data = persistenceInterface.parseQuery("checkCredentials", query[1], query[2]);

            userId = data.get(0)[1];
            full_name = data.get(0)[0];
            rights = persistenceInterface.parseQuery("getUserRoles", userId).stream().map(t -> t[0]).collect(Collectors.toList());

            if (!data.isEmpty()) {
                if (hasRights(query[0])) {
                    switch (query[0]) {
                        case "login":
                            addActivity(query[0], "", ip, userId);
                            return data;
                        case "getCalendar":
                            if (query[3].equals(userId) || isAssignedPatient(query[3])) {
                                addActivity(query[0], query[3] + ";:;" + query[4] + ";:;" + query[5], ip, userId);
                                return persistenceInterface.parseQuery("getCalendar", query[3], query[4], query[5]);
                            } else {
                                return constructReturn("Error", "Patient not assigned");
                            }
                        case "getEventParticipants":
                            return persistenceInterface.parseQuery("getEventParticipants", query[3]);
                        case "addCalendarEvent":
                            List<String[]> eventId = persistenceInterface.parseQuery("addCalendarEvent", query[3], query[4], query[5], query[6]);
                            for (int i = 7; i < query.length; i++) {
                                persistenceInterface.parseQuery("addEventParticipant", eventId.get(0)[0], query[i]);
                            }
                            return eventId;
                        case "updateCalendarEvent":
                            persistenceInterface.parseQuery("updateCalenderEvent", query[3], query[4], query[5], query[6], query[7]);
                            List<String> participants = persistenceInterface.parseQuery("getEventParticipants", query[3]).stream().map(t -> t[0]).collect(Collectors.toList());
                            for (int i = 8; i < query.length; i++) {
                                if (!participants.contains(query[i])) {
                                    persistenceInterface.parseQuery("addEventParticipant", query[3], query[i]);
                                    participants.remove(participants.indexOf(query[i]));
                                }
                            }
                            participants.forEach((t) -> {
                                persistenceInterface.parseQuery("removeEventParticipant", query[3], t);
                            });
                            return constructReturn("Success", "Event successfully updated");
                        case "removeCalendarEvent":
                            persistenceInterface.parseQuery("removeCalendarEvent", query[3]);
                            return constructReturn("Success", "Event Removed");
                    }
                } else {
                    return constructReturn("Error", "Missing required roles");
                }
            } else {
                return constructReturn("Error", "Credentials invalid");
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            e.printStackTrace();
        }
        return constructReturn("Error", "Unpredicted error");
    }

    private List<String[]> constructReturn(String... input) {
        return new ArrayList<String[]>() {
            {
                add(new String[]{input[0], input[1]});
            }
        };
    }

    private void addActivity(String type, String specifics, String ip, String userId) {
        persistenceInterface.parseQuery(new String[]{type, specifics, ip, userId});
    }

    private boolean hasRights(String action) {
        Map<String, String> actions = new HashMap<String, String>() {
            {
                put("login", "");
                put("getCalendar", "005-001");
                put("getEventParticipants", "005-001");
                put("addCalendarEvent", "005-001");
                put("updateCalendarEvent", "005-001");
                put("removeCalendarEvent", "005-001");
                put("addPatient", "004-002");
                put("getPatients", "004-001");
                put("addUser", "002-002");
                put("userList", "002-001");
                put("alterUserfullName", "002-003");
                put("alterUserUsername", "002-003");
                put("resetUserPassword", "002-005");
                put("alterOwnPassword", "003-001");
                put("setUserRoles", "002-006");
                put("getUserRoles", "002-006");
                put("getRoles", "002-006");
                put("addJournalEntry", "001-001");
                put("getJournal", "d");
                put("getMedicinalJournal", "");
                put("addActivity", "");
                put("getActivity", "");
                put("sendMessage", "");
                put("getMessages", "");
                put("getMenuItems", "");

            }
        };
        return actions.get(action).isEmpty() || rights.contains(actions.get(action));
    }

    private boolean isAssignedPatient(String id) {
        return persistenceInterface.parseQuery("getPatients", userId).stream().map(t -> t[0]).collect(Collectors.toList()).contains(id);
    }
}
