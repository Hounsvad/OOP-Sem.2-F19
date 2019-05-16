/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.modules.calendar;

/**
 *
 * @author Oliver
 */
public class DayRythmHour {

    private int hour;
    private String title;
    private String iconName;

    public DayRythmHour(int hour, String title, String iconName) {
        this.hour = hour;
        this.title = title;
        this.iconName = iconName;
    }

    public int getHour() {
        return hour;
    }

    public String getTitle() {
        return title;
    }

    public String getIconName() {
        return iconName;
    }

    public String toString() {
        return "Kl. " + hour + ": " + title;
    }

    public void openEditor() {
        //OPEN EDITOR
    }

}
