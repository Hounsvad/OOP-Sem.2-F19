/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.admin;

import client.presentation.CommunicationHandler;
import client.presentation.containers.Department;
import client.presentation.containers.Patient;
import client.presentation.containers.Role;
import client.presentation.containers.User;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
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
    private JFXListView<Patient> assignmentView;
    @FXML
    private FontAwesomeIconView saveRoles;
    @FXML
    private JFXListView<Role> roleView;
    @FXML
    private FontAwesomeIconView saveDetails;
    @FXML
    private JFXTextField userId;
    @FXML
    private JFXTextField userUsername;
    @FXML
    private JFXTextField userName;
    @FXML
    private JFXComboBox<Department> userDepartment;
    @FXML
    private FontAwesomeIconView addNew;
    @FXML
    private JFXTextField newUserUsername;
    @FXML
    private JFXTextField newName;
    @FXML
    private JFXComboBox<Department> newUserDepartment;
    @FXML
    private JFXListView<User> UserView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            UserView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            assignmentView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            //Get department and populate
            CommunicationHandler.getInstance().sendQuery("getDepartments").forEach(t -> departmentPicker.getItems().add(new Department(t[0], t[1])));
            departmentPicker.getSelectionModel().selectFirst();
            userDepartment.getItems().addAll(departmentPicker.getItems());
            userDepartment.getSelectionModel().selectFirst();
            newUserDepartment.getItems().addAll(departmentPicker.getItems());
            newUserDepartment.getSelectionModel().selectFirst();
            //update info based on first user
            populateUserList();
            //update stored patients
            populatePatientList();
            updatePatientAssignments();
            populateRolesList();

        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void departmentPicked(ActionEvent event) {
        populateUserList();
    }

    @FXML
    private void userSelected(MouseEvent event) {
        updatePatientAssignments();
    }

    @FXML
    private void saveAssignmentsClicked(MouseEvent event
    ) {
    }

    @FXML
    private void saveRolesClicked(MouseEvent event
    ) {
    }

    @FXML
    private void saveDetailsClicked(MouseEvent event
    ) {
    }

    @FXML
    private void addNewClicked(MouseEvent event
    ) {
    }

    private void populateUserList() {
        CommunicationHandler.getInstance().sendQuery("userListByDepartment", departmentPicker.getSelectionModel().getSelectedItem().getDepartmentId()).forEach(t -> UserView.getItems().add(new User(t[0], t[1], t[2])));
        UserView.getSelectionModel().selectFirst();
        updateFields();
    }

    private void populatePatientList() {
        CommunicationHandler.getInstance().sendQuery("getPatientsByDepartment", departmentPicker.getSelectionModel().getSelectedItem().getDepartmentId()).forEach(t -> assignmentView.getItems().add(new Patient(t[0], t[1])));
    }

    private void populateRolesList() {
        CommunicationHandler.getInstance().sendQuery("getRoles").forEach(t -> roleView.getItems().add(new Role(t[0], t[1])));
    }

    private void updatePatientAssignments() {
        List<Integer> assignedPatients = CommunicationHandler.getInstance().sendQuery("getPatients", UserView.getSelectionModel().getSelectedItem().getUserID()).stream().map((t) -> assignmentView.getItems().indexOf(new Patient(t[0], t[1]))).collect(Collectors.toList());
        int[] assignedPatientsArray = new int[assignedPatients.size()];
        for (int i = 0; i < assignedPatientsArray.length; i++) {

        }
        assignmentView.getSelectionModel().selectIndices(assignedPatientsArray.length, assignedPatientsArray);
    }

    private void updateFields() {

    }

}
