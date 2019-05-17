/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation.modules.dashboard;

import client.presentation.modules.Module;
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
public class MessageEntry {

    /**
     * The subject of the the entry
     */
    private final String subject;

    /**
     * The sender of the entry
     */
    private final String sender;

    /**
     * The message of the entry
     */
    private final String message;

    /**
     * The date on which the entry has been sent
     */
    private final String sentDateString;

    private final Module moduleController;

    /**
     * Constructs a new MessageEntry
     *
     * @param subject  The subject of the the entry
     * @param sender   The sender of the entry
     * @param message  The message of the entry
     * @param sentDate The date on which the entry has been sent
     */
    public MessageEntry(String subject, String sender, String message, Date sentDate) {
        this.subject = subject;
        this.sender = sender;
        this.message = message;
        this.sentDateString = String.format("%1$" + 2 + "s", sentDate.getHours()).replace(' ', '0') + ":"
                + String.format("%1$" + 2 + "s", sentDate.getMinutes()).replace(' ', '0') + ":"
                + String.format("%1$" + 2 + "s", sentDate.getSeconds()).replace(' ', '0') + " "
                + String.format("%1$" + 2 + "s", sentDate.getDate()).replace(' ', '0') + "/"
                + String.format("%1$" + 2 + "s", sentDate.getMonth() + 1).replace(' ', '0') + "/"
                + (sentDate.getYear() + 1900);
        this.moduleController = null;
    }

    /**
     *
     * @param moduleController
     */
    public MessageEntry(Module moduleController) {
        this.subject = "";
        this.sender = "";
        this.message = "";
        this.sentDateString = "";
        this.moduleController = moduleController;
        showCreationPopup();
    }

    /**
     * A custom toString method
     *
     * @return The MessageEntry as a String
     */
    @Override
    public String toString() {
        return getBoldString(sender) + ": " + subject + "\n" + message + "\n" + sentDateString;
    }

    /**
     * Opens a detailed popup of the MessageEntry
     */
    public void showPopup() {
        Platform.runLater(()
                -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MessageEntryPopupFXML.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                MessageEntryPopupFXMLController controller = fxmlLoader.<MessageEntryPopupFXMLController>getController();
                controller.setData(sender + ": " + subject, message, sentDateString);
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
     */
    public final void showCreationPopup() {
        Platform.runLater(()
                -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MessageEntry.class.getResource("MessageEntryCreationPopupFXML.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                fxmlLoader.<MessageEntryCreationPopupFXMLController>getController().setModuleController(moduleController);
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
