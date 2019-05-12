/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation.modules.dashboard;

import client.presentation.CommunicationHandler;
import client.presentation.modules.Module;
import client.presentation.utils.credentials.CredentialContainer;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Oliver
 */
public class DashboardFXMLController extends Module {

    @FXML
    private Text name;
    @FXML
    private JFXListView<ActivityEntry> activityView;
    @FXML
    private JFXListView<MessageEntry> messageView;

    private static List<ActivityEntry> activityEntriesCache;
    private static List<MessageEntry> messageEntriesCache;

    private static ChangeListener changeListener;

    /**
     * Initializes the controller class.
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

            }
        });

        messageView.setOnMouseClicked((MouseEvent event) -> {
            try {
                messageView.getSelectionModel().getSelectedItem().showPopup();
            } catch (NullPointerException e) {
                //Do nothing
            }
        });

        changeListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> a, Boolean b, Boolean c) {
                if (b == true && c == false) {
                    clearAll();
                } else if (b == false && c == true) {
                    updateData();
                }
            }
        };

        //Make sure that there is only one active listener from this class
        CredentialContainer.getInstance().getCredentialReadyProperty().removeListener(changeListener);
        CredentialContainer.getInstance().getCredentialReadyProperty().addListener(changeListener);
    }

    @FXML
    private void addNewMessage(MouseEvent event) {
        new MessageEntry(this);
    }

    public void updateData() {
        //Check if there are cached values, and use those until the new values arrive
        if (activityEntriesCache != null && messageEntriesCache != null) {
            activityView.getItems().clear();
            messageView.getItems().clear();
            activityView.getItems().addAll(activityEntriesCache);
            messageView.getItems().addAll(messageEntriesCache);
        }

        //Find the previous Updater thread, if any and kill it
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals("DashboardUpdater")) {
                t.interrupt();
            }
        }

        //New thread to get and update the values and store the new values in a cache
        new Thread(() -> {
            try {
                List<ActivityEntry> activityEntries = new ArrayList<>();
                List<MessageEntry> messageEntries = new ArrayList<>();

                //Get new values
                try {
                    communicationHandler.sendQuery("getActivity").forEach((tuple) -> activityEntries.add(new ActivityEntry(tuple[1], new Date(Long.parseLong(tuple[0])), tuple[2], tuple[3])));
                    communicationHandler.sendQuery("getMessages").forEach((tuple) -> messageEntries.add(new MessageEntry(tuple[2], tuple[0] + "(" + tuple[1] + ")", tuple[3], new Date(Long.parseLong(tuple[4])))));
                } catch (Exception e) {
                }

                //Update the values to the FX elements and update the cache
                Platform.runLater(() -> {
                    try {
                        activityView.getItems().clear();
                        messageView.getItems().clear();
                        activityView.getItems().addAll(activityEntries);
                        messageView.getItems().addAll(messageEntries);

                        activityEntriesCache = new ArrayList<>(activityEntries);
                        messageEntriesCache = new ArrayList<>(messageEntries);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                });

            } catch (Exception ex) {
            }
        }, "DashboardUpdater").
                start();
    }

    protected void clearAll() {
        activityEntriesCache = null;
        messageEntriesCache = null;
        Platform.runLater(() -> {
            activityView.getItems().clear();
            messageView.getItems().clear();
        });
    }
}
