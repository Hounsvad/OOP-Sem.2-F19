/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.journal;

import client.presentation.CommunicationHandler;
import client.presentation.containers.Patient;
import client.presentation.modules.Module;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Hounsvad
 */
public class JournalFXMLController extends Module {

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
    protected JFXListView<Patient> patientView;

    private final CommunicationHandler communicationHandler = CommunicationHandler.getInstance();
    @FXML
    private AnchorPane addMedicinalEntryButton;
    @FXML
    private AnchorPane addManualEntryButton;

    /**
     * Initializes the controller class
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateData();
    }

    /**
     * Adds a {@link MedicinalEntry}
     *
     * @param event
     */
    @FXML
    private void addMedicinalEntry() {
        MedicinalEntry.showCreationPopup(null);
    }

    /**
     * adds a {@link ManualEntry}
     *
     * @param event
     */
    @FXML
    private void addManualEntry() {
        ManualEntry.showCreationPopup(this);
    }

    protected Patient getPatient() {
        return patientView.getSelectionModel().getSelectedItem();
    }

    /**
     *
     */
    public void updateData() {
        clearAll();
//            List<LogEntry> logEntries = new ArrayList<>();
//            communicationHandler.sendQuery(new String[]{"getActivity", credentialContainer.getUsername(), credentialContainer.getPassword()}).forEach((tuple) -> logEntries.add(new LogEntry()));
//            automaticEntriesView.getItems().addAll(logEntries);
//
//            List<MedicinalEntry> medicalEntries = new ArrayList<>();
//            communicationHandler.sendQuery("getActivity").forEach((tuple) -> medicalEntries.add(new MedicinalEntry()));
//            medicinalEntriesView.getItems().addAll(medicalEntries);
//

        List<Patient> patients = new ArrayList<>();
        communicationHandler.sendQuery("getPatients").forEach((tuple) -> patients.add(new Patient(tuple[1], tuple[0])));
        patientView.getItems().addAll(patients);
        patientView.getSelectionModel().selectFirst();
        updateEntryDate();
    }

    /**
     *
     */
    @Override
    protected void clearAll() {
        manualEntriesView.getItems().clear();
        medicinalEntriesView.getItems().clear();
        patientView.getItems().clear();
    }

    @FXML
    private void medicinalSelected(MouseEvent event) {
    }

    @FXML
    private void manualSelected(MouseEvent event) {

    }

    @FXML
    private void patientSelected(MouseEvent event) {

    }

    private void updateEntryDate() {
        List<ManualEntry> manualEntries = new ArrayList<>();
        communicationHandler.sendQuery("getJournal", getPatient().getPatientID()).forEach((tuple) -> {
            Arrays.toString(tuple);
//            manualEntries.add(new ManualEntry(tuple[1], tuple[0], tuple[2]));
        });
        manualEntriesView.getItems().addAll(manualEntries);
    }

}
