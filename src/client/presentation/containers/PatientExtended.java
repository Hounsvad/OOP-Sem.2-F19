/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.containers;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 * @author Hounsvad
 */
public class PatientExtended extends Patient implements Comparable<PatientExtended> {

    private String department;

    /**
     *
     * @param fullName  is the full name of the patient
     * @param patientID
     */
    public PatientExtended(String fullName, String patientID, String department) {
        super(fullName, patientID);
        this.department = department;
    }

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

    @Override
    public int compareTo(PatientExtended o) {

        Integer[] oTokens = (Integer[]) Arrays.asList(o.getDepartment().split("-")).stream().map(t -> Integer.parseInt(t)).collect(Collectors.toList()).toArray();
        Integer[] tokens = (Integer[]) Arrays.asList(this.getDepartment().split("-")).stream().map(t -> Integer.parseInt(t)).collect(Collectors.toList()).toArray();
        if (o.department.split("")) {

        }
    }

}
