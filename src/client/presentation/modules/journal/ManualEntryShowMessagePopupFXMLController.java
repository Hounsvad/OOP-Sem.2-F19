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
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.text.DateFormatter;

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
    private Label dateLabel;

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
        DateFormatter dateFormatter = new DateFormatter(DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM));
        this.notes.setText(notes);
        try {
            dateLabel.setText(dateFormatter.valueToString(new Date(Long.parseLong(dateString))));
        } catch (ParseException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
