/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation.modules.calendar;

import com.calendarfx.view.DayViewBase;
import com.calendarfx.view.DetailedWeekView;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

/**
 * FXML Controller class
 *
 * @author Oliver
 */
public class CalendarFXMLController implements Initializable {

    @FXML
    private JFXListView<?> PatientView;
    @FXML
    private AnchorPane anchorPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DetailedWeekView detailedWeekView = new DetailedWeekView();
        detailedWeekView.earlyLateHoursStrategyProperty().set(DayViewBase.EarlyLateHoursStrategy.SHOW_COMPRESSED);
        detailedWeekView.createCalendarSource();
        detailedWeekView.createEntryAt(ZonedDateTime.now());

        anchorPane.getChildren().add(detailedWeekView);
        detailedWeekView.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        anchorPane.heightProperty().addListener((a, b, c) -> {
            detailedWeekView.setPrefSize(940 > anchorPane.getWidth() ? 940 : anchorPane.getWidth(), 520 > anchorPane.getHeight() ? 520 : anchorPane.getHeight());
            detailedWeekView.resize(940 > anchorPane.getWidth() ? 940 : anchorPane.getWidth(), 520 > anchorPane.getHeight() ? 520 : anchorPane.getHeight());
        });
        anchorPane.widthProperty().addListener((a, b, c) -> {
            detailedWeekView.setPrefSize(940 > anchorPane.getWidth() ? 940 : anchorPane.getWidth(), 520 > anchorPane.getHeight() ? 520 : anchorPane.getHeight());
            detailedWeekView.resize(940 > anchorPane.getWidth() ? 940 : anchorPane.getWidth(), 520 > anchorPane.getHeight() ? 520 : anchorPane.getHeight());
        });

    }

}
