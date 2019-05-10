/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.containers;

/**
 *
 * @author Hounsvad
 */
public class Patient {

    private final String fullName;
    private final String patientID;

    /**
     *
     * @param fullName
     * @param patientID
     */
    public Patient(String fullName, String patientID) {
        this.fullName = fullName;
        this.patientID = patientID;
    }

    /**
     *
     * @return the full name of the patient
     */
    @Override
    public String toString() {
        return fullName;
    }

    /**
     *
     * @return the full name of the patient
     */
    public String getFullName() {
        return fullName;
    }

    /**
     *
     * @return the patients unique identifier
     */
    public String getPatientID() {
        return patientID;
    }

}
