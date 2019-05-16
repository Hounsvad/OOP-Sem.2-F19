/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.calendar;

import client.presentation.modules.Popup;
import client.presentation.modules.dashboard.MessageEntry;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Oliver
 */
public class CalendarEventCreationPopupFXMLController extends Popup {

    @FXML
    private JFXComboBox<?> participents;
    @FXML
    private JFXTextField subject;
    @FXML
    private JFXTextArea message;

    private Entry<String> entry = null;

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public Entry<String> createEvent() {
        lock.lock();
        try {
            Platform.runLater(() -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CalendarCreationPopupFXML.fxml"));
                    fxmlLoader.setController(this);
                    Parent root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initStyle(StageStyle.UNDECORATED);
                    root.getStylesheets().add(MessageEntry.class.getResource("/client/presentation/css/generalStyleSheet.css").toExternalForm());
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            condition.await();
            return entry;
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void create() {
        lock.lock();
        try {
            entry = new Entry<>("title", new Interval(LocalDateTime.MIN, LocalDateTime.MAX));
            condition.signal();
            close();
        } finally {
            lock.unlock();
        }
    }

    @FXML
    private void cancel() {
        lock.lock();
        try {
            entry = null;
            condition.signal();
            close();
        } finally {
            lock.unlock();
        }
    }

}
