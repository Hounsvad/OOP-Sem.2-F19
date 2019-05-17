/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.admin;

import client.presentation.CommunicationHandler;
import client.presentation.containers.Department;
import client.presentation.modules.Popup;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Sanitas Solutions
 */
public class PatientCreationPopupFXMLController extends Popup implements Initializable {

    @FXML
    private FontAwesomeIconView cross;
    @FXML
    private JFXComboBox<Department> department;
    @FXML
    private JFXTextField name;

    private AdminFXMLController adminController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CommunicationHandler.getInstance().sendQuery("getDepartments").forEach(t -> department.getItems().add(new Department(t[0], t[1])));
    }

    /**
     *
     * @param c is the controller that should be updated after the addition of
     *          the new user
     */
    protected void setAdminController(AdminFXMLController c) {
        adminController = c;
    }

    /**
     * Closes the popup
     */
    @FXML
    @Override
    public void close() {
        ((Stage) cross.getScene().getWindow()).close();
    }

    /**
     * creates the user
     *
     * @param event
     */
    @FXML
    private void create(ActionEvent event) {
        CommunicationHandler.getInstance().sendQuery("addPatient", name.getText(), department.getSelectionModel().getSelectedItem().getDepartmentId());
        adminController.populatePatientList();
        adminController.updatePatientAssignments();
        close();
    }

}
