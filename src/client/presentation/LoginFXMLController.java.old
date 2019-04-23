/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation;

import client.presentation.utils.CustomDecorator;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.svg.SVGGlyph;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Oliver
 */
public class LoginFXMLController implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Label message;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;

    @FXML
    private Pane loadpane;
    @FXML
    private ImageView loadblur;

    public static String uName = null;
    public static String pWord = null;

    private boolean caps = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);

    private final CommunicationHandler communicationHandler = CommunicationHandler.getInstance();

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

        if (caps) {
            message.setText("Caps Lock is on!");
        }

        message.sceneProperty().addListener((observable, oldScene, newScene)
                -> {
            if (oldScene == null && newScene != null) {
                newScene.setOnKeyReleased((javafx.scene.input.KeyEvent event) -> {
                    if (event.getCode() == KeyCode.CAPS) {
                        caps = !caps;
                    }
                    if (caps) {
                        message.setText("Caps Lock is on!");
                    } else {
                        message.setText("");
                    }
                });
            }
        });

    }

    @FXML
    public void capslockCheck() {
        message.getScene().setOnKeyReleased((javafx.scene.input.KeyEvent event) -> {
            if (event.getCode() == KeyCode.CAPS) {
                caps = !caps;
                System.out.println(caps);

            }
            if (caps) {
                message.setText("Caps Lock is on!");
            } else if (message.getText().equals("Caps Lock is on!")) {
                message.setText("");
            }
        });
    }

    @FXML
    private void handleLoginButtonAction() {
        loadpane.setVisible(true);
        Thread t = new Thread(() -> {
            List<String[]> sqlReturn = communicationHandler.sendQuery(new String[]{"login", username.getText(), password.getText()});
            if (sqlReturn != null && !sqlReturn.isEmpty()) {
                uName = username.getText();
                pWord = password.getText();
                Platform.runLater(()
                        -> {
                    loadMain();
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
        });
        t.start();
    }

    @FXML
    private void handleCancelButtonAction() {
        System.exit(0);
    }

    private void closeStage() {
        ((Stage) username.getScene().getWindow()).close();
    }

    void loadMain() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("MainFXML.fxml"));
            CustomDecorator decorator = new CustomDecorator(stage, root, false, true, true);
            decorator.setCustomMaximize(true);
            decorator.setGraphic(new SVGGlyph());
            Scene scene = new Scene(decorator);
            scene.getStylesheets().add(getClass().getResource("css/generalStyleSheet.css").toExternalForm());
            stage.setScene(scene);
            stage.setMinHeight(870);
            stage.setMinWidth(1200);
            stage.show();
            stage.setTitle("Sanitas Overview");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    private void skip(ContextMenuEvent event) {
        loadMain();
        closeStage();
    }

}
