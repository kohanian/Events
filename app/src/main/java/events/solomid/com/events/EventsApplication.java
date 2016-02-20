package events.solomid.com.events;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by Derek on 2/20/2016.
 */
public class EventsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
