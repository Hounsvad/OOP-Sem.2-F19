/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation;

import client.presentation.modules.Module;
import client.presentation.utils.credentials.CredentialContainer;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Sanitas Solutions
 */
public class MainFXMLController implements Initializable {

    @FXML
    private SubScene subScene;

    private final CommunicationHandler communicationHandler = CommunicationHandler.getInstance();

    private Module module;

    @FXML
    private VBox menu;

    @FXML
    private GridPane menuGrid;

    @FXML
    private Button buttonDashboard;

    @FXML
    private Button buttonDashboardLabel;

    @FXML
    private AnchorPane root;

    @FXML
    private GridPane menuGrid1;

    /**
     * Initializes the controller class
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        communicationHandler.setMainFXMLController(this);

        //Configuring the side drawer-menu
        menu.prefHeightProperty().bind(((AnchorPane) menu.getParent()).heightProperty());
        subScene.heightProperty().bind(menu.heightProperty());
        ((AnchorPane) subScene.getParent()).widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                subScene.setWidth((double) newValue - 60);
            }
        });
        TranslateTransition menuTranslation = new TranslateTransition(Duration.millis(500), menu);
        menuTranslation.setFromX(-140);
        menuTranslation.setToX(0);
        menu.setOnMouseEntered(evt -> {
            menuTranslation.setRate(1);
            menuTranslation.play();
        });
        menu.setOnMouseExited(evt -> {
            menuTranslation.setRate(-1);
            menuTranslation.play();
        });
        addMenuItems(communicationHandler.sendQuery(new String[]{"getMenuItems", CredentialContainer.getInstance().getUsername(), CredentialContainer.getInstance().getPassword()}));
        loadSubScene("modules/dashboard/DashboardFXML.fxml");
        root.requestFocus();
        root.setOnKeyReleased(t -> {
            if (t.getCode().equals(KeyCode.F5)) {
                module.updateData();
            }
        });
    }

    /**
     * Loads the Subscene
     *
     * @param path The path to the Subscene
     */
    public void loadSubScene(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            subScene.setRoot(loader.load());
            module = loader.getController();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    private void addMenuItems(List<String[]> input) {
        //Bind Action for standard items
        buttonDashboard.setOnAction((e) -> loadSubScene("modules/dashboard/DashboardFXML.fxml"));
        buttonDashboardLabel.setOnAction((e) -> loadSubScene("modules/dashboard/DashboardFXML.fxml"));

        for (String[] tuple : input) {
            //Create Label
            Button label = new Button();
            label.setPrefWidth(140);
            label.setPrefHeight(60);
            label.setMaxWidth(Double.MAX_VALUE);
            label.setMaxHeight(Double.MAX_VALUE);
            label.setAlignment(Pos.CENTER_LEFT);
            label.setGraphic(new Label(tuple[0]));
            ((Label) label.getGraphic()).setFont(((Label) buttonDashboardLabel.getGraphic()).getFont());
            menuGrid.getChildren().add(label);
            GridPane.setColumnIndex(label, 0);
            GridPane.setRowIndex(label, input.indexOf(tuple) + 1);

            //Create icon
            Button icon = new Button();
            icon.setPrefWidth(60);
            icon.setPrefHeight(60);
            icon.setMaxWidth(Double.MAX_VALUE);
            icon.setMaxHeight(Double.MAX_VALUE);
            icon.setAlignment(Pos.CENTER);
            icon.setGraphic(new FontAwesomeIconView());
            ((FontAwesomeIconView) icon.getGraphic()).glyphNameProperty().set(tuple[1]);
            ((FontAwesomeIconView) icon.getGraphic()).glyphSizeProperty().set(40);
            menuGrid.getChildren().add(icon);
            GridPane.setColumnIndex(icon, 1);
            GridPane.setRowIndex(icon, input.indexOf(tuple) + 1);

            //Bind Action
            label.setOnAction((e) -> loadSubScene(tuple[2]));
            icon.setOnAction((e) -> loadSubScene(tuple[2]));
        }
    }

    /**
     * Sets the mouse pointer to be a spinner or sets it to the default cursor
     *
     * @param state the state for whether or not the spinner should be enabled
     *              for the mouse pointer
     */
    public void setSpinner(boolean state) {
        subScene.setCursor(state ? Cursor.WAIT : Cursor.DEFAULT);
    }

    @FXML
    private void openSettings(ActionEvent event) {
        System.out.println("openSettings");
        new Thread(() -> {
            Platform.runLater(()
                    -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SettingsPopupFXML.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initStyle(StageStyle.UNDECORATED);
                    root.getStylesheets().add(getClass().getResource("/client/presentation/css/generalStyleSheet.css").toExternalForm());
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                }
            });
        }, "SettingsWindowLoader").start();
    }
}
