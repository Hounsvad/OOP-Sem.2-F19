/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.utils;

/**
 *
 * @author Oliver
 */
public class User {

    private final String userName;
    private final String fullName;
    private final String userID;

    public User(String userName, String fullName, String userID) {
        this.userName = userName;
        this.fullName = fullName;
        this.userID = userID;
    }

    @Override
    public String toString() {
        return fullName + " (" + userName + ")";
    }

    public String getUserID() {
        return userID;
    }

}
