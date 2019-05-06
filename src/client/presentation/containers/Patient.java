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

    public Patient(String fullName, String patientID) {
        this.fullName = fullName;
        this.patientID = patientID;
    }

    @Override
    public String toString() {
        return fullName;
    }

    public String getPatientID() {
        return patientID;
    }
}
