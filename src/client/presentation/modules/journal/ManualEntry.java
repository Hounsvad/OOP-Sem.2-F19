/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.journal;

import client.presentation.modules.dashboard.MessageEntry;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.text.DateFormatter;

/**
 *
 * @author Hounsvad
 */
public class ManualEntry {

    private final String date;
    private final String patientId;
    private final String contents;

    public ManualEntry(String date, String patientId, String contents) {
        this.date = date;
        this.patientId = patientId;
        this.contents = contents;
    }

    /**
     *
     * @param moduleController
     */
    public static void showCreationPopup(Initializable moduleController) {
        Platform.runLater(()
                -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(ManualEntry.class.getResource("ManualEntryCreationPopupFXML.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                ((ManualEntryCreationPopupFXMLController) fxmlLoader.getController()).setModuleController(moduleController);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);
                root.getStylesheets().add(MessageEntry.class.getResource("/client/presentation/css/generalStyleSheet.css").toExternalForm());
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public String toString() {
        try {
            DateFormatter dateFormatter = new DateFormatter(DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM));
            return String.format("%s-40 %s", dateFormatter.valueToString(new Date(Long.parseLong(date))), contents);
        } catch (ParseException ex) {
            ex.printStackTrace();
            return "";
        }
    }

}
