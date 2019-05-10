/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.containers;

/**
 *
 * @author Hounsvad
 */
public class PatientExtended extends Patient {

    public PatientExtended(String fullName, String patientID) {
        super(fullName, patientID);
    }

    @Override
    public String toString() {
        return fullName;
    }

    public String getPatientID() {
        return patientID;
    }
}
