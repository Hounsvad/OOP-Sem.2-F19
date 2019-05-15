/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.journal;

import client.presentation.modules.Module;
import client.presentation.modules.Popup;
import client.presentation.modules.dashboard.MessageEntry;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Hounsvad
 */
public class MedicinalEntry {

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
                ((Popup) fxmlLoader.getController()).setModuleController(moduleController);
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
