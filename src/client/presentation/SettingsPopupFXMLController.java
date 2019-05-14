/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation;

import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hounsvad
 */
public class SettingsPopupFXMLController implements Initializable {

    @FXML
    private FontAwesomeIconView cross;
    @FXML
    private JFXTextField password;
    @FXML
    private JFXTextField repeatPassword;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Do nothing
        //Note: https://stackoverflow.com/questions/48345922/reference-password-validation
        //Propper password rules^^^
    }

    @FXML
    private void close() {
        ((Stage) cross.getScene().getWindow()).close();
    }

    @FXML
    private void saveOwnPassword(ActionEvent event) {
        if (validatePassword()) {
            CommunicationHandler.getInstance().sendQuery("alterOwnPassword", com.google.common.hash.Hashing.sha256().hashString(password.getText(), Charset.forName("UTF-8")).toString());
        } else {
            //give user indication of invalid password
        }
    }

    @FXML
    private void updateFields(KeyEvent event) {
        //implement visual ques on password strengths and equality between repeatPassword and password
    }

    private boolean validatePassword() {
        //implement logig for minimum password requirements
        return false;
    }

}
