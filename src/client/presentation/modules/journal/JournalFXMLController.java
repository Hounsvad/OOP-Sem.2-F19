/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.journal;

import client.presentation.CommunicationHandler;
import client.presentation.containers.Patient;
import client.presentation.modules.Module;
import client.presentation.utils.credentials.CredentialContainer;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Hounsvad
 */
public class JournalFXMLController extends Module {

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

    private Patient currentPatient;
    private final CommunicationHandler communicationHandler = CommunicationHandler.getInstance();
    private final CredentialContainer credentialContainer = CredentialContainer.getInstance();
    @FXML
    private JFXComboBox<?> departmentPicker;
    @FXML
    private JFXListView<?> UserView1;
    @FXML
    private JFXListView<?> UserView11;
    @FXML
    private AnchorPane addUserButton;
    @FXML
    private JFXListView<?> UserView;

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
        MedicinalEntry.showCreationPopup();
    }

    /**
     * adds a {@link ManualEntry}
     *
     * @param event
     */
    @FXML
    private void addManualEntry() {
        ManualEntry.showCreationPopup();
    }

    private void getPatient(Patient patient) {
        this.currentPatient = patient;
    }

    /**
     *
     */
    public void updateData() {
        if (currentPatient == null) {
            return;
        }
        try {
//            List<LogEntry> logEntries = new ArrayList<>();
//            communicationHandler.sendQuery(new String[]{"getActivity", credentialContainer.getUsername(), credentialContainer.getPassword()}).forEach((tuple) -> logEntries.add(new LogEntry()));
//            automaticEntriesView.getItems().addAll(logEntries);
//
//            List<MedicinalEntry> medicalEntries = new ArrayList<>();
//            communicationHandler.sendQuery(new String[]{"getActivity", credentialContainer.getUsername(), credentialContainer.getPassword()}).forEach((tuple) -> medicalEntries.add(new MedicinalEntry()));
//            medicinalEntriesView.getItems().addAll(medicalEntries);
//
//            List<ManualEntry> manualEntries = new ArrayList<>();
//            communicationHandler.sendQuery(new String[]{"getActivity", credentialContainer.getUsername(), credentialContainer.getPassword()}).forEach((tuple) -> manualEntries.add(new ManualEntry()));
//            manualEntriesView.getItems().addAll(manualEntries);
//
//            List<Patient> patients = new ArrayList<>();
//            communicationHandler.sendQuery(new String[]{"getPatients", credentialContainer.getUsername(), credentialContainer.getPassword()}).forEach((tuple) -> patients.add(new Patient(tuple[1], tuple[0])));
//            PatientView.getItems().addAll(patients);
        } catch (NullPointerException e) {
        }
    }

    /**
     *
     */
    @Override
    protected void clearAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
