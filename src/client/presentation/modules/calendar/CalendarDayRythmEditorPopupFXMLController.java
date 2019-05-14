/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.calendar;

import client.presentation.modules.Popup;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * FXML Controller class
 *
 * @author Oliver
 */
public class CalendarDayRythmEditorPopupFXMLController extends Popup {

    @FXML
    private JFXComboBox<?> participents;
    @FXML
    private JFXTextField subject;
    @FXML
    private JFXTextArea message;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void create(ActionEvent event) {
    }

}
