/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation.modules.dashboard;

import client.presentation.modules.Popup;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Sanitas Solutions
 */
public class ActivityEntryPopupFXMLController extends Popup {

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
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Set the data for the ActivityEntry popup
     *
     * @param titleString       the title for the entry
     * @param descriptionString the description of the entry
     * @param dateString        the date of the entry
     * @param ipString          the ip of the entry
     */
    public void setData(String titleString, String descriptionString, String dateString, String ipString) {
        title.setText(titleString);
        description.setText(descriptionString);
        date.setText(dateString);
        ip.setText(ipString);
    }

}
