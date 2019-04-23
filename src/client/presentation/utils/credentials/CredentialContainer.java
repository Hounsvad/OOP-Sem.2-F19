/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.utils.credentials;

import java.io.IOException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 *
 * @author Hounsvad
 */
public final class CredentialContainer {

    /**
     * The storage for the instance
     */
    private static CredentialContainer instance = null;

    /**
     * The stored password
     */
    private String password = null;

    /**
     * The stored username
     */
    private String username = null;

    /**
     * The stored time for last access
     */
    private Long lastAccess = 0l;

    /**
     * A variable to indicate whether the password is being retrieved or not
     */
    private boolean isGettingCredentials = false;

    /**
     *
     */
    private boolean firstRound = true;

    /**
     * Observable value indicating the whether or not the credentials are ready
     */
    private BooleanProperty credentialReady = new SimpleBooleanProperty(false);

    /**
     * The path of the login screens FXML
     */
    private static final String LOGIN_SCREEN_PATH = "";

    protected BooleanProperty getCredentialReadyProperty() {
        return credentialReady;
    }

    private CredentialContainer() {

    }

    public static CredentialContainer getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new CredentialContainer();
        return instance;
    }

    /**
     * Opens the login window
     */
    public void openLoginWindow() {
        if (!isGettingCredentials) {
            isGettingCredentials = true;
            FXMLLoader loader = new FXMLLoader();
            Stage login = new Stage();
            try {
                Parent root = loader.load(getClass().getResource(LOGIN_SCREEN_PATH));
                Scene s = new Scene(root);
                login.setScene(s);
                login.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Checks if the credentials are still valid
     * If the credentials have not been accessed for one hour they will
     * be reset
     *
     * @return
     */
    public boolean checkTimeValid() {
        if (this.lastAccess > 0 && this.lastAccess > System.currentTimeMillis() - 3600000) {
            return false;
        }
        this.password = null;
        this.lastAccess = 0l;//This is a long
        credentialReady.set(false);
        return true;
    }

    /**
     * Sets the {@link this#password} and updates the system to
     * know that it has a new password
     *
     * @param password is the hashed password in a string format
     */
    protected void setPassword(String password) {
        this.password = password;
        if (this.username != null) {
            credentialReady.set(true);
        }
        this.isGettingCredentials = false;
        this.lastAccess = System.currentTimeMillis();
    }

    /**
     * Sets the {@link this#username} and updates the system to
     * know that it has a new username
     *
     * @param username is the username in a string format
     */
    protected void setUsername(String username) {
        this.username = username;
        if (this.password != null) {
            credentialReady.set(true);
        }
        this.isGettingCredentials = false;
        this.lastAccess = System.currentTimeMillis();
    }

    /**
     * A getter for the {@link this#password}
     *
     * @return the password stored in the container or null if the credentials
     *         are timed out
     */
    public String getPassword() {
        if (checkTimeValid()) {
            this.lastAccess = System.currentTimeMillis();
            return this.password;
        }
        return null;
    }

    /**
     * A getter for the {@link this#password}
     *
     * @return the username stored in the container or null if the credentials
     *         are timed out
     */
    public String getUsername() {
        if (checkTimeValid()) {
            this.lastAccess = System.currentTimeMillis();
            return this.username;
        }
        return null;
    }

    /**
     * Checks if this is the first request for the login credentials
     *
     * @return a boolean value representing the first value
     */
    public boolean isFirst() {
        if (firstRound) {
            firstRound = false;
            return true;
        }
        return false;
    }
}
