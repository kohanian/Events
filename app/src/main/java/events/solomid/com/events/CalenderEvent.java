package events.solomid.com.events;

import android.location.Location;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by Jake on 2/19/2016.
 * Represent
 */
public class CalenderEvent implements Comparable<CalenderEvent> {
    public String title;
    public Date date;
    public String location;
    public Location latlong;

    CalenderEvent(String title, Date date, String location, Location latlong) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.latlong = latlong;
    }

    public int compareTo(CalenderEvent ce) {
        if(date.after(ce.date)) return 1;
        else if(date.before(ce.date)) return -1;
        else return 0;
    }
}
