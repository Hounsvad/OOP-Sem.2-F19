/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation.modules.calendar;

import com.calendarfx.view.page.WeekPage;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
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
        WeekPage dayView = new WeekPage();
        //dayView.setManaged(true);

        anchorPane.getChildren().add(dayView);
        dayView.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        Thread t = new Thread(() -> {
            while (true) {
                dayView.setPrefSize(940 > anchorPane.getWidth() ? 940 : anchorPane.getWidth(), 520 > anchorPane.getHeight() ? 520 : anchorPane.getHeight());
                dayView.resize(940 > anchorPane.getWidth() ? 940 : anchorPane.getWidth(), 520 > anchorPane.getHeight() ? 520 : anchorPane.getHeight());
            }
        });
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();

    }

}
