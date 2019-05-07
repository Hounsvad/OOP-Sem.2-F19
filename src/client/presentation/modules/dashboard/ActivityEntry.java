/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author Oliver
 */
public class ActivityEntry {
    
    /**
     * The type of the entry
     */
    private final String typeOfEntry;
    
    /**
     * the date of the entry
     */
    private final String dateOfEntryString;
    
    /**
     * the specifics of the entry
     */
    private final String specificsOfEntry;
    private final String ip;

    /**
     * Constructs an ActivityEntry
     * @param typeOfEntry the type of the entry
     * @param dateOfEntry the date of the entry
     * @param specificsOfEntry the specifics of the entry
     */
    public ActivityEntry(String typeOfEntry, Date dateOfEntry, String specificsOfEntry) {
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
    
    /**
     * A custom toString Method
     * @return the Activity as a String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(specificsOfEntry);
        int i = 0;
        while (i + 23 < sb.length() && (i = sb.lastIndexOf(" ", i + 23)) != -1) {
            sb.replace(i, i + 1, "\n");
        }
        return getBoldString(typeOfEntry) + "\n" + sb.toString() + "\n" + dateOfEntryString;
    }
    
    /**
     * Opens a detailed popup of the entry
     */
    public void showPopup() {
        Platform.runLater(()
                -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ActivityEntryPopupFXML.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                ActivityEntryPopupFXMLController controller = fxmlLoader.<ActivityEntryPopupFXMLController>getController();
                controller.setData(typeOfEntry, specificsOfEntry, "Query: ladidada", dateOfEntryString, ip);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
            }
        });
    }
}
