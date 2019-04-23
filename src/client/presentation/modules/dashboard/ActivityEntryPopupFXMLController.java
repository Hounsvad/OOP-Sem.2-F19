/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation.modules.dashboard;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Oliver
 */
public class ActivityEntryPopupFXMLController implements Initializable {

    /**
     * A placeholder for the FontAwesomeIconView icons
     */
    @FXML
    private FontAwesomeIconView cross;
    
    /**
     * The title of the activity entry
     */
    @FXML
    private Label title;
    
    /**
     * The description of the the activity entry
     */
    @FXML
    private Label description;
    
    /**
     * the label for the query
     */
    @FXML
    private Label query;
    
    /**
     * The date of the activity entry
     */
    @FXML
    private Label date;
    
    /**
     * The label for the ip
     */
    @FXML
    private Label ip;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    /**
     * Closes popup
     * @param event 
     */
    @FXML
    private void close(MouseEvent event) {
        ((Stage) cross.getScene().getWindow()).close();
    }
    
    /**
     * Set the Data for the activity entry
     * @param titleString
     * @param descriptionString
     * @param queryString
     * @param dateString
     * @param ipString 
     */
    public void setData(String titleString, String descriptionString, String queryString, String dateString, String ipString) {
        title.setText(titleString);
        description.setText(descriptionString);
        query.setText(queryString);
        date.setText(dateString);
        ip.setText(ipString);
    }

}
