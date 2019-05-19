/* 
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.modules.calendar;

import client.presentation.containers.Patient;
import client.presentation.modules.Module;
import client.presentation.containers.MessageEntry;
import client.presentation.utils.credentials.CredentialContainer;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.DateControl;
import com.calendarfx.view.DateControl.EntryContextMenuParameter;
import com.calendarfx.view.DateControl.EntryDetailsPopOverContentParameter;
import com.calendarfx.view.DayViewBase;
import com.calendarfx.view.DetailedWeekView;
import com.calendarfx.view.YearMonthView;
import com.calendarfx.view.YearMonthView.DateCell;
import com.jfoenix.controls.JFXListView;
import java.io.IOException;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Sanitas Solutions
 */
public class CalendarFXMLController extends Module {

    @FXML
    private JFXListView<Patient> patientView;
    @FXML
    private AnchorPane calendarPane;
    @FXML
    private AnchorPane datePickerPane;

    private static List<Patient> patientsCache;
    private static Map<CalendarCacheKey, Calendar> calendarsCache;
    private static Map<String, Calendar> dayRythmCache;
    private static ChangeListener changeListener;

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
        detailedWeekView.setEntryEditPolicy(param -> param.getEditOperation().equals(DateControl.EditOperation.DELETE));
        detailedWeekView.setEntryDetailsPopOverContentCallback(param -> calendarDetailsPopup(param));
        detailedWeekView.setEntryContextMenuCallback(param -> calendarEntryContextMenu(param));
        detailedWeekView.setContextMenuCallback(param -> calendarContextMenu());
        detailedWeekView.setEntryFactory(param -> createCalendarEntry());

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
            getCalendarEvents();
        });

        //Binding the datepicker calendar to the main calendar
        detailedWeekView.bind(yearMonthView, true);

        //Create changeListener to delete cached data upon credential invalidation
        changeListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> a, Boolean b, Boolean c) {
                if (b == true && c == false) {
                    clearAll();
                } else if (b == false && c == true) {
                    updateData();
                }
            }
        };

        //Make sure that there is only one active listener from this class
        CredentialContainer.getInstance().getCredentialReadyProperty().removeListener(changeListener);
        CredentialContainer.getInstance().getCredentialReadyProperty().addListener(changeListener);

        updateData();
    }

    @FXML
    private void getCalendarEvents() {
        //Avoid null pointer errors
        if (patientView == null || patientView.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        //Insert the cached date, if available
        if (calendarsCache != null && calendarsCache.containsKey(new CalendarCacheKey(Long.parseLong(patientView.getSelectionModel().getSelectedItem().getPatientID()), detailedWeekView.getStartDate().toEpochDay()))) {
            CalendarSource calendarSource = new CalendarSource();
            calendarSource.getCalendars().add(calendarsCache.get(new CalendarCacheKey(Long.parseLong(patientView.getSelectionModel().getSelectedItem().getPatientID()), detailedWeekView.getStartDate().toEpochDay())));
            try {
                detailedWeekView.getCalendarSources().remove(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            detailedWeekView.getCalendarSources().add(0, calendarSource);
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
                calendar.setStyle(Calendar.Style.STYLE2);
                //Get new values
                try {
                    communicationHandler.sendQuery(
                            "getCalendar",
                            patientView.getSelectionModel().getSelectedItem().getPatientID(),
                            Long.toString(toMillis(detailedWeekView.getStartDate(), detailedWeekView.getStartTime())),
                            Long.toString(toMillis(detailedWeekView.getEndDate(), detailedWeekView.getEndTime())))
                            .forEach(tuple -> {
                                Entry entry = new Entry<>(
                                        tuple[3],
                                        new Interval(LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(tuple[1])), ZoneId.systemDefault()),
                                                LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(tuple[4])), ZoneId.systemDefault())
                                        ));
                                entry.setId(tuple[0]);
                                entry.setLocation(tuple[4]);
                                calendar.addEntry(entry);
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
                        try {
                            detailedWeekView.getCalendarSources().remove(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        detailedWeekView.getCalendarSources().add(0, calendarSource);

                        if (calendarsCache == null) {
                            calendarsCache = new HashMap<>();
                        }
                        calendarsCache.put(new CalendarCacheKey(Long.parseLong(patientView.getSelectionModel().getSelectedItem().getPatientID()), detailedWeekView.getStartDate().toEpochDay()), calendar);
                        getDayRythm();
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
                    communicationHandler.sendQuery("getPatients").forEach((tuple) -> patients.add(new Patient(tuple[1], tuple[0])));
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
                        getCalendarEvents();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception ex) {
            }
        }, "CalendarPatientUpdater").
                start();
    }

    @Override
    public void updateData() {
        getPatients();
    }

    @Override
    protected void clearAll() {
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

    private Node calendarDetailsPopup(EntryDetailsPopOverContentParameter parameter) {
        if (parameter.getEntry().getCalendar().getName().equalsIgnoreCase("rhythm")) {
            Label label = new Label(parameter.getEntry().getTitle());
            label.setPrefSize(100, 60);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setStyle("-fx-background-color: #2E4057; -fx-text-fill: #048BA8;");
            return label;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CalendarEventDetailsPopoverFXML.fxml"));
            return fxmlLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
        return null;
    }

    private ContextMenu calendarEntryContextMenu(EntryContextMenuParameter parameter) {
        if (parameter.getCalendar().getName().equalsIgnoreCase("rhythm")) {
            return null;
        }
        MenuItem editEvent = new MenuItem("Edit");
        editEvent.setOnAction(param -> openEventEditor());
        return new ContextMenu(editEvent);
    }

    private ContextMenu calendarContextMenu() {
        MenuItem editDayRythm = new MenuItem("Edit Dayrythm");
        editDayRythm.setOnAction(param -> openDayRythmEditor());
        MenuItem createEvent = new MenuItem("Create event");
        createEvent.setOnAction(param -> openEventCreator());
        return new ContextMenu(editDayRythm, createEvent);
    }

    private Entry<String> createCalendarEntry() {
        openEventCreator();
        return null;
    }

    private void openEventEditor() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CalendarEventEditPopupFXML.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            root.getStylesheets().add(MessageEntry.class.getResource("/client/presentation/css/generalStyleSheet.css").toExternalForm());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    private void openDayRythmEditor() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("CalendarDayRythmEditorPopupFXML.fxml"));
            fxmlLoader.setControllerFactory(clazz -> {
                Object controller;
                try {
                    controller = clazz.getConstructor().newInstance();
                } catch (ReflectiveOperationException ex) {
                    throw new RuntimeException(ex);
                }
                if (controller instanceof CalendarDayRythmEditorPopupFXMLController) {
                    ((CalendarDayRythmEditorPopupFXMLController) controller).setModuleController(this);
                }
                return controller;
            });
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            root.getStylesheets().add(MessageEntry.class.getResource("/client/presentation/css/generalStyleSheet.css").toExternalForm());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    private void openEventCreator() {
        if (patientView.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        new Thread(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CalendarEventCreationPopupFXML.fxml"));
            Platform.runLater(() -> {
                try {
                    Parent root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initStyle(StageStyle.UNDECORATED);
                    root.getStylesheets().add(MessageEntry.class.getResource("/client/presentation/css/generalStyleSheet.css").toExternalForm());
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace(System.err);
            }
            Entry<String> entry = fxmlLoader.<CalendarEventCreationPopupFXMLController>getController().createEvent();
            if (entry == null) {
                return;
            }
            detailedWeekView.getCalendars().get(0).addEntry(entry);
            String[] query = new String[4];
            query[0] = "createCalendarEvent";
            query[1] = patientView.getSelectionModel().getSelectedItem().getPatientID();
            System.arraycopy(entryToString(entry), 0, query, 2, 3);
            communicationHandler.sendQuery(query);
            getCalendarEvents();
        }).start();
    }

    private void getDayRythm() {
        //Avoid null pointer errors
        if (patientView == null || patientView.getSelectionModel().getSelectedItem() == null || patientView.getSelectionModel().getSelectedItem().equals("")) {
            return;
        }

        //Insert the cached date, if available
        if (dayRythmCache != null && dayRythmCache.get(patientView.getSelectionModel().getSelectedItem().getPatientID()) != null) {
            try {
                detailedWeekView.getCalendarSources().remove(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            CalendarSource calendarSource = new CalendarSource();
            calendarSource.getCalendars().add(dayRythmCache.get(patientView.getSelectionModel().getSelectedItem().getPatientID()));
            detailedWeekView.getCalendarSources().add(1, calendarSource);
        }

        //Find the previous Updater thread, if any and kill it
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals("CalendarRythmUpdater")) {
                t.interrupt();
            }
        }

        //New thread to get and update the values and store the new values in a cache
        new Thread(() -> {
            try {
                Calendar calendar = new Calendar("rhythm");
                calendar.setStyle(Calendar.Style.STYLE2);
                calendar.setReadOnly(true);
                //Get new values
                try {
                    communicationHandler.sendQuery(
                            "getDayRhythm",
                            patientView.getSelectionModel().getSelectedItem().getPatientID())
                            .forEach(tuple -> {
                                for (int i = 0; i < 7; i++) {
                                    Entry entry = new Entry<>(
                                            tuple[2],
                                            new Interval(detailedWeekView.getStartDate().plusDays(i), LocalTime.of(Integer.valueOf(tuple[0]), 0), detailedWeekView.getStartDate().plusDays(i), LocalTime.of(Integer.valueOf(tuple[0]) + 1, 0)));
                                    calendar.addEntry(entry);
                                }
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
                        try {
                            detailedWeekView.getCalendarSources().remove(1);
                        } catch (Exception e) {
                        }
                        detailedWeekView.getCalendarSources().add(1, calendarSource);
                        if (dayRythmCache == null) {
                            dayRythmCache = new HashMap<>();
                        }
                        dayRythmCache.put(patientView.getSelectionModel().getSelectedItem().getPatientID(), calendar);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                });

            } catch (Exception ex) {
            }
        }, "CalendarRythmUpdater").
                start();
    }

    private String[] entryToString(Entry<String> entry) {
        return new String[]{entry.toString()}; //TO BE CHANGED
    }

    protected String getPatientID() {
        if (patientView == null || patientView.getSelectionModel().getSelectedItem() == null || patientView.getSelectionModel().getSelectedItem().equals("")) {
            return "";
        }
        return patientView.getSelectionModel().getSelectedItem().getPatientID();
    }

}
