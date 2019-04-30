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
import javafx.event.EventHandler;
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
        try {
            List<ActivityEntry> activityEntries = new ArrayList<>();
            CommunicationHandler.getInstance().sendQuery(new String[]{"getActivity", CredentialContainer.getInstance().getUsername(), CredentialContainer.getInstance().getPassword()}).forEach((tuple) -> activityEntries.add(new ActivityEntry(tuple[1], new Date(Integer.parseInt(tuple[0])), tuple[2], tuple[3])));
            activityView.getItems().addAll(activityEntries);

            List<MessageEntry> messageEntries = new ArrayList<>();
            CommunicationHandler.getInstance().sendQuery(new String[]{"getMessages", CredentialContainer.getInstance().getUsername(), CredentialContainer.getInstance().getPassword()}).forEach((tuple) -> messageEntries.add(new MessageEntry(tuple[1], tuple[0], tuple[2], new Date(Integer.parseInt(tuple[3])))));
            messageView.getItems().addAll(messageEntries);
        } catch (NullPointerException e) {
        }

        activityView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                try {
                    activityView.getSelectionModel().getSelectedItem().showPopup();
                } catch (NullPointerException e) {

                }
            }
        });

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

    @FXML
    private void addNewMessage(MouseEvent event) {
        MessageEntry.showCreationPopup();
    }

}
