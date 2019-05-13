/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Oliver
 */
public class MainFXMLController implements Initializable {

    /**
     * The SubScene of the stage
     */
    @FXML
    private SubScene subScene;

    private Module module;

    /**
     * The VBox for the Menu
     */
    @FXML
    private VBox menu;

    /**
     * The GridPane for the menu
     */
    @FXML
    private GridPane menuGrid;

    /**
     * The Button leading to the Dashboard
     */
    @FXML
    private Button buttonDashboard;

    /**
     * The TextButton leading to the Dashboard
     */
    @FXML
    private Button buttonDashboardLabel;

    /**
     * The TextButton leading to the Calandar
     */
    @FXML
    private AnchorPane root;

    /**
     * Initializes the controller class
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        CommunicationHandler.getInstance().setMainFXMLController(this);

        //Side-menu
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

        addMenuItems(CommunicationHandler.getInstance().sendQuery(new String[]{"getMenuItems", CredentialContainer.getInstance().getUsername(), CredentialContainer.getInstance().getPassword()}));
        loadSubScene("modules/dashboard/DashboardFXML.fxml");
        root.requestFocus();
        root.setOnKeyReleased(t -> {
            if (t.getCode().equals(KeyCode.F5)) {
                System.out.println("Hit F5");
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
            ex.printStackTrace();
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
     *
     * @param state
     */
    public void setSpinner(boolean state) {
        subScene.setCursor(state ? Cursor.WAIT : Cursor.DEFAULT);
    }
}
