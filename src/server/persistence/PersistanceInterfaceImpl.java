/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
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

    /**
     * Table and column names
     */
    private final Map<String, String[]> tables = new HashMap<String, String[]>() {
        {
            put("activity", new String[]{"user_id", "date", "type", "specifics", "ip"});
            put("calender", new String[]{"event_id", "date", "event_name", "event_detail", "date_end"});
            put("departments", new String[]{"name", "department_id", "department_mail_domain"});
            put("id", new String[]{"uid", "fullName"});
            put("journal", new String[]{"patient_id", "department_id", "date", "text", "created_by_id", "entry_type"});
            put("messages", new String[]{"sender_id", "recipient_id", "title", "message", "date"});
            put("modules", new String[]{"name", "icon", "fxml", "role"});
            put("participation", new String[]{"event_id", "id"});
            put("patient_assignment", new String[]{"user_id", "patient_id"});
            put("patients", new String[]{"id", "department"});
            put("rhythm", new String[]{"patien_id", "hour", "icon", "title", "text"});
            put("role_assignment", new String[]{"user_id", "role", "department"});
            put("roles", new String[]{"role", "description"});
            put("users", new String[]{"username", "password", "department", "id"});

        }
    };
    private Map<String, String> configFileMap;

    /**
     * Connection container
     */
    private Connection conn = null;

    private PersistanceInterfaceImpl() {
        //Load settings from config file
        this.configFileMap = new TreeMap<>();
        try (Scanner configFileScanner = new Scanner(getClass().getResourceAsStream("/server/recources/DatabaseConfiguration.config"))) {

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

    public static PersistanceInterfaceImpl getInstance() {
        if (instance == null) {
            instance = new PersistanceInterfaceImpl();
        }
        return instance;
    }

    @Override
    public List<String[]> parseQuery(String... query) {

        ResultSet sqlReturnValues;
        List<String[]> output = new ArrayList<String[]>();
        Statement stmt = null;
        String queryString;
        try {
            //readying sql driver and connection
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        switch (query[0]) {
            case "checkCredentials":
                queryString = "SELECT id.full_name, matchingId.id FROM id, (SELECT id FROM users WHERE password = '" + query[2] + "' AND username = '" + query[1] + "') AS matchingId WHERE matchingId.id = id.id";
                break;
            case "getCalendar":
                queryString = "SELECT calendar.* FROM calendar, (SELECT participation.event_id FROM participation WHERE participation.id = " + query[1] + ") AS x WHERE (calendar.date >= " + query[2] + " AND calendar.date <= " + query[3] + ") AND calendar.event_id = x.event_id";
                break;
            case "getEventParticipants":
                queryString = "SELECT id.id, id.full_name FROM id, (SELECT id FROM participation WHERE event_id = " + query[1] + ") as participants WHERE id.id = participants.id";
                break;
            case "addEventParticipant":
                queryString = "INSERT INTO participation VALUES (" + query[1] + ", " + query[2] + ")";
                break;
            case "addCalendarEvent":
                queryString = "INSERT INTO calender VALUES ((SELECT MAX(W.event_id) FROM calender as W)+1, " + query[1] + ", '" + query[3] + "', '" + query[4] + "'," + query[2] + ")";
                break;
            case "updateCalendarEvent":
                queryString = "UPDATE calender SET date = " + query[2] + ", event_name = '" + query[4] + "', event_detail = '" + query[5] + "', date_end = " + query[3] + " WHERE event_id = " + query[1];
                break;
            case "removeCalendarEvent":
                queryString = "DELETE FROM participation WHERE event_id = '" + query[1] + "'; DELETE FROM calender WHERE event_id = '" + query[1] + "'";
                break;
            case "addPatient":
                queryString = "INSERT INTO id VALUES ((SELECT MAX(id) FROM id WHERE id < 9000000000) + 1, '" + query[1] + "'); INSERT INTO patients VALUES((SELECT MAX(id) FROM id WHERE id < 9000000000), '" + query[2] + "')";
                break;
            case "getPatients":
                queryString = "SELECT id.id, id.full_name FROM id, (SELECT patient_id as id FROM patient_assignment WHERE user_id = " + query[1] + ") as compare WHERE compare.id = id.id";
                break;
            case "getAllPatients":
                queryString = "SELECT patients.id, patients.department, id.full_name FROM patients, id, (SELECT department FROM users WHERE id = '" + query[1] + "') as dep where patients.id = id.id AND patients.department = dep.department ORDER BY patients.id ASC";
                break;
            case "addUser":
                queryString = "INSERT INTO id VALUES((SELECT MAX(id) FROM id)+1, '" + query[2] + "'); INSERT INTO users VALUES ('" + query[1] + "', '" + query[3] + "', '" + query[4] + "', (SELECT MAX(id.id)from id))";
                break;
            case "getMailDomainByDepartment":
                queryString = "SELECT department_mail_domain FROM departments WHERE department_id = '" + query[1] + "'";
                break;
            case "getUsers":
                queryString = "SELECT username, users.id, full_name FROM users, id WHERE id.id = users.id AND users.department = '" + query[1] + "' ORDER BY users.id";
                break;
            case "alterUserFullName":
                queryString = "UPDATE id SET full_name = '" + query[2] + "' WHERE id = " + query[1];
                break;
            case "setUserPassword":
                queryString = "UPDATE users SET password = '" + query[2] + "' WHERE id = " + query[1];
                break;
            case "addUserRole":
                queryString = "INSERT INTO role_assignment VALUES (" + query[1] + ", '" + query[2] + "')";
                break;
            case "removeUserRole":
                queryString = "DELETE FROM role_assignment WHERE user_id = " + query[1] + " AND role = '" + query[2] + "'";
                break;
            case "getUserRoles":
                queryString = "SELECT role FROM role_assignment where user_id = " + query[1];
                break;
            case "getRoles":
                queryString = "SELECT * FROM roles";
                break;
            case "addJournalEntry":
                queryString = "INSERT INTO journal VALUES (" + query[1] + ", '" + query[2] + "', " + query[3] + ", '" + query[4] + "', " + query[5] + ", '" + query[6] + "')";
                break;
            case "getJournal":
                queryString = "SELECT * FROM journal WHERE patient_id = " + query[1] + " AND entry_type = 'journal' LIMIT 30";
                break;
            case "getMedicalJournal":
                queryString = "SELECT * FROM journal WHERE patient_id = " + query[1] + " AND entry_type = 'medicinal' LIMIT 30";
                break;
            case "addActivity":
                queryString = "INSERT INTO activity (user_id, type, specifics, ip) VALUES (" + query[4] + ", '" + query[1] + "', '" + query[2] + "', '" + query[3] + "')";
                break;
            case "getActivity":
                queryString = "SELECT date, type, specifics, ip FROM activity WHERE date > (date_part('epoch'::text, now()) * (1000)::double precision)-25922000000 AND user_id = " + query[1] + "ORDER BY date desc";
                break;
            case "sendMessage":
                queryString = "INSERT INTO messages(sender_id, recipient_id, title, message) VALUES (" + query[1] + ", " + query[2] + ", '" + query[3] + "', '" + query[4] + "')";
                break;
            case "getMessages":
                queryString = "SELECT id.full_name, users.username, messages.title, messages.message, messages.date FROM users, id, messages WHERE id.id = sender_id AND users.id = sender_id AND recipient_id = " + query[1] + " ORDER BY date DESC LIMIT 30";
                break;
            case "getMenuItems":
                queryString = "SELECT DISTINCT modules.name, modules.icon, modules.fxml, modules.index FROM modules, role_assignment WHERE role_assignment.user_id = " + query[1] + " AND (role_assignment.role = modules.role OR role_assignment.role = '000-000') ORDER BY index";
                break;
            case "getUserDepartment":
                queryString = "SELECT users.department FROM users WHERE users.id=" + query[1];
                break;
            case "getPatientsByDepartment":
                queryString = "SELECT id.full_name, patients.id, patients.department FROM patients, id WHERE patients.id = id.id AND patients.department = '" + query[1] + "' ORDER BY patients.id";
                break;
            case "getPatientId":
                queryString = "SELECT patients.department FROM patients WHERE patients.id=" + query[1];
                break;
            case "getDomain":
                queryString = "SELECT departments.department_mail_domain FROM users, departments WHERE users.id = " + query[1] + " AND users.department = department_id";
                break;
            case "getDepartments":
                queryString = "SELECT department_id, name FROM departments";
                break;
            case "assignPatient":
                queryString = "INSERT INTO patient_assignment VALUES (" + query[1] + "," + query[2] + ")";
                break;
            case "removeAssignedPatient":
                queryString = "DELETE FROM patient_assignment WHERE user_id = " + query[1] + " AND patient_id = " + query[2];
                break;
            case "setUserDepartment":
                queryString = "UPDATE users SET department='" + query[2] + "' WHERE id = " + query[1] + "";
                break;
            default:
                return new ArrayList<String[]>() {
                    {
                        add(new String[]{"Error", "Unexpected error"});
                    }
                };
        }

        try {
            sqlReturnValues = stmt.executeQuery(queryString);
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
            //ex.printStackTrace();
            output = new ArrayList<String[]>() {
                {
                    add(new String[]{"Error", "Unexpected sql error"});
                }
            };
        }
        return output;

    }

}
