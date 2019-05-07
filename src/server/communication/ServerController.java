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

    /**
     * the Socket the server uses
     */
    ServerSocket server;

    /**
     * Initializes the connection to the client
     */
    @Override
    public void run() {
        try {
            server = new ServerSocket(1025);
            while (true) {
                Socket clientSocket = server.accept();
                Thread clientHandler = new ClientHandlerThread(clientSocket);
                clientHandler.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
