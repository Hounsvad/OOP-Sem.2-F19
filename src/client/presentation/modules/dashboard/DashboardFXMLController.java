/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation.modules.dashboard;

import client.presentation.CommunicationHandler;
import client.presentation.utils.credentials.CredentialContainer;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Oliver
 */
public class DashboardFXMLController implements Initializable {
    
    /**
     * The name of the user
     */
    @FXML
    private Text name;
    
    /**
     * The Listview for the activity entries
     */
    @FXML
    private JFXListView<ActivityEntry> activityView;
    /**
     * The ListView for the Message entries
     */
    @FXML
    private JFXListView<MessageEntry> messageView;

    private final CommunicationHandler communicationHandler = CommunicationHandler.getInstance();
    private final CredentialContainer credentialContainer = CredentialContainer.getInstance();

    /**
     * Initializes the controller class
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set the text on name to the name of the user
        name.setText(CommunicationHandler.getInstance().getName());

        updateData();

        activityView.setOnMouseClicked((MouseEvent event) -> {
            try {
                activityView.getSelectionModel().getSelectedItem().showPopup();
            } catch (NullPointerException e) {

            /**
             * Opens the popup for the clicked on activity entry
             * @param event 
             */
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
            
            /**
             * Opens the popup for the clicked on message entry
             * @param event 
             */
            @Override
            public void handle(MouseEvent event) {
                try {
                    messageView.getSelectionModel().getSelectedItem().showPopup();
                } catch (NullPointerException e) {
                }
            }
        });
    }

    @FXML
    private void addNewMessage(MouseEvent event) {
        MessageEntry.showCreationPopup();
    }

    private void updateData() {
        try {
            List<ActivityEntry> activityEntries = new ArrayList<>();
            communicationHandler.sendQuery(new String[]{"getActivity", credentialContainer.getUsername(), credentialContainer.getPassword()}).forEach((tuple) -> activityEntries.add(new ActivityEntry(tuple[1], new Date(Integer.parseInt(tuple[0])), tuple[2], tuple[3])));
            activityView.getItems().addAll(activityEntries);

            List<MessageEntry> messageEntries = new ArrayList<>();
            communicationHandler.sendQuery(new String[]{"getMessages", credentialContainer.getUsername(), credentialContainer.getPassword()}).forEach((tuple) -> messageEntries.add(new MessageEntry(tuple[2], tuple[0] + "(" + tuple[1] + ")", tuple[3], new Date(Integer.parseInt(tuple[4])))));
            messageView.getItems().addAll(messageEntries);
        } catch (NullPointerException e) {
        }
    }
}
