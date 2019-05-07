/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.dashboard;

import client.presentation.containers.User;
import client.presentation.modules.Popup;
import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Oliver
 */
public class MessageEntryCreationPopupFXMLController extends Popup {

    @FXML
    private JFXComboBox<User> recipients;
    @FXML
    private JFXTextField subject;
    @FXML
    private JFXTextArea message;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<User> userList = new ArrayList<>();
        userList.add(new User("olnor18", "Oliver Lind Nordestgaard", "DEADBEAF001"));
        userList.add(new User("frhou18", "Frederik Alexander Hounsvad", "DEADBEAF002"));
        //communicationHandler.sendQuery(new String[]{"userList", credentialContainer.getUsername(), credentialContainer.getPassword()}).forEach((tuple) -> userList.add(new User(tuple[0], tuple[1], tuple[3])));
        recipients.getItems().addAll(userList);
    }

    @FXML
    private void send() {
        if (recipients.getSelectionModel().getSelectedItem() != null && !subject.getText().isEmpty() && !message.getText().isEmpty()) {
            communicationHandler.sendQuery(new String[]{"sendMessage", credentialContainer.getUsername(), credentialContainer.getPassword(), recipients.getSelectionModel().getSelectedItem().getUserID(), subject.getText(), message.getText()});
            close();
        } else {
            JFXAlert alert = new JFXAlert<>(((Stage) message.getScene().getWindow()));
            JFXDialogLayout layout = new JFXDialogLayout();
            layout.setBody(new Label("Please fill out all the fields"));
            alert.setOverlayClose(true);
            alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
            alert.setContent(layout);
            alert.initModality(Modality.NONE);
            alert.showAndWait();
        }
    }

}