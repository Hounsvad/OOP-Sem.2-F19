/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation.modules.journal;

import client.presentation.modules.Popup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author nadinfariss
 */
public class ManualEntryShowMessagePopupFXMLController extends Popup {

    @FXML
    private FontAwesomeIconView cross;
    @FXML
    private JFXTextField notes;
    @FXML
    private JFXButton closeButton;
    @FXML
    private Label date;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void close(MouseEvent event) {
        close();
    }

    @FXML
    private void closeButton(ActionEvent event) {
        ((Stage) closeButton.getScene().getWindow()).close();
    }

    public void setData(String notes, String dateString) {
        this.notes.setText(notes);
        date.setText(dateString);
    }
}
