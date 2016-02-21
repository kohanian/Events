package events.solomid.com.events;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CharityListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_list);
        String[] list = new String[6] ;
        list[0] = "The Chukwudi Fund" ;
        list[1] = "Feed the Squirrels" ;
        list[2] = "Cakes for Kidz" ;
        list[3] = "Ankit needs a new pair of shoes" ;
        list[4] = "Furry Friends Fund" ;
        list[5] = "Hugs for puppies" ;
        final ListView listv = (ListView) findViewById(R.id.charities) ;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                                                                list);
        listv.setAdapter(adapter);
        listv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor prefs = getSharedPreferences(HttpRequester.SHARED_PREF_NAME, MODE_PRIVATE).edit();
                String charity = listv.getItemAtPosition(position).toString() ;
                prefs.putString("CHARITY", charity) ;
                prefs.commit();
                Intent intent = new Intent(CharityListActivity.this, EventListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        }) ;

    }

}
