/* 
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.modules.dashboard;

import static client.presentation.utils.StringUtils.getBoldString;
import java.io.IOException;
import java.util.Date;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Sanitas Solutions
 */
public class ActivityEntry {

    private final String typeOfEntry;
    private final String dateOfEntryString;
    private final String specificsOfEntry;
    private final String ip;

    /**
     *
     * @param typeOfEntry
     * @param dateOfEntry
     * @param specificsOfEntry
     * @param ip
     */
    public ActivityEntry(String typeOfEntry, Date dateOfEntry, String specificsOfEntry, String ip) {
        this.typeOfEntry = typeOfEntry;
        this.dateOfEntryString = String.format("%1$" + 2 + "s", dateOfEntry.getHours()).replace(' ', '0') + ":"
                + String.format("%1$" + 2 + "s", dateOfEntry.getMinutes()).replace(' ', '0') + ":"
                + String.format("%1$" + 2 + "s", dateOfEntry.getSeconds()).replace(' ', '0') + " "
                + String.format("%1$" + 2 + "s", dateOfEntry.getDate()).replace(' ', '0') + "/"
                + String.format("%1$" + 2 + "s", dateOfEntry.getMonth()).replace(' ', '0') + "/"
                + (dateOfEntry.getYear() + 1900);
        this.specificsOfEntry = specificsOfEntry;
        this.ip = ip;
    }

    @Override
    public String toString() {
        return getBoldString(typeOfEntry) + "\n" + ip + "\n" + dateOfEntryString;
    }

    /**
     *
     */
    public void showPopup() {
        Platform.runLater(()
                -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ActivityEntryPopupFXML.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                ActivityEntryPopupFXMLController controller = fxmlLoader.<ActivityEntryPopupFXMLController>getController();
                controller.setData(typeOfEntry, specificsOfEntry, dateOfEntryString, ip);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);
                root.getStylesheets().add(MessageEntry.class.getResource("/client/presentation/css/generalStyleSheet.css").toExternalForm());
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
            }
        });
    }
}
