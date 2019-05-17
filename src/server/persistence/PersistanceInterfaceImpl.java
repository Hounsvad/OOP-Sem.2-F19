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
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author Oliver
 */
public class PersistanceInterfaceImpl implements PersistanceInterface {

    private static PersistanceInterfaceImpl instance = null;

    private Map<String, String> configFileMap;

    /**
     * Connection container
     */
    private Connection conn = null;

    private PersistanceInterfaceImpl() {
        //Load settings from config file
        this.configFileMap = new TreeMap<>();
        try (Scanner configFileScanner = new Scanner(getClass().getResourceAsStream("/server/recources/Database.config"))) {

            while (configFileScanner.hasNextLine()) {
                String[] tokens = configFileScanner.nextLine().split(" := ");
                this.configFileMap.put(tokens[0], tokens[1]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://" + configFileMap.get("url") + ":" + configFileMap.get("port") + "/" + configFileMap.get("databaseName") + "?sslmode=require", configFileMap.get("username"), configFileMap.get("password"));
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public static PersistanceInterfaceImpl getInstance() {
        if (instance == null) {
            instance = new PersistanceInterfaceImpl();
        }
        return instance;
    }

    /**
     *
     * @param query
     * @return
     */
    @Override
    public List<String[]> parseQuery(String... query) {

        ResultSet sqlReturnValues;
        List<String[]> output = new ArrayList<>();
        PreparedStatement stmt = null;
        String queryString;
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
            case "addEventParticipant":
                // queryString = "INSERT INTO participation VALUES (" + query[1] + ", " + query[2] + ")";
                try {
                    stmt = conn.prepareStatement("INSERT INTO participation VALUES (?, ?)");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[2]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "addCalendarEvent":
                // queryString = "INSERT INTO calender VALUES ((SELECT MAX(W.event_id) FROM calender as W)+1, " + query[1] + ", '" + query[3] + "', '" + query[4] + "'," + query[2] + ")";
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
                //queryString = "UPDATE calender SET date = " + query[2] + ", event_name = '" + query[4] + "', event_detail = '" + query[5] + "', date_end = " + query[3] + " WHERE event_id = " + query[1];
                break;
            case "removeCalendarEvent":
                try {
                    stmt = conn.prepareStatement("DELETE FROM participation WHERE event_id = ?; DELETE FROM calender WHERE event_id = ?");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                // queryString = "DELETE FROM participation WHERE event_id = '" + query[1] + "'; DELETE FROM calender WHERE event_id = '" + query[1] + "'";
                break;
            case "addPatient":
                try {
                    stmt = conn.prepareStatement("INSERT INTO id VALUES ((SELECT MAX(id) FROM id WHERE id < 9000000000) + 1, ?); INSERT INTO patients VALUES((SELECT MAX(id) FROM id WHERE id < 9000000000), ?)");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[2]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                //queryString = "INSERT INTO id VALUES ((SELECT MAX(id) FROM id WHERE id < 9000000000) + 1, '" + query[1] + "'); INSERT INTO patients VALUES((SELECT MAX(id) FROM id WHERE id < 9000000000), '" + query[2] + "')";
                break;
            case "getPatients":
                try {
                    stmt = conn.prepareStatement("SELECT id.id, id.full_name FROM id, (SELECT patient_id as id FROM patient_assignment WHERE user_id = ?) as compare WHERE compare.id = id.id");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                // queryString = "SELECT id.id, id.full_name FROM id, (SELECT patient_id as id FROM patient_assignment WHERE user_id = " + query[1] + ") as compare WHERE compare.id = id.id";
                break;
            case "getAllPatients":
                try {
                    stmt = conn.prepareStatement("SELECT patients.id, patients.department, id.full_name FROM patients, id, (SELECT department FROM users WHERE id = ?) as dep where patients.id = id.id AND patients.department = dep.department ORDER BY patients.id ASC");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                //queryString = "SELECT patients.id, patients.department, id.full_name FROM patients, id, (SELECT department FROM users WHERE id = '" + query[1] + "') as dep where patients.id = id.id AND patients.department = dep.department ORDER BY patients.id ASC";
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
                // queryString = "INSERT INTO id VALUES((SELECT MAX(id) FROM id)+1, '" + query[2] + "'); INSERT INTO users VALUES ('" + query[1] + "', '" + query[3] + "', '" + query[4] + "', (SELECT MAX(id.id)from id))";
                break;
            case "getMailDomainByDepartment":
                try {
                    stmt = conn.prepareStatement("SELECT department_mail_domain FROM departments WHERE department_id = ?)");
                    stmt.setString(1, (query[1]));

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                // queryString = "SELECT department_mail_domain FROM departments WHERE department_id = '" + query[1] + "'";
                break;
            case "getUsers":
                try {
                    stmt = conn.prepareStatement("SELECT username, users.id, full_name FROM users, id WHERE id.id = users.id AND users.department = ? ORDER BY users.id");
                    stmt.setString(1, (query[1]));

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                //queryString = "SELECT username, users.id, full_name FROM users, id WHERE id.id = users.id AND users.department = '" + query[1] + "' ORDER BY users.id";
                break;
            case "alterUserFullName":
                try {
                    stmt = conn.prepareStatement("UPDATE id SET full_name = ? WHERE id = ?");
                    stmt.setString(1, (query[2]));
                    stmt.setLong(2, Long.parseLong(query[1]));

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                // queryString = "UPDATE id SET full_name = '" + query[2] + "' WHERE id = " + query[1];
                break;
            case "setUserPassword":
                try {
                    stmt = conn.prepareStatement("UPDATE users SET password = ? WHERE id = ?");
                    stmt.setString(1, (query[2]));
                    stmt.setLong(2, Long.parseLong(query[1]));

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                // queryString = "UPDATE users SET password = '" + query[2] + "' WHERE id = " + query[1];
                break;
            case "addUserRole":
                try {
                    stmt = conn.prepareStatement("INSERT INTO role_assignment VALUES (?, ?)");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setString(2, (query[2]));

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                // queryString = "INSERT INTO role_assignment VALUES (" + query[1] + ", '" + query[2] + "')";
                break;
            case "removeUserRole":
                try {
                    stmt = conn.prepareStatement("DELETE FROM role_assignment WHERE user_id = ? AND role = ?");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setString(2, (query[2]));

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                // queryString = "DELETE FROM role_assignment WHERE user_id = " + query[1] + " AND role = '" + query[2] + "'";
                break;
            case "getUserRoles":
                try {
                    stmt = conn.prepareStatement("SELECT role FROM role_assignment where user_id = ?");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                //queryString = "SELECT role FROM role_assignment where user_id = " + query[1];
                break;
            case "getRoles":
                try {
                    stmt = conn.prepareStatement("SELECT * FROM roles");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                // queryString = "SELECT * FROM roles";
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
                // queryString = "INSERT INTO journal VALUES (" + query[1] + ", " + query[3] + ", '" + query[4] + "', " + query[5] + ", '" + query[6] + "')";
                break;
            case "getJournal":
                try {
                    stmt = conn.prepareStatement("SELECT * FROM journal WHERE patient_id = ? AND entry_type = 'journal' LIMIT 30");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                // queryString = "SELECT * FROM journal WHERE patient_id = " + query[1] + " AND entry_type = 'journal' LIMIT 30";
                break;
            case "getMedicalJournal":
                try {
                    stmt = conn.prepareStatement("SELECT * FROM journal WHERE patient_id = ? AND entry_type = 'medicinal' LIMIT 30");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                // queryString = "SELECT * FROM journal WHERE patient_id = " + query[1] + " AND entry_type = 'medicinal' LIMIT 30";
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
                //queryString = "INSERT INTO activity (user_id, type, specifics, ip) VALUES (" + query[4] + ", '" + query[1] + "', '" + query[2] + "', '" + query[3] + "')";
                break;
            case "getActivity":
                try {
                    stmt = conn.prepareStatement("SELECT date, type, specifics, ip FROM activity WHERE date > (date_part('epoch'::text, now()) * (1000)::double precision)-25922000000 AND user_id = ? ORDER BY date desc");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                //queryString = "SELECT date, type, specifics, ip FROM activity WHERE date > (date_part('epoch'::text, now()) * (1000)::double precision)-25922000000 AND user_id = " + query[1] + "ORDER BY date desc";
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
                //queryString = "INSERT INTO messages(sender_id, recipient_id, title, message) VALUES (" + query[1] + ", " + query[2] + ", '" + query[3] + "', '" + query[4] + "')";
                break;
            case "getMessages":
                try {
                    stmt = conn.prepareStatement("SELECT id.full_name, users.username, messages.title, messages.message, messages.date FROM users, id, messages WHERE id.id = sender_id AND users.id = sender_id AND recipient_id = ? ORDER BY date DESC LIMIT 30");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                // queryString = "SELECT id.full_name, users.username, messages.title, messages.message, messages.date FROM users, id, messages WHERE id.id = sender_id AND users.id = sender_id AND recipient_id = " + query[1] + " ORDER BY date DESC LIMIT 30";
                break;
            case "getMenuItems":
                try {
                    stmt = conn.prepareStatement("SELECT DISTINCT modules.name, modules.icon, modules.fxml, modules.index FROM modules, role_assignment WHERE role_assignment.user_id = ? AND (role_assignment.role = modules.role OR role_assignment.role = '000-000') ORDER BY index");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                // queryString = "SELECT DISTINCT modules.name, modules.icon, modules.fxml, modules.index FROM modules, role_assignment WHERE role_assignment.user_id = " + query[1] + " AND (role_assignment.role = modules.role OR role_assignment.role = '000-000') ORDER BY index";
                break;
            case "getUserDepartment":
                try {
                    stmt = conn.prepareStatement("SELECT users.department FROM users WHERE users.id=?");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                // queryString = "SELECT users.department FROM users WHERE users.id=" + query[1];
                break;
            case "getPatientsByDepartment":
                try {
                    stmt = conn.prepareStatement("SELECT id.full_name, patients.id, patients.department FROM patients, id WHERE patients.id = id.id AND patients.department = ? ORDER BY patients.id");
                    stmt.setString(1, (query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                //queryString = "SELECT id.full_name, patients.id, patients.department FROM patients, id WHERE patients.id = id.id AND patients.department = '" + query[1] + "' ORDER BY patients.id";
                break;
            case "getPatientId":
                try {
                    stmt = conn.prepareStatement("SELECT patients.department FROM patients WHERE patients.id=?");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                //queryString = "SELECT patients.department FROM patients WHERE patients.id=" + query[1];
                break;
            case "getDomain":
                try {
                    stmt = conn.prepareStatement("SELECT departments.department_mail_domain FROM users, departments WHERE users.id = ? AND users.department = department_id");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                // queryString = "SELECT departments.department_mail_domain FROM users, departments WHERE users.id = " + query[1] + " AND users.department = department_id";
                break;
            case "getDepartments":
                try {
                    stmt = conn.prepareStatement("SELECT department_id, name FROM departments");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                //queryString = "SELECT department_id, name FROM departments";
                break;
            case "assignPatient":
                try {
                    stmt = conn.prepareStatement("INSERT INTO patient_assignment VALUES (?,?)");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[2]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                // queryString = "INSERT INTO patient_assignment VALUES (" + query[1] + "," + query[2] + ")";
                break;
            case "removeAssignedPatient":
                try {
                    stmt = conn.prepareStatement("DELETE FROM patient_assignment WHERE user_id = ? AND patient_id = ?");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[2]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                // queryString = "DELETE FROM patient_assignment WHERE user_id = " + query[1] + " AND patient_id = " + query[2];
                break;
            case "setUserDepartment":
                try {
                    stmt = conn.prepareStatement("UPDATE users SET department=? WHERE id = ?");
                    stmt.setString(1, (query[2]));
                    stmt.setLong(2, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                //queryString = "UPDATE users SET department='" + query[2] + "' WHERE id = " + query[1] + "";
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
                    //UPDATE rhythm SET hour = \"hour\", icon = \"icon\", title = \"title\" WHERE patient_id = 1000000001
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            }

            default:
                return new ArrayList<String[]>() {
                    {
                        add(new String[]{"Error", "Unexpected error"});
                    }
                };
        }

        try {
            sqlReturnValues = stmt.executeQuery();//(queryString);
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
                ex.printStackTrace();
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
