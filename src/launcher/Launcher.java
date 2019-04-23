/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package launcher;

import client.presentation.Presentation;
import javafx.application.Application;
import javafx.stage.Stage;
import server.communication.ServerController;

/**
 *
 * @author Oliver
 */
public class Launcher extends Application {

    public static void main(String[] args) {
        new Thread(() -> Presentation.main(args)).start();
        new Thread(new ServerController()).start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Downside of creating a JavaFX project
    }
}
