/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.journal;

import client.presentation.modules.Popup;
import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
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
public class ManualEntryCreationPopupFXMLController extends Popup {

    @FXML
    private JFXTextArea notes;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void send() {
        if (!notes.getText().isEmpty()) {
            communicationHandler.sendQuery(new String[]{});
            close();
        } else {
            JFXAlert alert = new JFXAlert<>(((Stage) notes.getScene().getWindow()));
            JFXDialogLayout layout = new JFXDialogLayout();
            layout.setBody(new Label("Please fill out the field"));
            alert.setOverlayClose(true);
            alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
            alert.setContent(layout);
            alert.initModality(Modality.NONE);
            alert.showAndWait();
        }
    }
}
