/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.containers;

/**
 *
 * @author Sanitas Solutions
 */
public class PatientExtended extends Patient {

    private String department;

    /**
     *
     * @param fullName   is the full name of the patient
     * @param patientID  is the unique id of the patient
     * @param department is the department which the patient is associated
     *                   with
     */
    public PatientExtended(String fullName, String patientID, String department) {
        super(fullName, patientID);
        this.department = department;
    }

    /**
     *
     * @return returns the department which the patient is associated with
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Formats the result for showing in a listView
     *
     * @return a formatted string containing the patients name and id
     */
    @Override
    public String toString() {
        return String.format("%s(%s)", getFullName(), getPatientID());
    }

}
