/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.containers;

import java.util.Objects;

/**
 *
 * @author Hounsvad
 */
public class Department {

    private String departmentId;
    private String departmentName;

    /**
     * Constructs a container with the relevant details about a department
     *
     * @param departmentId   is the unique id of the department
     * @param departmentName is the name of the department
     */
    public Department(String departmentId, String departmentName) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    /**
     *
     * @return the unique id of the department
     */
    public String getDepartmentId() {
        return departmentId;
    }

    /**
     *
     * @return the name of the department
     */
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     *
     * @param obj a department
     * @return true if both department ids match
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Department other = (Department) obj;
        if (!other.departmentId.equals(departmentId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.departmentId);
        hash = 59 * hash + Objects.hashCode(this.departmentName);
        return hash;
    }

}
