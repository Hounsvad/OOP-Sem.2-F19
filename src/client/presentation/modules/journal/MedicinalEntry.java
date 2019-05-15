/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.journal;

import client.presentation.modules.Module;
import client.presentation.modules.dashboard.MessageEntry;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
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
public class MedicinalEntry {

    private String patientID;
    private String date;
    private String notes;
    private String medicin;
    private String dosage;

    public MedicinalEntry(String patientID, String date, String text) {
        this.patientID = patientID;
        this.date = date;
        this.notes = text.split(";:;")[2];
        this.medicin = text.split(";:;")[0];
        this.dosage = text.split(";:;")[1];

    }

    public void showPopup() {
        Platform.runLater(()
                -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MedicalEntryShowMessagePopupFXML.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                MedicalEntryShowMessagePopupFXMLController controller = fxmlLoader.<MedicalEntryShowMessagePopupFXMLController>getController();
                controller.setData(notes, date, medicin, dosage);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);
                root.getStylesheets().add(getClass().getResource("/client/presentation/css/generalStyleSheet.css").toExternalForm());
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
            }
        });
    }

    /**
     *
     * @param moduleController
     */
    public static void showCreationPopup(Module moduleController) {
        Platform.runLater(()
                -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MedicinalEntry.class.getResource("MedicalEntryCreationPopupFXML.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                ((MedicalEntryCreationPopupFXMLController) fxmlLoader.getController()).setModuleController(moduleController);
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

    public String toString() {
        try {
            DateFormatter dateFormatter = new DateFormatter(DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM));
            String headLine = String.format("Medicin type: %-30sDosage: %s", medicin, dosage);
            return String.format("%-40s : %s", dateFormatter.valueToString(new Date(Long.parseLong(date))), headLine);
        } catch (ParseException ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
