/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation.modules.dashboard;

import client.presentation.CommunicationHandler;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Oliver
 */
public class DashboardFXMLController implements Initializable {

    @FXML
    private Text name;
    @FXML
    private JFXListView<ActivityEntry> activityView;
    @FXML
    private JFXListView<MessageEntry> messageView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        name.setText(CommunicationHandler.getInstance().getName());
        name.setFill(Paint.valueOf("#2e4057"));
        ActivityEntry[] activityEntries = new ActivityEntry[]{
            new ActivityEntry(("Login"), new Date(), "Common login"),
            new ActivityEntry(("Delete"), new Date(), "Everything"),
            new ActivityEntry(("Login"), new Date(), "Common login"),
            new ActivityEntry(("Delete"), new Date(), "Everything"),
            new ActivityEntry(("Login"), new Date(), "Common login"),
            new ActivityEntry(("Delete"), new Date(), "Everything"),
            new ActivityEntry(("Login"), new Date(), "Common login"),
            new ActivityEntry(("Delete"), new Date(), "Everything"),
            new ActivityEntry(("Login"), new Date(), "Common login"),
            new ActivityEntry(("Delete"), new Date(), "Everything"),
            new ActivityEntry(("Login"), new Date(), "Common login"),
            new ActivityEntry(("Delete"), new Date(), "Everything"),
            new ActivityEntry(("Login"), new Date(), "Common login"),
            new ActivityEntry(("Delete"), new Date(), "Everything"),
            new ActivityEntry(("Login"), new Date(), "Common login"),
            new ActivityEntry(("Delete"), new Date(), "Everything"),
            new ActivityEntry(("Login"), new Date(), "Common login"),
            new ActivityEntry(("Delete"), new Date(), "Everything"),
            new ActivityEntry(("Login"), new Date(), "Common login"),
            new ActivityEntry(("Delete"), new Date(), "Everything"),
            new ActivityEntry(("Create journal"), new Date(), "Frederik Hounsvwad awdawdawdad")
        };

        MessageEntry[] messageEntries = new MessageEntry[]{
            new MessageEntry(("Ny bruger"), "John Lennon", "Vi har fået en ny bruger som hedder Gorm, så tag godt imod ham. Han er ny i afdelingen og kommer fremad til at være UX", new Date()),
            new MessageEntry(("Login"), "john", "Common login", new Date()),
            new MessageEntry(("Login"), "john", "Common login", new Date()),
            new MessageEntry(("Login"), "john", "Common login", new Date()),
            new MessageEntry(("Login"), "john", "Common login", new Date()),
            new MessageEntry(("Login"), "john", "Common login", new Date()),
            new MessageEntry(("Login"), "john", "Common login", new Date())};

        activityView.getItems().addAll(activityEntries);
        activityView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                try {
                    activityView.getSelectionModel().getSelectedItem().showPopup();
                } catch (NullPointerException e) {

                }
            }
        });
        messageView.getItems().addAll(messageEntries);
        messageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                try {
                    messageView.getSelectionModel().getSelectedItem().showPopup();
                } catch (NullPointerException e) {
                }
            }
        }
        );
    }

}
