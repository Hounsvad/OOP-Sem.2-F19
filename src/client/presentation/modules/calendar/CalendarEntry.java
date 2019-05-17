/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.calendar;

import client.presentation.utils.StringUtils;

/**
 *
 * @author Sanitas Solutions
 */
public class CalendarEntry {

    private final String title;
    private final String description;

    public CalendarEntry(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return StringUtils.getBoldString(title) + "\n" + description;
    }
}
