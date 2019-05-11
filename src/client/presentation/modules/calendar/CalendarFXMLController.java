/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation.modules.calendar;

import client.presentation.CommunicationHandler;
import client.presentation.containers.Patient;
import client.presentation.utils.credentials.CredentialContainer;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.DayViewBase;
import com.calendarfx.view.DetailedWeekView;
import com.calendarfx.view.YearMonthView;
import com.calendarfx.view.YearMonthView.DateCell;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Oliver
 */
public class CalendarFXMLController implements Initializable {

    @FXML
    private JFXListView<Patient> patientView;
    @FXML
    private AnchorPane calendarPane;
    @FXML
    private AnchorPane datePickerPane;

    CommunicationHandler communicationHandler = CommunicationHandler.getInstance();
    CredentialContainer credentialContainer = CredentialContainer.getInstance();

    private static List<Patient> patientsCache;
    private static Map<Integer, Calendar> calendarsCache;

    DetailedWeekView detailedWeekView = new DetailedWeekView();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Create the main calendar view
        detailedWeekView.earlyLateHoursStrategyProperty().set(DayViewBase.EarlyLateHoursStrategy.SHOW_COMPRESSED);
        detailedWeekView.weekFieldsProperty().set(WeekFields.ISO);
        detailedWeekView.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        //Add calendar to the designated calendar pane
        calendarPane.getChildren().add(detailedWeekView);

        //A fix to make the calendar scale properly with the pane
        calendarPane.heightProperty().addListener((a, b, c) -> {
            detailedWeekView.setPrefSize(940 > calendarPane.getWidth() ? 940 : calendarPane.getWidth(), 420 > calendarPane.getHeight() ? 420 : calendarPane.getHeight());
            detailedWeekView.resize(940 > calendarPane.getWidth() ? 940 : calendarPane.getWidth(), 420 > calendarPane.getHeight() ? 420 : calendarPane.getHeight());
        });
        calendarPane.widthProperty().addListener((a, b, c) -> {
            detailedWeekView.setPrefSize(940 > calendarPane.getWidth() ? 940 : calendarPane.getWidth(), 420 > calendarPane.getHeight() ? 420 : calendarPane.getHeight());
            detailedWeekView.resize(940 > calendarPane.getWidth() ? 940 : calendarPane.getWidth(), 420 > calendarPane.getHeight() ? 420 : calendarPane.getHeight());
        });

        //Create the calendar below the main calendar used to pick a date
        YearMonthView yearMonthView = new YearMonthView();
        yearMonthView.getStylesheets().add(getClass().getResource("/client/presentation/css/calendarStyleSheet.css").toExternalForm());
        yearMonthView.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        //Add the second, datepicker, calendar to the relevant pane
        datePickerPane.getChildren().add(yearMonthView);

        //A fix to make the calendar scale properly with the pane, here with fixed height
        datePickerPane.widthProperty().addListener((a, b, c) -> {
            yearMonthView.setPrefSize(1020 > datePickerPane.getWidth() ? 1020 : datePickerPane.getWidth(), 300);
            yearMonthView.resize(1020 > datePickerPane.getWidth() ? 1020 : datePickerPane.getWidth(), 300);
        });

        //Create a fix to the datepicker calendar not selecting the day when clicked
        yearMonthView.setClickBehaviour(YearMonthView.ClickBehaviour.PERFORM_SELECTION);
        yearMonthView.setOnMouseClicked(selectedObject -> {
            if (selectedObject.getPickResult().getIntersectedNode() instanceof DateCell) {
                yearMonthView.setDate(((DateCell) selectedObject.getPickResult().getIntersectedNode()).getDate());
            } else if (selectedObject.getPickResult().getIntersectedNode() instanceof Text) {
                yearMonthView.setDate(((DateCell) selectedObject.getPickResult().getIntersectedNode().getParent()).getDate());
            }
        });

        //Binding the datepicker calendar to the main calendar
        detailedWeekView.bind(yearMonthView, true);

