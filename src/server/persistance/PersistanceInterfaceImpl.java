/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.persistance;

import java.sql.Connection;
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
    s

    private String databaseName;

    private String dbURL;

    private String dbUser;

    private String dbPassword;

    /**
     * Connection container
     */
    private Connection conn = null;

    public PersistanceInterfaceImpl() {
        //Load settings from config file
        Map<String, String> configFileMap = new TreeMap<>();
        try (Scanner configFileScanner = new Scanner(getClass().getResourceAsStream("recources/DatabaseConfiguration.config"))) {

            while (configFileScanner.hasNextLine()) {
                String[] tokens = configFileScanner.nextLine().split(" := ");
                configFileMap.put(tokens[0], tokens[1]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        //Set local parametes based on config file
        //this.databaseName = configFileMap.get("this")
    }

    @Override
    public List<String[]> parseQuery(String[] query) {
        switch (query[0]) {
            case "checkCredentials":

                break;
        }
        return null;
    }

}
