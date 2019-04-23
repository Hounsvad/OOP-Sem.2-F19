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

    List<String[]> list = new ArrayList();

    enum Commands {
        LOGIN(3, "login"),
        GETCALENDER(4, "getCalender"),
        MEDICIN(2, "getMedicin"),
        USERLIST(3, "getUsers");

        int count = 0;
        String command;

        Commands(int count, String command) {
            this.count = count;
            this.command = command;
        }

        public int getCount() {
            return this.count;
        }

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
