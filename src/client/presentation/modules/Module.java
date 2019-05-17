/* 
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.modules;

import client.presentation.CommunicationHandler;
import client.presentation.utils.credentials.CredentialContainer;
import javafx.fxml.Initializable;

/**
 *
 * @author Sanitas Solutions
 */
public abstract class Module implements Initializable {

    /**
     *
     */
    protected CommunicationHandler communicationHandler = CommunicationHandler.getInstance();

    /**
     *
     */
    protected CredentialContainer credentialContainer = CredentialContainer.getInstance();

    /**
     *
     */
    abstract protected void clearAll();

    /**
     *
     */
    abstract public void updateData();
}
