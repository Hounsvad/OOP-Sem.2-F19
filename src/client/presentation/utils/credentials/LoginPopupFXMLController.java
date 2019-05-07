/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.utils.credentials;

import client.presentation.CommunicationHandler;
import client.presentation.utils.CustomDecorator;
import client.presentation.utils.StringUtils;
import com.google.common.hash.Hashing;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.svg.SVGGlyph;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Hounsvad
 */
public class LoginPopupFXMLController implements Initializable {

    /**
     * The offset on the x axis
     */
    private double xOffset = 0;
    /**
     * The offset on the y axis
     */
    private double yOffset = 0;

    /**
     * The CommunicationHandler
     */
    private final CommunicationHandler communicationHandler = CommunicationHandler.getInstance();

    /**
     * The text field for the username
     */
    @FXML
    private JFXTextField username;
    
    /**
     * The text field for the password
     */
    @FXML
    private JFXPasswordField password;

    /**
     * The label for the message if the password/username is wrong
     */
    @FXML
    private Label message;
    
    /**
     * The pane for the loading animation
     */
    @FXML
    private Pane loadpane;
    
    /**
     * The image view for the blurred login screen
     */
    @FXML
    private ImageView loadblur;

    /**
     * The Credential instance
     */
    private CredentialContainer containerInstance;

    /**
     * Initializes the controller class
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadpane.setVisible(false);
        loadblur.setEffect(new GaussianBlur(5));
        username.textProperty().addListener((observable, oldValue, newValue)
                -> {
            if (!newValue.isEmpty() && username.getStyleClass().contains("wrong-credentials")) {
                username.getStyleClass().remove("wrong-credentials");
                password.getStyleClass().remove("wrong-credentials");
                message.setText("");
            }
        });

        password.textProperty().addListener((observable, oldValue, newValue)
                -> {
            if (!newValue.isEmpty() && password.getStyleClass().contains("wrong-credentials")) {
                username.getStyleClass().remove("wrong-credentials");
                password.getStyleClass().remove("wrong-credentials");
                message.setText("");
            }
        });
        containerInstance = CredentialContainer.getInstance();
    }
    
    /**
     * Cheeky way around the login - DELETE IN FINAL VERSION
     * @param event 
     */
    @FXML
    private void skip(ContextMenuEvent event) {
        loadMain();
        closeStage();
    }
    
    /**
     * Handles the login
     * Opens the main program if your login was successful
     * Tells you if your login failed
     */
    @FXML
    private void handleLoginButtonAction() {
        loadpane.setVisible(true);
        new Thread(() -> {
            List<String[]> sqlReturn = CommunicationHandler.getInstance().sendQuery(new String[]{"login", username.getText(), StringUtils.hash(password.getText())});
            if (sqlReturn != null && !sqlReturn.isEmpty()) {
                CredentialContainer.getInstance().setUsername(username.getText());
                CredentialContainer.getInstance().setPassword(StringUtils.hash(password.getText()));
                Platform.runLater(() -> {
                    if (containerInstance.isFirst()) {
                        loadMain();
                    }
                    closeStage();
                });
                
            } else if (username.getText().isEmpty() || password.getText().isEmpty()) {
                Platform.runLater(()
                        -> {
                    message.setText("Please enter a username and a password!");
                    username.getStyleClass().add("wrong-credentials");
                    password.getStyleClass().add("wrong-credentials");
                });

            } else {
                Platform.runLater(()
                        -> {
                    message.setText("Wrong username or password!");
                    password.setText("");
                    username.getStyleClass().add("wrong-credentials");
                    password.getStyleClass().add("wrong-credentials");
                });
                
            }
            Platform.runLater(()
                    -> {
                loadpane.setVisible(false);
            });
        }).start();
    }
    
    /**
     * Closes the application when cancelling the login
     */
    @FXML
    private void handleCancelButtonAction() {
        System.exit(0);
    }
    
    /**
     * Hashes the input
     * @param input The string that gets hashed
     * @return the hashed input
     */
    private String hash(String input) {
        return Hashing.sha256().hashString(input, Charset.forName("UTF8")).toString();
    }
    
    /**
     * Closes the stage
     */
    private void closeStage() {
        ((Stage) username.getScene().getWindow()).close();
    }
    
    /**
     * Initializes the main application screen
     */
    private void loadMain() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/client/presentation/MainFXML.fxml"));
            CustomDecorator decorator = new CustomDecorator(stage, root, false, true, true);
            decorator.setCustomMaximize(true);
            decorator.setGraphic(new SVGGlyph());
            Scene scene = new Scene(decorator);
            scene.getStylesheets().add(getClass().getResource("/client/presentation/css/generalStyleSheet.css").toExternalForm());
            stage.setScene(scene);
            stage.setMinHeight(845);
            stage.setMinWidth(1290);
            stage.show();
            stage.setTitle("Sanitas Overview");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
