package events.solomid.com.events;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AccountInfo extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        Button submit = (Button) findViewById(R.id.submit) ;
        submit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                EditText id = (EditText) findViewById(R.id.id_input);
                String string_id = id.getText().toString() ;
                SharedPreferences.Editor prefs = getSharedPreferences(HttpRequester.SHARED_PREF_NAME, MODE_PRIVATE).edit();
                prefs.putString("id", string_id) ;
                prefs.apply();
                Intent intent = new Intent(AccountInfo.this, CharityListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
    }
}
