/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package server.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Sanitas Solutions
 */
public class ServerController implements Runnable {

    private ServerSocket server;

    /**
     * initialises the connection to the client
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
            ex.printStackTrace(System.err);
        }
    }
}
