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
     * The icon for the cross that exits the program
     */
    @FXML
    private FontAwesomeIconView cross;
    
    /**
     * The title of the ActivityEntry
     */
    @FXML
    private Label title;
    
    /**
     * The description of the the ActivityEntry
     */
    @FXML
    private Label description;
    
    /**
     * The label for the query
     */
    @FXML
    private Label query;
    
    /**
     * The date of the ActivityEntry
     */
    @FXML
    private Label date;
    
    /**
     * The label for the ip
     */
    @FXML
    private Label ip;

    /**
     * Initilizes the controller class
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
     * Set the data for the ActivityEntry popup
     * @param titleString the title for the entry
     * @param descriptionString the description of the entry
     * @param queryString the query of the entry???
     * @param dateString the date of the entry
     * @param ipString  the ip of the entry
     */
    public void setData(String titleString, String descriptionString, String queryString, String dateString, String ipString) {
        title.setText(titleString);
        description.setText(descriptionString);
        query.setText(queryString);
        date.setText(dateString);
        ip.setText(ipString);
    }

}
