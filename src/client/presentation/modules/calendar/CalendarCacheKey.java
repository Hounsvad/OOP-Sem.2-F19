/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.calendar;

import java.util.Objects;

/**
 *
 * @author Oliver
 */
public class CalendarCacheKey {

    private long patientID;
    private long epochDateStart;

    public CalendarCacheKey(long patientID, long epochDateStart) {
        this.patientID = patientID;
        this.epochDateStart = epochDateStart;
    }

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
        final CalendarCacheKey other = (CalendarCacheKey) obj;
        if (!Objects.equals(this.patientID, other.patientID)) {
            return false;
        }
        if (!Objects.equals(this.epochDateStart, other.epochDateStart)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.patientID);
        hash = 17 * hash + Objects.hashCode(this.epochDateStart);
        return hash;
    }
}
