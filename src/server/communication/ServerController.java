/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author duffy
 */
public class ServerController implements Runnable {

    ServerSocket server;

    public void run() {
        try {
            server = new ServerSocket(1025);
            while (true) {
                Socket clientSocket = server.accept();
                Runnable clientHandler = new ClientHandlerThread(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
