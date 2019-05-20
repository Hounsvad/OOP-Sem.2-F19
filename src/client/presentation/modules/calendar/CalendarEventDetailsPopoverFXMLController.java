/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.modules.calendar;

import client.presentation.modules.Popup;
import com.calendarfx.view.DateControl.EntryDetailsPopOverContentParameter;
import java.net.URL;
import java.time.ZoneOffset;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Sanitas Solutions
 */
public class CalendarEventDetailsPopoverFXMLController extends Popup {

    @FXML
    private Label title;
    @FXML
    private Label description;
    @FXML
    private Label dateStart;
    @FXML
    private Label dateEnd;

    private /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setData(EntryDetailsPopOverContentParameter event) {
        title.setText(event.getEntry().getTitle());
        description.setText(event.getEntry().getLocation());
        event.getEntry().getEndAsLocalDateTime().atOffset(ZoneOffset.systemDefault());
    }

}
