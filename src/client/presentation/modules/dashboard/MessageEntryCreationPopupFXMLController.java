/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.dashboard;

import client.presentation.CommunicationHandler;
import client.presentation.utils.User;
import client.presentation.utils.credentials.CredentialContainer;
import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Oliver
 */
public class MessageEntryCreationPopupFXMLController implements Initializable {

    @FXML
    private FontAwesomeIconView cross;
    @FXML
    private JFXComboBox<User> recipients;
    @FXML
    private JFXTextField subject;
    @FXML
    private JFXTextArea message;

    CommunicationHandler communicationHandler = CommunicationHandler.getInstance();
    CredentialContainer credentialContainer = CredentialContainer.getInstance();

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
    private void close() {
        ((Stage) cross.getScene().getWindow()).close();
    }

    @FXML
    private void send() {
        if (recipients.getSelectionModel().getSelectedItem() != null && !subject.getText().isEmpty() && !message.getText().isEmpty()) {
            communicationHandler.sendQuery(new String[]{"sendMessage", credentialContainer.getUsername(), credentialContainer.getPassword(), recipients.getSelectionModel().getSelectedItem().getUserID(), subject.getText(), message.getText()});
            close();
        } else {
            JFXAlert alert = new JFXAlert<>(((Stage) message.getScene().getWindow()));
            System.out.println("post");
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
