/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Oliver
 */
public class PersistanceInterfaceImpl implements PersistanceInterface {

    private static PersistanceInterfaceImpl instance = null;
    private Map<String, String> configFileMap;
    private Connection conn = null;

    /**
     * Private constructor to initiate the singleton
     */
    private PersistanceInterfaceImpl() {

        //Load settings from config file
        this.configFileMap = new HashMap<>();
        new Scanner(getClass().getResourceAsStream("/server/recources/Database.config")).useDelimiter("\n").forEachRemaining((s) -> configFileMap.put(s.split(" := ")[0], s.split(" := ")[1]));

        //Initiate connection
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://" + configFileMap.get("url") + ":" + configFileMap.get("port") + "/" + configFileMap.get("databaseName") + "?sslmode=require", configFileMap.get("username"), configFileMap.get("password"));
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns a singleton instance of the persistence controller so as to
     * optimise speed by keeping the SQL connection open
     *
     * @return the instance of the persistence controller
     */
    public static PersistanceInterfaceImpl getInstance() {
        if (instance == null) {
            instance = new PersistanceInterfaceImpl();
        }
        return instance;
    }

    /**
     * Parses a query based on a set of predefined actions
     * <P>
     * The first position of the query is the action to be performed and the
     * following positions are parameters
     *
     * <pre>
     *  * "checkCredentials"            Username    HashedPassword
     *  * "getCalendar"                 id          Date_min            Date_max
     *  * "getEventParticipants"        eventId
     *  * "addCalendarEvent"            date_start  date_end            name        details
     *  * "addEventParticipant"         eventId     participant
     *  * "removeEventParticipant"      eventId     participant
     *  * "updateCalendarEvent"         eventId     date_start          date_end    name        details
     *  * "removeCalendarEvent"         eventId
     *  * "setRhythmHour"               Patient     id                  hour        icon	title
     *  * "getDayRhythm"                patient     id
     *  * "updateRhythmHour"            patient     id                  hour        icon	title
     *  * "addPatient"                  fullname    department
     *  * "getpatientsByDepartment"     department
     *  * "getPatients"                 userid
     *  * "addAssignedPatient"          userId      PatientId
     *  * "removeAssignedPatient"       userId      PatientId
     *  * "addUser"                     username    fullname            password    department
     *  * "getUsersByDepartment"        department
     *  * "alterUserFullName"           userId      fullName
     *  * "setUserPassword"             userId      hashedPassword
     *  * "setUserDepartment"           userId      departmentId
     *  * "getUserDepartment"           userid
     *  * "addUserRole"                 userId      role
     *  * "removeUserRole"              userId      role
     *  * "getUserRoles"                userId
     *  * "getRoles"
     *  * "getDepartments"
     *  * "addJournalEntry"             Patient     id                  department  date	contents	userid      entrytype
     *  * "getJournal"                  Patient     id
     *  * "getMedicinalJournal"         Patient     id
     *  * "addActivity"                 type        specifics           ip          userId
     *  * "getActivity"                 userId
     *  * "getMessages"                 userid
     *  * "sendMessage"                 from id     recieverUserName    header      contents
     *  * "getMenuItems"                userid
     *  * "getMailDomainByDepartment"   userid
     * </pre>
     *
     * @param query is the query
     * @return the result of the query or a statement of success or error and
     *         cause
     */
    @Override
    public List<String[]> parseQuery(String... query) {
        List<String[]> output = new ArrayList<>();
        PreparedStatement stmt = null;
        switch (query[0]) {
            case "checkCredentials":
                try {
                    stmt = conn.prepareStatement("SELECT id.full_name, matchingId.id FROM id, (SELECT id FROM users WHERE password = ? AND username = ?) AS matchingId WHERE matchingId.id = id.id");
                    stmt.setString(1, query[2]);
                    stmt.setString(2, query[1]);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "getCalendar":
                try {
                    stmt = conn.prepareStatement("SELECT calendar.* FROM calendar, (SELECT participation.event_id FROM participation WHERE participation.id = ?) AS x WHERE (calendar.date >= ? AND calendar.date <= ?) AND calendar.event_id = x.event_id");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[2]));
                    stmt.setLong(3, Long.parseLong(query[3]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "getEventParticipants":
                try {
                    stmt = conn.prepareStatement("SELECT id.id, id.full_name FROM id, (SELECT id FROM participation WHERE event_id = ?) as participants WHERE id.id = participants.id");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "addCalendarEvent":
                try {
                    stmt = conn.prepareStatement("INSERT INTO calender VALUES ((SELECT MAX(W.event_id) FROM calender as W)+1, ?, ?, ?, ?)");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[3]));
                    stmt.setLong(3, Long.parseLong(query[4]));
                    stmt.setLong(4, Long.parseLong(query[2]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "addEventParticipant":
                try {
                    stmt = conn.prepareStatement("INSERT INTO participation VALUES (?, ?)");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[2]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "removeEventParticipant":
                try {
                    stmt = conn.prepareStatement("DELETE FROM participation WHERE event_id = ? AND id = ?");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[2]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "updateCalendarEvent":
                try {
                    stmt = conn.prepareStatement("UPDATE calender SET date = ?, event_name = ?, event_detail = ?, date_end = ? WHERE event_id = ?");
                    stmt.setLong(1, Long.parseLong(query[2]));
                    stmt.setString(2, (query[4]));
                    stmt.setString(3, (query[5]));
                    stmt.setLong(4, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "removeCalendarEvent":
                try {
                    stmt = conn.prepareStatement("DELETE FROM participation WHERE event_id = ?; DELETE FROM calender WHERE event_id = ?");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "setRhythmHour": {
                try {
                    stmt = conn.prepareStatement("INSERT INTO rhythm(patient_id, hour, icon, title) VALUES (?, ?, ?, ?)");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setInt(2, Integer.parseInt(query[2]));
                    stmt.setString(3, query[3]);
                    stmt.setString(4, query[4]);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case "getDayRhythm": {
                try {
                    stmt = conn.prepareStatement("SELECT hour, icon, title FROM rhythm WHERE patient_id = ?");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case "updateRhythmHour": {
                try {
                    stmt = conn.prepareStatement("UPDATE rhythm SET icon = ?, title = ? WHERE patient_id = ? AND hour = ?");
                    stmt.setString(3, query[1]);
                    stmt.setInt(4, Integer.parseInt(query[2]));
                    stmt.setLong(1, Long.parseLong(query[3]));
                    stmt.setString(2, query[4]);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case "addPatient":
                try {
                    stmt = conn.prepareStatement("INSERT INTO id VALUES ((SELECT MAX(id) FROM id WHERE id < 9000000000) + 1, ?); INSERT INTO patients VALUES((SELECT MAX(id) FROM id WHERE id < 9000000000), ?)");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[2]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "getPatientsByDepartment":
                try {
                    stmt = conn.prepareStatement("SELECT id.full_name, patients.id, patients.department FROM patients, id WHERE patients.id = id.id AND patients.department = ? ORDER BY patients.id");
                    stmt.setString(1, (query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "getPatients":
                try {
                    stmt = conn.prepareStatement("SELECT id.id, id.full_name FROM id, (SELECT patient_id as id FROM patient_assignment WHERE user_id = ?) as compare WHERE compare.id = id.id");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "addAssignedPatient":
                try {
                    stmt = conn.prepareStatement("INSERT INTO patient_assignment VALUES (?,?)");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[2]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "removeAssignedPatient":
                try {
                    stmt = conn.prepareStatement("DELETE FROM patient_assignment WHERE user_id = ? AND patient_id = ?");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[2]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "addUser":
                try {
                    stmt = conn.prepareStatement("INSERT INTO id VALUES((SELECT MAX(id) FROM id)+1, ?); INSERT INTO users VALUES (?, ?, ?, (SELECT MAX(id.id)from id))");
                    stmt.setLong(1, Long.parseLong(query[2]));
                    stmt.setString(2, (query[1]));
                    stmt.setString(3, (query[3]));
                    stmt.setString(4, (query[4]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "getUsersByDepartment":
                try {
                    stmt = conn.prepareStatement("SELECT username, users.id, full_name FROM users, id WHERE id.id = users.id AND users.department = ? ORDER BY users.id");
                    stmt.setString(1, (query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;

            case "alterUserFullName":
                try {
                    stmt = conn.prepareStatement("UPDATE id SET full_name = ? WHERE id = ?");
                    stmt.setString(1, (query[2]));
                    stmt.setLong(2, Long.parseLong(query[1]));

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "setUserPassword":
                try {
                    stmt = conn.prepareStatement("UPDATE users SET password = ? WHERE id = ?");
                    stmt.setString(1, (query[2]));
                    stmt.setLong(2, Long.parseLong(query[1]));

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "setUserDepartment":
                try {
                    stmt = conn.prepareStatement("UPDATE users SET department=? WHERE id = ?");
                    stmt.setString(1, (query[2]));
                    stmt.setLong(2, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "getUserDepartment":
                try {
                    stmt = conn.prepareStatement("SELECT users.department FROM users WHERE users.id=?");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "addUserRole":
                try {
                    stmt = conn.prepareStatement("INSERT INTO role_assignment VALUES (?, ?)");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setString(2, (query[2]));

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "removeUserRole":
                try {
                    stmt = conn.prepareStatement("DELETE FROM role_assignment WHERE user_id = ? AND role = ?");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setString(2, (query[2]));

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "getUserRoles":
                try {
                    stmt = conn.prepareStatement("SELECT role FROM role_assignment where user_id = ?");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "getRoles":
                try {
                    stmt = conn.prepareStatement("SELECT * FROM roles");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "getDepartments":
                try {
                    stmt = conn.prepareStatement("SELECT department_id, name FROM departments");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "addJournalEntry":
                try {
                    stmt = conn.prepareStatement("INSERT INTO journal VALUES (?, ?, ?, ?, ?)");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[4]));
                    stmt.setString(3, query[3]);
                    stmt.setLong(4, Long.parseLong(query[5]));
                    stmt.setString(5, query[2]);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "getJournal":
                try {
                    stmt = conn.prepareStatement("SELECT * FROM journal WHERE patient_id = ? AND entry_type = 'journal' LIMIT 30");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "getMedicalJournal":
                try {
                    stmt = conn.prepareStatement("SELECT * FROM journal WHERE patient_id = ? AND entry_type = 'medicinal' LIMIT 30");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "addActivity":
                try {
                    stmt = conn.prepareStatement("INSERT INTO activity (user_id, type, specifics, ip) VALUES (?, ?, ?,?)");
                    stmt.setLong(1, Long.parseLong(query[4]));
                    stmt.setString(2, query[1]);
                    stmt.setString(3, query[2]);
                    stmt.setString(4, query[3]);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "getActivity":
                try {
                    stmt = conn.prepareStatement("SELECT date, type, specifics, ip FROM activity WHERE date > (date_part('epoch'::text, now()) * (1000)::double precision)-25922000000 AND user_id = ? ORDER BY date desc");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "sendMessage":
                try {
                    stmt = conn.prepareStatement("INSERT INTO messages(sender_id, recipient_id, title, message) VALUES (?, ?, ?, ?)");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[2]));
                    stmt.setString(3, query[3]);
                    stmt.setString(4, query[4]);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "getMessages":
                try {
                    stmt = conn.prepareStatement("SELECT id.full_name, users.username, messages.title, messages.message, messages.date FROM users, id, messages WHERE id.id = sender_id AND users.id = sender_id AND recipient_id = ? ORDER BY date DESC LIMIT 30");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "getMenuItems":
                try {
                    stmt = conn.prepareStatement("SELECT DISTINCT modules.name, modules.icon, modules.fxml, modules.index FROM modules, role_assignment WHERE role_assignment.user_id = ? AND (role_assignment.role = modules.role OR role_assignment.role = '000-000') ORDER BY index");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "getMailDomainByDepartment":
                try {
                    stmt = conn.prepareStatement("SELECT department_mail_domain FROM departments WHERE department_id = ?)");
                    stmt.setString(1, (query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            default:
                return new ArrayList<String[]>() {
                    {
                        add(new String[]{"Error", String.format("The action \"%s\" is not defined", query[0])});
                    }
                };
        }

        //Exceqution of the query
        try {
            ResultSet sqlReturnValues = stmt.executeQuery();
            int columnCount = sqlReturnValues.getMetaData().getColumnCount();
            output = new ArrayList<>();
            while (sqlReturnValues.next()) {
                String[] temp = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    temp[i - 1] = sqlReturnValues.getString(i);
                }
                output.add(temp);

            }
        } catch (SQLException ex) {
            if (!ex.getMessage().contains("No results")) {
                ex.printStackTrace(System.err);
            }
            output = new ArrayList<String[]>() {
                {
                    add(new String[]{"Error", "Unexpected sql error"});
                }
            };
        }
        return output;
    }
}
