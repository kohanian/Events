package events.solomid.com.events;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
            Log.d("AlarmService", "Test arg:" + args.getString("COUNT"));
            Toast.makeText(context,"It works man!"+args.getInt("COUNT"),Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"It broke man!",Toast.LENGTH_SHORT).show();
        }
    }
}
