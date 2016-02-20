package events.solomid.com.events;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        //Determine user location
        Log.d("AlarmRec", "Toasting");
        Bundle args = intent.getExtras();
        if(args != null) {
            String taskName = args.getString("NAME");
            String latLong = args.getString("LATLONG");
            Log.d("AlarmService", "Test arg:" + taskName + " @ GPS " + latLong);
            Toast.makeText(context,"!-"+taskName+" @ "+latLong,Toast.LENGTH_SHORT).show();
            LocationManager mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location swagTest = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(swagTest == null) {
                Log.e("woot","NO GPS");
                Toast.makeText(context,"NO-GPS",Toast.LENGTH_SHORT).show();
            } else {
                Log.e("woot","TEST: "+swagTest.toString());
                Toast.makeText(context,"WOOT",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context,"It broke man!",Toast.LENGTH_SHORT).show();
        }
    }
}
