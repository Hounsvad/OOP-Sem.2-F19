/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.admin;

import client.presentation.CommunicationHandler;
import client.presentation.containers.Department;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Oliver
 */
public class AdminFXMLController implements Initializable {

    @FXML
    private JFXComboBox<Department> departmentPicker;
    @FXML
    private FontAwesomeIconView saveAssignments;
    @FXML
    private JFXListView<?> assignmentView;
    @FXML
    private FontAwesomeIconView saveRoles;
    @FXML
    private JFXListView<?> roleView;
    @FXML
    private FontAwesomeIconView saveDetails;
    @FXML
    private JFXTextField userId;
    @FXML
    private JFXTextField userUsername;
    @FXML
    private JFXTextField userName;
    @FXML
    private JFXComboBox<?> userDepartment;
    @FXML
    private FontAwesomeIconView addNew;
    @FXML
    private JFXTextField newUserUsername;
    @FXML
    private JFXTextField newName;
    @FXML
    private JFXComboBox<?> newUserDepartment;
    @FXML
    private JFXListView<?> UserView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CommunicationHandler.getInstance().sendQuery("")
    }

    @FXML
    private void departmentPicked(ActionEvent event) {

    }

    @FXML
    private void userSelected(MouseEvent event) {
    }

    @FXML
    private void saveAssignmentsClicked(MouseEvent event) {
    }

    @FXML
    private void saveRolesClicked(MouseEvent event) {
    }

    @FXML
    private void saveDetailsClicked(MouseEvent event) {
    }

    @FXML
    private void addNewClicked(MouseEvent event) {
    }

}
