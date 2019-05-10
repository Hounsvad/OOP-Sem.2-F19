/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.containers;

/**
 *
 * @author Hounsvad
 */
public class PatientExtended extends Patient {

    /**
     *
     * @param fullName  is the full name of the patient
     * @param patientID
     */
    public PatientExtended(String fullName, String patientID) {
        super(fullName, patientID);
    }

    /**
     *
     * @return a formatted string containing the patients name and id
     */
    @Override
    public String toString() {
        return String.format("", super.toString());
    }

}
