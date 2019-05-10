/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.domain;

import com.google.common.hash.Hashing;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import server.persistence.PersistanceInterface;
import server.persistence.PersistanceInterfaceImpl;

/**
 *
 * @author Frederik
 */
public class DomainInterfaceImpl implements DomainInterface {

    private final PersistanceInterface persistenceInterface = new PersistanceInterfaceImpl();
    private String userId = null;
    private String ip = null;
    private List<String> rights = null;
    private final Map<String, String> smtpConfiguration = new HashMap<>();
    private static final String PASS_CHARS = "ABCDEFGHIJKLMNOPQRSTUVXYZabcdefghijklmnopqrstuvxyz,.-1234567890+?!@#&/";
    private final Map<String, String> actions = new HashMap<String, String>() {
        {
            put("direct", "");
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
            put("getUserActivity", "002-007");

        }
    };

    public DomainInterfaceImpl(String ip) {
        this.ip = ip;
        new Scanner(getClass().getResourceAsStream("/server/recources/SMTPConfiguration.config")).useDelimiter("\r\n").forEachRemaining((s) -> smtpConfiguration.put(s.split(" := ")[0], s.split(" := ")[1]));
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

            if (!data.isEmpty()) {
                userId = data.get(0)[1];
                rights = persistenceInterface.parseQuery("getUserRoles", userId).stream().map(t -> t[0]).collect(Collectors.toList());

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
                        case "addPatient":
                            persistenceInterface.parseQuery("addPatient", query[3], persistenceInterface.parseQuery("getUserDepartment", userId).get(0)[0]);
                            return constructReturn("Success", "Patienty");
                        case "getPatients":
                            return persistenceInterface.parseQuery("getPatients", userId);
                        case "addUser":
                            String password = generatePassword();
                            List<String[]> result = persistenceInterface.parseQuery("addUser", query[3], query[4], Hashing.sha256().hashString(password, Charset.forName("UTF-8")).toString(), query[5]);
                            sendPassword(query[4], result.get(0)[0], password);
                            return result;
                        case "userList":
                            List<String[]> s = persistenceInterface.parseQuery("getUserDepartment", userId);
                            return persistenceInterface.parseQuery("getUsers", s.get(0)[0]);
                        case "alterUserfullName":
                            persistenceInterface.parseQuery("alterUserFullName", query[3], query[4]);
                            return constructReturn("Success", "Full name of user altered");
                        case "resetUserPassword":
                            String newPassword = generatePassword();
                            persistenceInterface.parseQuery("setUserPassword", query[3], Hashing.sha256().hashString(newPassword, Charset.forName("UTF-8")).toString());
                            sendPassword(query[4], query[3], newPassword);
                            return constructReturn("Success", "Password updated");
                        case "alterOwnPassword":
                            persistenceInterface.parseQuery("setUserPassword", query[4], query[3]);
                            return constructReturn("Success", "Password succesfully updated");
                        case "setUserRoles":
                            List<String> roles = persistenceInterface.parseQuery("getUserRoles", query[3]).stream().map(t -> t[0]).collect(Collectors.toList());
                            for (int i = 4; i < query.length; i++) {
                                if (!roles.contains(query[i])) {
                                    persistenceInterface.parseQuery("addUserRole", query[3], query[i]);
                                    roles.remove(roles.indexOf(query[i]));
                                }
                            }
                            roles.forEach((t) -> {
                                persistenceInterface.parseQuery("removeUserRole", query[3], t);
                            });
                            return constructReturn("Success", "roles successfully updated");
                        case "getUserRoles":
                            return persistenceInterface.parseQuery("getUserRoles", query[3]);
                        case "getRoles":
                            return persistenceInterface.parseQuery("getRoles");
                        case "addJournalEntry":
                            persistenceInterface.parseQuery("addJournalEntry", query[3], query[4], Long.toString(System.currentTimeMillis()), query[6], userId, query[5]);
                            return constructReturn("Success", "Entry added");
                        case "getJournal":
                            return persistenceInterface.parseQuery("getJournal", query[3]);
                        case "getMedicinalJournal":
                            return persistenceInterface.parseQuery("getMedicalJournal", query[3]);
                        case "getActivity":
                            return persistenceInterface.parseQuery("getActivity", userId);
                        case "getUserActivity":
                            return persistenceInterface.parseQuery("getActivity", query[3]);
                        case "sendMessage":
                            return persistenceInterface.parseQuery("sendMessage", userId, query[3], query[4], query[5]);
                        case "getMessages":
                            return persistenceInterface.parseQuery("getMessages", userId);
                        case "getMenuItems":
                            return persistenceInterface.parseQuery("getMenuItems", userId);
                        case "getDepartments":
                            return persistenceInterface.parseQuery("getDepartments");
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
        persistenceInterface.parseQuery("addActivity", type, specifics, ip, userId);
    }

    private boolean hasRights(String action) {
        return actions.get(action).isEmpty() || rights.contains(actions.get(action));
    }

    private boolean isAssignedPatient(String id) {
        return persistenceInterface.parseQuery("getPatients", userId).stream().map(t -> t[0]).collect(Collectors.toList()).contains(id);
    }

    private void sendPassword(String username, String domain, String password) {
        new EmailHandler(smtpConfiguration.get("host"), smtpConfiguration.get("port"), smtpConfiguration.get("username"), smtpConfiguration.get("password")).sendMail(username + "@" + domain, password);
    }

    private String generatePassword() {
        StringBuilder passwordBuilder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            passwordBuilder.append(PASS_CHARS.charAt((int) (Math.random() * 70)));
        }
        return passwordBuilder.toString();
    }
}
