/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.journal;

import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Hounsvad
 */
public class JournalFXMLController implements Initializable {

    @FXML
    private JFXListView<LogEntry> automaticEntriesView;
    @FXML
    private JFXListView<MedicinalEntry> medicinalEntriesView;
    @FXML
    private JFXListView<ManualEntry> manualEntriesView;
    @FXML
    private JFXListView<Patient> PatientView;
    @FXML
    private AnchorPane addMedicinalEntryButton;
    @FXML
    private AnchorPane addManualEntryButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PatientView.getSelectionModel().selectionModeProperty().addListener((observable) -> {
            getPatient((Patient) PatientView.getSelectionModel().getSelectedItem());
        });
        //CommunicationHandler.getInstance().sendQuery();
    }

    @FXML
    private void addMedicinalEntry(MouseEvent event) {
    }

    @FXML
    private void addManualEntry(MouseEvent event) {
    }

    private void getPatient(Patient patient) {

    }

}
