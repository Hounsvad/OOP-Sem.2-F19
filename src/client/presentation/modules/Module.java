/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
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
