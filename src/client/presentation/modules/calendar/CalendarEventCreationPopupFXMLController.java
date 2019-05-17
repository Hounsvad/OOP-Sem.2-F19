/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.calendar;

import client.presentation.modules.Popup;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javafx.fxml.FXML;

/**
 * FXML Controller class
 *
 * @author Sanitas Solutions
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

            condition.await();
            return entry;
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.err);
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
