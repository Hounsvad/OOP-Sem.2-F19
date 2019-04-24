/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.persistance;

import java.io.File;
import java.io.IOException;
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

    private Map<String, File> tables = new HashMap<String, File>() {
        {
            put("users", new File("db/users.csv"));
            put("messages", new File("db/messages.csv"));
        }
    };

    public PersistanceInterfaceImpl() {
        for (File f : tables.values()) {
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<String[]> parseQuery(String[] query) {
        switch (query[0]) {
            case "login":
                try (Scanner s = new Scanner(tables.get("users"))) {
                    while (s.hasNextLine()) {
                        String[] turple = s.nextLine().split(",");
                        //Check username and password
                        if (turple[0].equals(query[1]) && turple[1].equals(query[2])) {
                            return new ArrayList<String[]>() {
                                {
                                    add(new String[]{
                                        turple[0],
                                        turple[2],
                                        turple[3],
                                        turple[4]
                                    });
                                }
                            };
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;

            case "getMessages":
                try (Scanner s = new Scanner(tables.get("messages"))) {
                    List<String[]> returnVariable = new ArrayList<>();
                    while (s.hasNextLine()) {
                        //Check username
                        String[] turple = s.nextLine().split(",");
                        if (turple[0] == query[1]) {
                            returnVariable.add(turple);
                        }
                    }
                    return returnVariable;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;

            case "getUsers":
                try (Scanner s = new Scanner(tables.get("users"))) {
                    List<String[]> returnVariable = new ArrayList<>();
                    while (s.hasNextLine()) {
                        String[] turple = s.nextLine().split(",");
                        //Check institution
                        if (turple[3] == query[1]) {
                            returnVariable.add(new String[]{
                                turple[0],
                                turple[2],
                                turple[3],
                                turple[4]
                            });
                        }
                    }
                    return returnVariable;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
        }
        return null;
    }

}
