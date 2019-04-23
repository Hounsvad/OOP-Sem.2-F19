/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

/**
 *
 * @author duffy
 */
public class CommunicationInterfaceImpl implements CommunicationInterface {
    
    /**
     * 
     */
    List<String[]> list = new ArrayList();
    
    /**
     * The possible commands for the system
     */
    enum Commands {
        /**
         * The login command 
         */
        LOGIN(3, "login"),
        /**
         * the calender command
         */
        GETCALENDER(4, "getCalender"),
        /**
         * the medicin command
         */
        MEDICIN(2, "getMedicin"),
        /**
         * the userlist command
         */
        USERLIST(3, "getUsers");
        
        /**
         * the count of attributes for the commands
         */
        int count = 0;
        
        /**
         * the command that gets sent
         */
        String command;

        /**
         * Uses the enum with the given count and command
         * @param count
         * @param command 
         */
        Commands(int count, String command) {
            this.count = count;
            this.command = command;
        }
        
        /**
         * Gets the count
         * @return 
         */
        public int getCount() {
            return this.count;
        }

        /**
         * gets the command
         * @return 
         */
        public String getCommand() {
            return this.command;
        }

    }

    @Override
    public List<String[]> sendQuery(String[] query) {
        if (checkQuery(query)) {
            try {
                Socket clientSocket = new Socket("localhost", 1025);
                ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                output.writeObject(query);
                return ((List<String[]>) input.readObject());
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public boolean checkQuery(String[] query) {
        for (Commands com : Commands.values()) {
            if (query[0] == com.getCommand() && query.length == com.count) {
                return true;
            }
        }
        return false;
    }
}
