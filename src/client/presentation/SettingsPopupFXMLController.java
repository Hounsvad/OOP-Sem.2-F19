/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation;

import client.presentation.utils.credentials.CredentialContainer;
import com.jfoenix.controls.JFXPasswordField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Hounsvad
 */
public class SettingsPopupFXMLController implements Initializable {

    @FXML
    private FontAwesomeIconView cross;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXPasswordField repeatPassword;

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
        if (validate()) {
            CommunicationHandler.getInstance().sendQuery("alterOwnPassword", com.google.common.hash.Hashing.sha256().hashString(password.getText(), Charset.forName("UTF-8")).toString());
            CredentialContainer.getInstance().openLoginWindow();
            close();
        } else {
            if (validateEqual()) {
                shakePassword();
            } else {
                shakeRepeat();
            }
        }
    }

    @FXML
    private void updatePassword(KeyEvent event) {
        if (validatePassword()) {
            System.out.println("password long");
            if (password.getStyleClass().contains("wrong-credentials")) {
                password.getStyleClass().remove("wrong-credentials");
            }
        } else {
            shakePassword();
        }
    }

    @FXML
    private void updateRepeat(KeyEvent event) {
        if (validateEqual()) {
            System.out.println("passwords equal");
            if (repeatPassword.getStyleClass().contains("wrong-credentials")) {
                repeatPassword.getStyleClass().remove("wrong-credentials");
            }
        } else {
            shakeRepeat();
        }
    }

    private boolean validate() {
        return validatePassword() && validateEqual();
    }

    private boolean validatePassword() {
        return password.getText().length() >= 8;
    }

    private boolean validateEqual() {
        return password.getText().equals(repeatPassword.getText());
    }

    private void shakePassword() {
        if (!password.getStyleClass().contains("wrong-credentials")) {
            password.getStyleClass().add("wrong-credentials");
        }
        TranslateTransition translate = new TranslateTransition(Duration.millis(300), password);
        translate.play();
    }

    private void shakeRepeat() {
        if (!repeatPassword.getStyleClass().contains("wrong-credentials")) {
            repeatPassword.getStyleClass().add("wrong-credentials");
        }
        TranslateTransition translate = new TranslateTransition(Duration.millis(300), repeatPassword);
        translate.play();
    }

}
