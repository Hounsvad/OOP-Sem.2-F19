/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.journal;

import client.presentation.containers.Patient;
import client.presentation.modules.Popup;
import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Sanitas Solutions
 */
public class MedicalEntryCreationPopupFXMLController extends Popup {

    @FXML
    private JFXComboBox<String> medicin;
    @FXML
    private JFXTextField dosage;
    @FXML
    private JFXTextArea notes;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        medicin.getItems().addAll("Parcetamol", "Ibuprofen", "Morfin", "Antihestamin");
    }

    @FXML
    private void send() {
        if (medicin.getSelectionModel().getSelectedItem() != null && !dosage.getText().isEmpty() && !notes.getText().isEmpty()) {
            communicationHandler.sendQuery("addJournalEntry", getPatient().getPatientID(), "medicinal", String.join(";:;", new String[]{medicin.getSelectionModel().getSelectedItem(), dosage.getText(), notes.getText()}));
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    getModuleController().updateData();
                }
            });
            close();
        } else {
            JFXAlert alert = new JFXAlert<>(((Stage) notes.getScene().getWindow()));
            JFXDialogLayout layout = new JFXDialogLayout();
            layout.setBody(new Label("Please fill out all the fields"));
            alert.setOverlayClose(true);
            alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
            alert.setContent(layout);
            alert.initModality(Modality.NONE);
            alert.showAndWait();
        }
    }

    private Patient getPatient() {
        return ((JournalFXMLController) getModuleController()).getPatient();
    }

}
