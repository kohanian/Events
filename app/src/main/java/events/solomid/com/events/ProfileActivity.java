package events.solomid.com.events;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button edit = (Button) findViewById(R.id.edit_settings) ;
        Button exit = (Button) findViewById(R.id.exit_settings) ;

        TextView charity = (TextView) findViewById(R.id.charity_view) ;
        TextView amount = (TextView) findViewById(R.id.donated);

        SharedPreferences prefs = getSharedPreferences(HttpRequester.SHARED_PREF_NAME, MODE_PRIVATE) ;

        String ch = prefs.getString("CHARITY", "no charity") ;
        int am = prefs.getInt(HttpRequester.SHARED_PREF_ID_TOTALDONATED, 0) ;
        String amm = "$" + am ;

        charity.setText(ch);
        amount.setText(amm);

        Log.d("plz", "Setting Total: " + amm + " and " + am + "and charity is "+ch);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EventListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, CharityListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
    }
}
