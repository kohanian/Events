package events.solomid.com.events;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import java.util.Calendar;

/**
 * Created by Derek on 2/20/2016.
 */
public class EventsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        scheduleAlarms();
    }

    public void scheduleAlarms() {
        //Create a new PendingIntent and add it to the AlarmManager
        Log.d("main","scheduled bro");
        Intent alarmIntent = new Intent(EventsApplication.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(EventsApplication.this, 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 60*1000; //Every 1 minute
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Alarm is Set!", Toast.LENGTH_SHORT).show();
    }
}