        updateData();
    }

    @FXML
    private void getCalendarEvents() {
        //Avoid null pointer errors
        if (patientView == null || patientView.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        //Insert the cached date, if available
        if (calendarsCache != null && calendarsCache.containsKey(patientView.getSelectionModel().getSelectedIndex())) {
            CalendarSource calendarSource = new CalendarSource();
            calendarSource.getCalendars().add(calendarsCache.get(patientView.getSelectionModel().getSelectedIndex()));
            detailedWeekView.getCalendarSources().clear();
            detailedWeekView.getCalendarSources().add(calendarSource);
        }

        //Find the previous Updater thread, if any and kill it
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals("CalendarUpdater")) {
                t.interrupt();
            }
        }

        //New thread to get and update the values and store the new values in a cache
        new Thread(() -> {
            try {
                Calendar calendar = new Calendar();
                //Get new values
                try {
                    communicationHandler.sendQuery(new String[]{
                        "getCalendar",
                        credentialContainer.getUsername(),
                        credentialContainer.getPassword(),
                        patientView.getSelectionModel().getSelectedItem().getPatientID(),
                        Long.toString(toMillis(detailedWeekView.getStartDate(), detailedWeekView.getStartTime())),
                        Long.toString(toMillis(detailedWeekView.getEndDate(), detailedWeekView.getEndTime()))})
                            .forEach(tuple -> {
                                Entry entry = new Entry<String>(
                                        tuple[3],
                                        new Interval(LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(tuple[1])), ZoneId.systemDefault()),
                                                LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(tuple[4])), ZoneId.systemDefault())
                                        ));
                                entry.setId(tuple[0]);
                                entry.setLocation(tuple[4]);
                                calendar.addEntry(entry);
                                //test
                            }
                            );
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Update the values to the FX elements and update the cache
                Platform.runLater(() -> {
                    try {
                        CalendarSource calendarSource = new CalendarSource();
                        calendarSource.getCalendars().add(calendar);
                        detailedWeekView.getCalendarSources().clear();
                        detailedWeekView.getCalendarSources().add(calendarSource);

                        if (calendarsCache == null) {
                            calendarsCache = new HashMap<>();
                        }
                        calendarsCache.put(patientView.getSelectionModel().getSelectedIndex(), calendar);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception ex) {
            }
        }, "CalendarUpdater").
                start();
    }

    private void getPatients() {
        //Insert the cached date, if available
        if (patientsCache != null) {
            patientView.getItems().clear();
            patientView.getItems().addAll(patientsCache);
        }

        //Find the previous Updater thread, if any and kill it
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals("CalendarPatientUpdater")) {
                t.interrupt();
            }
        }

        //New thread to get and update the values and store the new values in a cache
        new Thread(() -> {
            try {
                List<Patient> patients = new ArrayList<>();

                //Get new values
                try {
                    communicationHandler.sendQuery(new String[]{"getPatients", credentialContainer.getUsername(), credentialContainer.getPassword()}).forEach((tuple) -> patients.add(new Patient(tuple[1], tuple[0])));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Update the values to the FX elements and update the cache
                Platform.runLater(() -> {
                    try {
                        patientView.getItems().clear();
                        patientView.getItems().addAll(patients);

                        patientsCache = new ArrayList<>(patients);

                        //If nothing is selected, select the first element
                        if (patientView.getSelectionModel().getSelectedItem() == null) {
                            patientView.getSelectionModel().select(0);
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception ex) {
            }
        }, "CalendarPatientUpdater").
                start();
    }

    public void updateData() {
        getPatients();
        getCalendarEvents();
    }

    private void clearAll() {
        patientsCache = null;
        calendarsCache = null;
        Platform.runLater(() -> {
            patientView.getItems().clear();
            detailedWeekView.getCalendars().clear();
        });
    }

    private long toMillis(LocalDate date, LocalTime time) {
        return date.atTime(time).toEpochSecond(ZoneId.systemDefault().getRules().getOffset(Instant.now())) * 1000;
    }

}
