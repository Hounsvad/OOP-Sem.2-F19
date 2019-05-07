/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation.modules.dashboard;

import client.presentation.modules.Popup;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Oliver
 */
public class MessageEntryPopupFXMLController extends Popup {

    /**
     * The icon for the cross that exits the programm
     */
    @FXML
    private FontAwesomeIconView cross;
    
    /**
     * The title of the MessageEntry
     */
    @FXML
    private Label title;
    
    /**
     * The message of the the MessageEntry
     */
    @FXML
    private Label message;
    
    /**
     * The date of the MessageEntry
     */
    @FXML
    private Label date;

    /**
     * Initializes the controller class
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Closes the popup
     * @param event 
     */
    @FXML
    private void close(MouseEvent event) {
        ((Stage) cross.getScene().getWindow()).close();
    }

    /**
     * Sets the data of the MessageEntry popup
     * @param titleString
     * @param messageString
     * @param dateString
     */
    public void setData(String titleString, String messageString, String dateString) {
        title.setText(titleString);
        message.setText(messageString);
        date.setText(dateString);
    }
    
}
