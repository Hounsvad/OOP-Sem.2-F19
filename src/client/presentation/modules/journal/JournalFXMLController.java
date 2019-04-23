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
    
    /**
     * The list view for the activity feed about log entries
     */
    @FXML
    private JFXListView<LogEntry> automaticEntriesView;
    /**
     * The list view for the activity feed about medicinal entries
     */
    @FXML
    private JFXListView<MedicinalEntry> medicinalEntriesView;
    /**
     * The list view for the activity feed about manual entries
     */
    @FXML
    private JFXListView<ManualEntry> manualEntriesView;
    /**
     * The list view for the activity feed about the patients
     */
    @FXML
    private JFXListView<Patient> PatientView;
    /**
     * the anchor pane used as a button to add medicinal entry
     */
    @FXML
    private AnchorPane addMedicinalEntryButton;
    /**
     * The anchor pane used as a button to add a manual entry
     */
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
    
    /**
     * Adds a medicinal entry
     * @param event 
     */
    @FXML
    private void addMedicinalEntry(MouseEvent event) {
    }

    /**
     * adds a manual entry
     * @param event 
     */
    @FXML
    private void addManualEntry(MouseEvent event) {
    }
    
    private void getPatient(Patient patient) {

    }

}
