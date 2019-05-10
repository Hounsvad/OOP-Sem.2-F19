/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.admin;

import java.util.List;

/**
 * Container for the users details and assignments
 *
 * @author Hounsvad
 */
public class UserEntry {

    private String username;
    private String userId;
    private String name;
    private DepartmentEntry department;
    private List<PatientEntry> patients;

    /**
     * Constructs a basic user based on the given data
     *
     * @param username
     * @param userId
     * @param name
     */
    public UserEntry(String username, String userId, String name) {
        this.username = username;
        this.userId = userId;
        this.name = name;
    }

    /**
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return the userId of the user
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @return the full name of the user
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the department container object associated with the users
     *         department
     */
    public DepartmentEntry getDepartment() {
        return department;
    }

    /**
     *
     * @return a list of patient container objects associated with the users
     *         assigned patients
     */
    public List<PatientEntry> getPatients() {
        return patients;
    }

    /**
     *
     * @param username is the username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @param userId is the userId of the user
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     *
     * @param name is the full name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param department is a container object associated with the user
     */
    public void setDepartment(DepartmentEntry department) {
        this.department = department;
    }

    /**
     *
     * @param patients is a list of container objects for patitents
     */
    public void setPatients(List<PatientEntry> patients) {
        this.patients = patients;
    }

    @Override
    public String toString() {
        return name + "(" + username + ")\n" + userId;
    }

}
