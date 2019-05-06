/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.journal;

import client.presentation.CommunicationHandler;
import client.presentation.containers.Patient;
import client.presentation.utils.credentials.CredentialContainer;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    private Patient currentPatient;
    private final CommunicationHandler communicationHandler = CommunicationHandler.getInstance();
    private final CredentialContainer credentialContainer = CredentialContainer.getInstance();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateData();
    }

    @FXML
    private void addMedicinalEntry(MouseEvent event) {
        MedicinalEntry.showCreationPopup();
    }

    @FXML
    private void addManualEntry(MouseEvent event) {
        ManualEntry.showCreationPopup();
    }

    private void getPatient(Patient patient) {
        this.currentPatient = patient;
    }

    private void updateData() {
        if (currentPatient == null) {
            return;
        }
        try {
            List<LogEntry> logEntries = new ArrayList<>();
            communicationHandler.sendQuery(new String[]{"getActivity", credentialContainer.getUsername(), credentialContainer.getPassword()}).forEach((tuple) -> logEntries.add(new LogEntry()));
            automaticEntriesView.getItems().addAll(logEntries);

            List<MedicinalEntry> medicalEntries = new ArrayList<>();
            communicationHandler.sendQuery(new String[]{"getActivity", credentialContainer.getUsername(), credentialContainer.getPassword()}).forEach((tuple) -> medicalEntries.add(new MedicinalEntry()));
            medicinalEntriesView.getItems().addAll(medicalEntries);

            List<ManualEntry> manualEntries = new ArrayList<>();
            communicationHandler.sendQuery(new String[]{"getActivity", credentialContainer.getUsername(), credentialContainer.getPassword()}).forEach((tuple) -> manualEntries.add(new ManualEntry()));
            manualEntriesView.getItems().addAll(manualEntries);

            List<Patient> patients = new ArrayList<>();
            communicationHandler.sendQuery(new String[]{"getPatients", credentialContainer.getUsername(), credentialContainer.getPassword()}).forEach((tuple) -> patients.add(new Patient(tuple[1], tuple[0])));
            PatientView.getItems().addAll(patients);
        } catch (NullPointerException e) {
        }
    }
}
