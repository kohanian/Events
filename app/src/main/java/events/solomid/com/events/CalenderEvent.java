package events.solomid.com.events;

import java.util.Date;

/**
 * Created by Jake on 2/19/2016.
 * Represent
 */
public class CalenderEvent {
    public String title;
    public Date date;
    public String location;

    CalenderEvent(String title, Date date, String location) {
        this.title = title;
        this.date = date;
        this.location = location;
    }
}
