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
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
            //Set selection modes
            UserView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            assignmentView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            roleView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            //Get department and populate
            new Thread(() -> {
                try {
                    Thread.sleep(600);
                    Platform.runLater(() -> {
                        CommunicationHandler.getInstance().sendQuery("getDepartments").forEach(t -> departmentPicker.getItems().add(new Department(t[0], t[1])));
                        departmentPicker.getSelectionModel().selectFirst();
                        userDepartment.getItems().addAll(departmentPicker.getItems());
                        userDepartment.getSelectionModel().selectFirst();
                        newUserDepartment.getItems().addAll(departmentPicker.getItems());
                        newUserDepartment.getSelectionModel().selectFirst();
                        populateUserList();
                        populateRolesList();
                        populatePatientList();
                    });
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();

        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void departmentPicked(ActionEvent event) {
        populateUserList();
        populatePatientList();
    }

    @FXML
    private void userSelected(MouseEvent event) {
        updateFields();
        updatePatientAssignments();
        updateRoleAssignments();
    }

    @FXML
    private void saveAssignmentsClicked(MouseEvent event) {
        List<Patient> assignedPatitents = assignmentView.getSelectionModel().getSelectedItems();
        String[] query = new String[assignedPatitents.size() + 2];
        query[0] = "updatePatientAssignment";
        query[1] = UserView.getSelectionModel().getSelectedItem().getUserID();
        for (int i = 2; i < query.length; i++) {
            query[i] = assignedPatitents.get(i - 2).getPatientID();
        }
        CommunicationHandler.getInstance().sendQuery(query);
    }

    @FXML
    private void saveRolesClicked(MouseEvent event) {
        List<Role> assignedRoles = roleView.getSelectionModel().getSelectedItems();
        String[] query = new String[assignedRoles.size() + 2];
        query[0] = "setUserRoles";
        query[1] = UserView.getSelectionModel().getSelectedItem().getUserID();
        for (int i = 2; i < query.length; i++) {
            query[i] = assignedRoles.get(i - 2).getRoleId();
        }
        CommunicationHandler.getInstance().sendQuery(query);
    }

    @FXML
    private void saveDetailsClicked(MouseEvent event) {
        if (userDepartment.getSelectionModel().getSelectedItem() != departmentPicker.getSelectionModel().getSelectedItem()) {
            CommunicationHandler.getInstance().sendQuery("setUserDepartment", UserView.getSelectionModel().getSelectedItem().getUserID(), userDepartment.getSelectionModel().getSelectedItem().getDepartmentId());
        }
        clearFields();
        updateFields();
    }

    @FXML
    private void addNewClicked(MouseEvent event) {

    }

    @FXML
    private void addNewPatientClicked(MouseEvent event) {
        Platform.runLater(()
                -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PatientCreationPopupFXML.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);
                root.getStylesheets().add(getClass().getResource("/client/presentation/css/generalStyleSheet.css").toExternalForm());
                stage.setScene(new Scene(root));
                ((PatientCreationPopupFXMLController) fxmlLoader.getController()).setAdminController(this);
                stage.show();
            } catch (IOException e) {
            }

        });
    }

    private void populateUserList() {
        clearFields();
        Platform.runLater(() -> {
            UserView.getItems().clear();
            CommunicationHandler.getInstance().sendQuery("userListByDepartment", departmentPicker.getSelectionModel().getSelectedItem().getDepartmentId()).forEach(t -> UserView.getItems().add(new User(t[0], t[1], t[2])));
        });
    }

    private void updateFields() {
        userId.setText(UserView.getSelectionModel().getSelectedItem().getUserID());
        userUsername.setText(UserView.getSelectionModel().getSelectedItem().getUsername());
        userName.setText(UserView.getSelectionModel().getSelectedItem().getUserFullName());
        updateRoleAssignments();
        updatePatientAssignments();
    }

    private void clearFields() {
        userId.setText("");
        userUsername.setText("");
        userName.setText("");
    }

    protected void populatePatientList() {
        Platform.runLater(() -> {
            assignmentView.getItems().clear();
            CommunicationHandler.getInstance().sendQuery("getPatientsByDepartment", departmentPicker.getSelectionModel().getSelectedItem().getDepartmentId()).forEach(t -> assignmentView.getItems().add(new Patient(t[0], t[1])));
        });
    }

    protected void updatePatientAssignments() {
        Platform.runLater(() -> {
            assignmentView.getSelectionModel().clearSelection();
            int[] indecies = CommunicationHandler.getInstance().sendQuery("getPatientsByUser", UserView.getSelectionModel().getSelectedItem().getUserID()).stream().map((t) -> assignmentView.getItems().indexOf(new Patient(t[1], t[0]))).collect(Collectors.toList()).stream().mapToInt(i -> i).toArray();
            if (indecies.length > 0) {
                assignmentView.getSelectionModel().selectIndices(indecies[0], indecies);
            }
        });
    }

    private void populateRolesList() {
        Platform.runLater(() -> {
            roleView.getItems().clear();
            List<Role> l1 = CommunicationHandler.getInstance().sendQuery("getRoles").stream().map(t -> new Role(t[0], t[1])).collect(Collectors.toList());
            roleView.getItems().addAll(l1);
            Collections.sort(roleView.getItems());
        });
    }

    private void updateRoleAssignments() {
        Platform.runLater(() -> {
            roleView.getSelectionModel().clearSelection();
            int[] indecies = CommunicationHandler.getInstance().sendQuery("getUserRoles", UserView.getSelectionModel().getSelectedItem().getUserID()).stream().map((t) -> roleView.getItems().indexOf(new Role(t[0], "No description"))).collect(Collectors.toList()).stream().mapToInt(i -> i).toArray();
            if (indecies.length > 0) {
                roleView.getSelectionModel().selectIndices(indecies[0], indecies);
            }
        });
    }

}
