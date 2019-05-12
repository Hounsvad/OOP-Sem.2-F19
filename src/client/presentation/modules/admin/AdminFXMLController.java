/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.admin;

import client.presentation.CommunicationHandler;
import client.presentation.containers.Department;
import client.presentation.containers.Patient;
import client.presentation.containers.Role;
import client.presentation.containers.User;
import client.presentation.modules.Module;
import client.presentation.utils.credentials.CredentialContainer;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
public class AdminFXMLController extends Module {

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
    private JFXTextField newUserFullName;
    @FXML
    private JFXComboBox<Department> newUserDepartment;
    @FXML
    private JFXListView<User> UserView;

    private static ChangeListener changeListener;

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
                        populateDepartmentLists();
                        populateRolesList();

                    });
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();

        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        changeListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> a, Boolean b, Boolean c) {
                if (b == true && c == false) {
                    clearAll();
                } else if (b == false && c == true) {
                    updateData();
                }
            }
        };

        //Make sure that there is only one active listener from this class
        CredentialContainer.getInstance().getCredentialReadyProperty().removeListener(changeListener);
        CredentialContainer.getInstance().getCredentialReadyProperty().addListener(changeListener);
    }

    @FXML
    private void departmentPicked(ActionEvent event) {
        populateUserList();
        populatePatientList();
    }

    @FXML
    private void userSelected(MouseEvent event) {
        updateUserDetailFields();
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
        updateUserDetailFields();
    }

    @FXML
    private void addNewClicked(MouseEvent event) {
        communicationHandler.sendQuery("addUser", newUserUsername.getText(), newUserFullName.getText(), newUserDepartment.getSelectionModel().getSelectedItem().getDepartmentId());
        populateUserList();
    }

    @FXML
    private void addNewPatientClicked(MouseEvent event) {
        new Thread(() -> {
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
        }).start();

    }

    private void populateDepartmentLists() {
        new Thread(() -> {
            Platform.runLater(() -> {
                //clear data
                departmentPicker.getItems().clear();
                userDepartment.getItems().clear();
                newUserDepartment.getItems().clear();
                //get data and populate main department picker
                CommunicationHandler.getInstance().sendQuery("getDepartments").forEach(t -> departmentPicker.getItems().add(new Department(t[0], t[1])));
                //populate data
                departmentPicker.getSelectionModel().selectFirst();
                userDepartment.getItems().addAll(departmentPicker.getItems());
                userDepartment.getSelectionModel().selectFirst();
                newUserDepartment.getItems().addAll(departmentPicker.getItems());
                newUserDepartment.getSelectionModel().selectFirst();
                populateUserList();
                populatePatientList();
            });
        }).start();

    }

    private void populateUserList() {
        clearFields();
        new Thread(() -> {
            Platform.runLater(() -> {
                UserView.getItems().clear();
                CommunicationHandler.getInstance().sendQuery("userListByDepartment", departmentPicker.getSelectionModel().getSelectedItem().getDepartmentId()).forEach(t -> UserView.getItems().add(new User(t[0], t[1], t[2])));
            });
        }).start();

    }

    protected void populatePatientList() {
        new Thread(() -> {
            Platform.runLater(() -> {
                assignmentView.getItems().clear();
                CommunicationHandler.getInstance().sendQuery("getPatientsByDepartment", departmentPicker.getSelectionModel().getSelectedItem().getDepartmentId()).forEach(t -> assignmentView.getItems().add(new Patient(t[0], t[1])));
            });
        }).start();

    }

    private void populateRolesList() {
        new Thread(() -> {
            Platform.runLater(() -> {
                roleView.getItems().clear();
                List<Role> l1 = CommunicationHandler.getInstance().sendQuery("getRoles").stream().map(t -> new Role(t[0], t[1])).collect(Collectors.toList());
                roleView.getItems().addAll(l1);
                Collections.sort(roleView.getItems());
            });
        }).start();

    }

    protected void updatePatientAssignments() {
        new Thread(() -> {
            Platform.runLater(() -> {
                assignmentView.getSelectionModel().clearSelection();
                int[] indecies = CommunicationHandler.getInstance().sendQuery("getPatientsByUser", UserView.getSelectionModel().getSelectedItem().getUserID()).stream().map((t) -> assignmentView.getItems().indexOf(new Patient(t[1], t[0]))).collect(Collectors.toList()).stream().mapToInt(i -> i).toArray();
                if (indecies.length > 0) {
                    assignmentView.getSelectionModel().selectIndices(indecies[0], indecies);
                }
            });
        }).start();

    }

    private void updateRoleAssignments() {
        new Thread(() -> {
            Platform.runLater(() -> {
                roleView.getSelectionModel().clearSelection();
                int[] indecies = CommunicationHandler.getInstance().sendQuery("getUserRoles", UserView.getSelectionModel().getSelectedItem().getUserID()).stream().map((t) -> roleView.getItems().indexOf(new Role(t[0], "No description"))).collect(Collectors.toList()).stream().mapToInt(i -> i).toArray();
                if (indecies.length > 0) {
                    roleView.getSelectionModel().selectIndices(indecies[0], indecies);
                }
            });
        }).start();

    }

    private void updateUserDetailFields() {
        userId.setText(UserView.getSelectionModel().getSelectedItem().getUserID());
        userUsername.setText(UserView.getSelectionModel().getSelectedItem().getUsername());
        userName.setText(UserView.getSelectionModel().getSelectedItem().getUserFullName());
        updateRoleAssignments();
        updatePatientAssignments();
    }

    private void clearFields() {
        userId.setText("");
        userName.setText("");
        userUsername.setText("");

        //clears data about a new user
        newUserFullName.setText("");
        newUserUsername.setText("");
    }

    @Override
    protected void clearAll() {
        //clears department pickers
        departmentPicker.getItems().clear();
        userDepartment.getItems().clear();
        newUserDepartment.getItems().clear();

        //clears user details
        clearFields();
    }

    @Override
    public void updateData() {
        populateDepartmentLists();
    }

}
