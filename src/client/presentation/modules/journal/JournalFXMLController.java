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
     * The list view for the AutomaticEntries
     */
    @FXML
    private JFXListView<LogEntry> automaticEntriesView;
    /**
     * The list view for the MedicinalEntries
     */
    @FXML
    private JFXListView<MedicinalEntry> medicinalEntriesView;
    /**
     * The list view for the ManualEntries
     */
    @FXML
    private JFXListView<ManualEntry> manualEntriesView;
    /**
     * The list view for the Patients
     */
    @FXML
    private JFXListView<Patient> PatientView;
    /**
     * the anchor pane used as a button to add a MedicinalEntry
     */
    @FXML
    private AnchorPane addMedicinalEntryButton;
    /**
     * The anchor pane used as a button to add a ManualEntry
     */
    @FXML
    private AnchorPane addManualEntryButton;

    /**
     * Initializes the controller class
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PatientView.getSelectionModel().selectionModeProperty().addListener((observable) -> {
            getPatient((Patient) PatientView.getSelectionModel().getSelectedItem());
        });
        //CommunicationHandler.getInstance().sendQuery();
    }
    
    /**
     * Adds a {@link MedicinalEntry}
     * @param event 
     */
    @FXML
    private void addMedicinalEntry(MouseEvent event) {
    }

    /**
     * adds a {@link ManualEntry}
     * @param event 
     */
    @FXML
    private void addManualEntry(MouseEvent event) {
    }
    
    private void getPatient(Patient patient) {

    }

}
