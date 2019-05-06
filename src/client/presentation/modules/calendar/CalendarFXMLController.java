/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation.modules.calendar;

import client.presentation.CommunicationHandler;
import client.presentation.containers.Patient;
import client.presentation.utils.credentials.CredentialContainer;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.DayViewBase;
import com.calendarfx.view.DetailedWeekView;
import com.calendarfx.view.YearMonthView;
import com.calendarfx.view.YearMonthView.DateCell;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
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
    private JFXListView<Patient> PatientView;
    @FXML
    private AnchorPane calendarPane;
    @FXML
    private AnchorPane datePickerPane;

    CommunicationHandler communicationHandler = CommunicationHandler.getInstance();
    CredentialContainer credentialContainer = CredentialContainer.getInstance();

    DetailedWeekView detailedWeekView = new DetailedWeekView();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //communicationHandler.sendQuery(new String[]{"getPatients", credentialContainer.getUsername(), credentialContainer.getPassword()}).forEach(tuple -> PatientView.getItems().add(new Patient(tuple[1], tuple[0])));
        detailedWeekView.earlyLateHoursStrategyProperty().set(DayViewBase.EarlyLateHoursStrategy.SHOW_COMPRESSED);
        detailedWeekView.createCalendarSource();
        detailedWeekView.createEntryAt(ZonedDateTime.now());
        detailedWeekView.weekFieldsProperty().set(WeekFields.ISO);

        calendarPane.getChildren().add(detailedWeekView);
        detailedWeekView.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        calendarPane.heightProperty().addListener((a, b, c) -> {
            detailedWeekView.setPrefSize(940 > calendarPane.getWidth() ? 940 : calendarPane.getWidth(), 420 > calendarPane.getHeight() ? 420 : calendarPane.getHeight());
            detailedWeekView.resize(940 > calendarPane.getWidth() ? 940 : calendarPane.getWidth(), 420 > calendarPane.getHeight() ? 420 : calendarPane.getHeight());
        });
        calendarPane.widthProperty().addListener((a, b, c) -> {
            detailedWeekView.setPrefSize(940 > calendarPane.getWidth() ? 940 : calendarPane.getWidth(), 420 > calendarPane.getHeight() ? 420 : calendarPane.getHeight());
            detailedWeekView.resize(940 > calendarPane.getWidth() ? 940 : calendarPane.getWidth(), 420 > calendarPane.getHeight() ? 420 : calendarPane.getHeight());
        });

        YearMonthView yearMonthView = new YearMonthView();
        yearMonthView.getStylesheets().add(getClass().getResource("/client/presentation/css/calendarStyleSheet.css").toExternalForm());
        datePickerPane.getChildren().add(yearMonthView);
        yearMonthView.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        datePickerPane.widthProperty().addListener((a, b, c) -> {
            yearMonthView.setPrefSize(1020 > datePickerPane.getWidth() ? 1020 : datePickerPane.getWidth(), 300);
            yearMonthView.resize(1020 > datePickerPane.getWidth() ? 1020 : datePickerPane.getWidth(), 300);
        });

        yearMonthView.setClickBehaviour(YearMonthView.ClickBehaviour.PERFORM_SELECTION);
        yearMonthView.setOnMouseClicked(selectedObject -> {
            if (selectedObject.getPickResult().getIntersectedNode() instanceof DateCell) {
                yearMonthView.setDate(((DateCell) selectedObject.getPickResult().getIntersectedNode()).getDate());
            } else {
                yearMonthView.setDate(((DateCell) selectedObject.getPickResult().getIntersectedNode().getParent()).getDate());
            }
        });
        detailedWeekView.bind(yearMonthView, true);
        getCalendarEvents();

    }

    @FXML
    private void getCalendarEvents() {
        if (PatientView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        communicationHandler.sendQuery(new String[]{
            "getPatients",
            credentialContainer.getUsername(),
            credentialContainer.getPassword(),
            PatientView.getSelectionModel().getSelectedItem().getPatientID(),
            Long.toString(detailedWeekView.getDate().minusDays(detailedWeekView.getDate().getDayOfWeek().getValue() - 1).toEpochDay() * 1000),
            Long.toString(detailedWeekView.getDate().plusDays(7 - detailedWeekView.getDate().getDayOfWeek().getValue()).toEpochDay() * 1000)})
                .forEach(tuple -> {
                    Entry entry = new Entry<String>(
                            tuple[3],
                            new Interval(LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(tuple[1])), ZoneId.systemDefault()),
                                    LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(tuple[2])), ZoneId.systemDefault())
                            ));
                    entry.setId(tuple[0]);
                    entry.setLocation(tuple[4]);
                    detailedWeekView.getCalendars().get(0).addEntry(entry);
                }
                );
    }

}
