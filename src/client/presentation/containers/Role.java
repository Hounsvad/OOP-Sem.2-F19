/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.containers;

/**
 *
 * @author Hounsvad
 */
public class Role {

    private String roleId;

    private String roleDescription;

    public Role(String roleId, String roleDescription) {
        this.roleId = roleId;
        this.roleDescription = roleDescription;
    }

    /**
     * Get the value of roleDescription
     *
     * @return the value of roleDescription
     */
    public String getRoleDescription() {
        return roleDescription;
    }

    /**
     * Get the value of roleId
     *
     * @return the value of roleId
     */
    public String getRoleId() {
        return roleId;
    }

}
