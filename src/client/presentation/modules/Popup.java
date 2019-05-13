/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.presentation.modules;

import client.presentation.CommunicationHandler;
import client.presentation.utils.credentials.CredentialContainer;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 *
 * @author Oliver
 */
public abstract class Popup implements Initializable {

    @FXML
    private FontAwesomeIconView cross;

    private Initializable moduleController;

    /**
     *
     */
    protected final CommunicationHandler communicationHandler = CommunicationHandler.getInstance();

    /**
     *
     */
    protected final CredentialContainer credentialContainer = CredentialContainer.getInstance();

    /**
     *
     */
    @FXML
    protected void close() {
        ((Stage) cross.getScene().getWindow()).close();
    }

    /**
     *
     * @return
     */
    protected Initializable getModuleController() {
        return moduleController;
    }

    /**
     *
     * @param moduleController
     */
    public void setModuleController(Initializable moduleController) {
        this.moduleController = moduleController;
    }

}
