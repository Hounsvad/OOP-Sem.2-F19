/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
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
     * The Button leading to the Calendar
     */
    @FXML
    private Button buttonCalendar;
    
    /**
     * The Button leading to the Journal
     */
    @FXML
    private Button buttonJournal;

    /**
     * The TextButton leading to the Dashboard
     */
    @FXML
    private Button buttonDashboardLabel;
    
    /**
     * The TextButton leading to the Calandar
     */
    @FXML
    private Button buttonCalendarLabel;
    
    /**
     * The TextButton leading to the Journal
     */
    @FXML
    private Button buttonJournalLabel;
    

    /**
     * Initializes the controller class
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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


        buttonDashboard.setOnAction((e) -> loadSubScene("modules/dashboard/DashboardFXML.fxml"));
        buttonCalendar.setOnAction((e) -> loadSubScene("modules/calendar/CalendarFXML.fxml"));
        buttonJournal.setOnAction((e) -> loadSubScene("modules/journal/JournalFXML.fxml"));
        buttonDashboardLabel.setOnAction((e) -> loadSubScene("modules/dashboard/DashboardFXML.fxml"));
        buttonCalendarLabel.setOnAction((e) -> loadSubScene("modules/calendar/CalendarFXML.fxml"));
        buttonJournalLabel.setOnAction((e) -> loadSubScene("modules/journal/JournalFXML.fxml"));
        loadSubScene("modules/dashboard/DashboardFXML.fxml");
    }

    /**
     * Loads the Subscene
     * @param path The path to the Subscene
     */
    public void loadSubScene(String path) {
        try {
            subScene.setRoot(FXMLLoader.load(getClass().getResource(path)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
