package events.solomid.com.events;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        //Determine user location
        Log.d("AlarmRec", "Toasting");
        Bundle args = intent.getExtras();
        if(args != null) {
            String taskName = args.getString("NAME");
            String latLong = args.getString("LATLONG") ;
            Log.d("cat", latLong) ;
            Location event = null;

            if (latLong != null && !latLong.equals("NULL")) {
                event = new Location("Event") ;

                String[] arr = latLong.split(" ");
                event.setLatitude(Double.parseDouble(arr[0]));
                event.setLongitude(Double.parseDouble(arr[1]));
            }

            Log.d("AlarmService", "Test arg:" + taskName + " @ GPS " + latLong);
            //Toast.makeText(context,"!-"+taskName+" @ "+latLong,Toast.LENGTH_SHORT).show();

            LocationManager mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location swagTest = null;

            try {
                swagTest = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } catch (SecurityException error) {
                Log.d("WHAT", "NO GPS");
            }

            if(swagTest == null) {
                 Log.e("woot","NO GPS");
                //Toast.makeText(context,"NO-GPS",Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(context, "TEST: " + swagTest.toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, "TEST: " + event.toString(), Toast.LENGTH_LONG).show();
                Log.e("woot", "TEST1: " + swagTest.toString()) ;

                if (swagTest.distanceTo(event) > 1000) {
                    HttpRequester requester = new HttpRequester(context);
                    Log.d("Donating","Donating Cash...");
                    requester.donateCash();

                    NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.com_facebook_button_icon)
                            .setContentTitle("Events")
                            .setContentText("You missed your event!");

                    Intent resultIntent = new Intent(context, MissedEventActivity.class);

                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addParentStack(EventListActivity.class);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                    builder.setContentIntent(resultPendingIntent);
                    NotificationManager notif = (NotificationManager)
                            context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notif.notify(48434, builder.build());
                }
            }
        } else {
            //Toast.makeText(context,"It broke man!",Toast.LENGTH_SHORT).show();
            Log.d("HI", "It broke man.");
        }
    }
}
