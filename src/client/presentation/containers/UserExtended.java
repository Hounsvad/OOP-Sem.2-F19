/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.containers;

/**
 *
 * @author Oliver
 */
public class UserExtended {

    private final String userName;
    private final String fullName;
    private final String userID;

    /**
     *
     * @param userName
     * @param userID
     * @param fullName
     */
    public UserExtended(String userName, String userID, String fullName) {
        this.userName = userName;
        this.fullName = fullName;
        this.userID = userID;
    }

    @Override
    public String toString() {
        return fullName + " (" + userName + ")";
    }

    /**
     *
     * @return
     */
    public String getUserID() {
        return userID;
    }

}
