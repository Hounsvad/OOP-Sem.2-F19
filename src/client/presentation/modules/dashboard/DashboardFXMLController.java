/* 
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.modules.dashboard;

import client.presentation.containers.entries.ActivityEntry;
import client.presentation.containers.entries.Cache;
import client.presentation.containers.entries.Entry;
import client.presentation.containers.entries.MessageEntry;
import client.presentation.modules.Module;
import client.presentation.utils.credentials.CredentialContainer;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Sanitas Solutions
 */
public class DashboardFXMLController extends Module {

    @FXML
    private Text name;
    @FXML
    private JFXListView<ActivityEntry> activityView;
    @FXML
    private JFXListView<MessageEntry> messageView;

    private static List<ActivityEntry> activityEntriesCacheOld;
    private static List<MessageEntry> messageEntriesCacheOld;

    private final Map<String, List<Entry>> cache = Cache.getInstance().getCache();

    private static ChangeListener changeListener;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Set the text on name to the name of the user
        name.setText(communicationHandler.getName());

        updateData();

        //Opens popup upon click on elements in the listViews
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
                //Do nothing n the event that the user cliced in the message view but not on a message
            }
        });

        //Instantiated custom changelisner in order to ensure that it can be removed upon re-setting it
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
        //Shows its own popup upon creation and the instance is not used further
        new MessageEntry(this);
    }

    @Override
    public void updateData() {
        //Check if there are cached values, and use those until the new values arrive
        if (cache.get("DashboardActivity") != null && cache.get("DashboardMessage") != null) {
            activityView.getItems().clear();
            messageView.getItems().clear();
            //Gets cached entries and casts them to the appropriate type
            activityView.getItems().addAll(cache.get("DashboardActivity").stream().map(t -> (ActivityEntry) t).collect(Collectors.toList()));
            messageView.getItems().addAll(cache.get("DashboardMessage").stream().map(t -> (MessageEntry) t).collect(Collectors.toList()));
        }

        //Find the previous Updater thread, if any and kill it
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals("DashboardUpdater")) {
                t.interrupt();
            }
        }

        //New thread to get and update the values and store the new values in a cache
        new Thread(() -> {
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
                    if (cache.get("DashboardActivity") != null && cache.get("DashboardMessage") != null) {
                        cache.get("DashboardActivity").clear();
                        cache.get("DashboardActivity").addAll(activityEntries);
                        cache.get("DashboardMessage").clear();
                        cache.get("DashboardMessage").addAll(messageEntries);
                    } else {
                        cache.put("DashboardActivity", new ArrayList<>(activityEntries));
                        cache.put("DashboardMessage", new ArrayList<>(messageEntries));
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            });
        }, "DashboardUpdater").start();
    }

    @Override
    protected void clearAll() {
        cache.remove("getActivity");
        cache.remove("getMessages");
        Platform.runLater(() -> {
            activityView.getItems().clear();
            messageView.getItems().clear();
        });
    }
}
