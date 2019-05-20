/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.modules.calendar;

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
public class CalendarEventDetailsPopoverFXMLController extends Popup {

    @FXML
    private Label title;
    @FXML
    private Label description;
    @FXML
    private Label dateStart;
    @FXML
    private Label dateEnd;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
