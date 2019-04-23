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

    private double xOffset = 0;
    private double yOffset = 0;

    private final CommunicationHandler communicationHandler = CommunicationHandler.getInstance();

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private Label message;
    @FXML
    private Pane loadpane;
    @FXML
    private ImageView loadblur;

    private CredentialContainer containerInstance;

    /**
     * Initializes the controller class.
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

    @FXML
    private void skip(ContextMenuEvent event) {
        loadMain();
        closeStage();
    }

    @FXML
    private void handleLoginButtonAction() {
        loadpane.setVisible(true);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
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
                    System.err.println("Please enter a username and a password!");
                    Platform.runLater(()
                            -> {
                        message.setText("Please enter a username and a password!");
                        username.getStyleClass().add("wrong-credentials");
                        password.getStyleClass().add("wrong-credentials");
                    });

                } else {
                    System.err.println("Wrong username or password!");
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

            }
        });
        t.start();
    }

    @FXML
    private void handleCancelButtonAction() {
        System.exit(0);
    }

    private String hash(String input) {
        return Hashing.sha256().hashString(input, Charset.forName("UTF8")).toString();
    }

    private void closeStage() {
        ((Stage) username.getScene().getWindow()).close();
    }

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
            stage.setMinHeight(870);
            stage.setMinWidth(1200);
            stage.show();
            stage.setTitle("Sanitas Overview");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
