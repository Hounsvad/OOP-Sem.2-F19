/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.persistance;

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

    /**
     * Table and column names
     */
    private final Map<String, String[]> tables = new HashMap<String, String[]>() {
        {
            put("activity", new String[]{"user_uid", "date", "type", "specifics", "ip"});
            put("calender", new String[]{"event_id", "date", "event_name", "event_detail"});
            put("departments", new String[]{"name", "department_id", "department_mail_domain"});
            put("id", new String[]{"uid", "fullName", "is_patient"});
            put("journal", new String[]{"patient_uid", "department_id", "date", "text", "created_by_uid", "entry_type"});
            put("messages", new String[]{"sender_uid", "recipient_uid", "title", "message", "date", "seen"});
            put("participation", new String[]{"event_id", "uid"});
            put("patient_assignment", new String[]{"user_uid", "patient_uid"});
            put("rhythm", new String[]{"patien_uid", "hour", "icon", "title", "text"});
            put("role_assignment", new String[]{"user_uid", "role"});
            put("roles", new String[]{"role", "description"});
            put("users", new String[]{"username", "password", "full_name", "department", "uid"});

        }
    };
    private Map<String, String> configFileMap;

    /**
     * Connection container
     */
    private Connection conn = null;

    public PersistanceInterfaceImpl() {
        //Load settings from config file
        this.configFileMap = new TreeMap<>();
        try (Scanner configFileScanner = new Scanner(getClass().getResourceAsStream("recources/DatabaseConfiguration.config"))) {

            while (configFileScanner.hasNextLine()) {
                String[] tokens = configFileScanner.nextLine().split(" := ");
                this.configFileMap.put(tokens[0], tokens[1]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public List<String[]> parseQuery(String[] query) {

        ResultSet sqlReturnValues;
        List<String[]> output = null;
        Statement stmt = null;
        String queryString;
        //readying sql driver and connection
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://" + configFileMap.get("url") + ":" + configFileMap.get("port") + "/" + configFileMap.get("databaseName"), configFileMap.get("username"), configFileMap.get("password"));
            stmt = conn.createStatement();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        switch (query[0]) {
            case "checkCredentials":
                queryString = "SELECT full_name, id FROM users WHERE password = '" + query[2] + "' AND user.username = '" + query[1] + "'"; // returns id, full name
                break;
            case "getCalendar":
                queryString = "SELECT calender.* FROM calender, (SELECT participation.event_id FROM participation WHERE participation.id = " + query[1] + ") AS x WHERE (calender.date < " + query[3] + " AND calender.date > " + query[2] + " ) AND calender.event_id = x.event_id";
                break;

            default:
                return null;
        }

        try {
            sqlReturnValues = stmt.executeQuery(queryString);
            int columnCount = sqlReturnValues.getMetaData().getColumnCount();
            output = new ArrayList<>();
            while (sqlReturnValues.next()) {
                String[] temp = new String[columnCount];
                System.out.println(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    temp[i - 1] = sqlReturnValues.getString(i);
                }
                output.add(temp);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return output;

    }

}
