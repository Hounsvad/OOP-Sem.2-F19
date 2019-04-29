/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @FXML
    private SubScene subScene;

    @FXML
    private VBox menu;
    @FXML
    private GridPane menuGrid;
    @FXML
    private Button buttonDashboard;
    @FXML
    private Button buttonDashboardLabel;

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

//        buttonCalendar.setOnAction((e) -> loadSubScene("modules/calendar/CalendarFXML.fxml"));
//        buttonJournal.setOnAction((e) -> loadSubScene("modules/journal/JournalFXML.fxml"));
//
//        buttonCalendarLabel.setOnAction((e) -> loadSubScene("modules/calendar/CalendarFXML.fxml"));
//        buttonJournalLabel.setOnAction((e) -> loadSubScene("modules/journal/JournalFXML.fxml"));
        //addMenuItems(CommunicationHandler.getInstance().sendQuery(new String[]{"getMenuItems", CredentialContainer.getInstance().getUsername(), CredentialContainer.getInstance().getPassword()}));
        addMenuItems(new ArrayList<String[]>() {
            {
                add(new String[]{"Calendar", "CALENDAR", "modules/calendar/CalendarFXML.fxml"});
                add(new String[]{"Journal", "FILE_TEXT", "modules/journal/JournalFXML.fxml"});
                add(new String[]{"Admin", "COG", "modules/journal/JournalFXML.fxml"}); //FXML TO BE CHANGED
            }
        });
        loadSubScene("modules/dashboard/DashboardFXML.fxml");
    }

    private void loadSubScene(String path) {
        try {
            subScene.setRoot(FXMLLoader.load(getClass().getResource(path)));
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
}
