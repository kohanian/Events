package events.solomid.com.events;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MissedEventActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missed_event);
        //Bundle args = getIntent().getExtras();
        //String name_place = args.getString("name", "ANONY");

        ((TextView) (findViewById(R.id.missed_event_textview)))
                .setText("You missed your event!"); 
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, EventListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }
}
