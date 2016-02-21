package events.solomid.com.events;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventListActivity extends Activity {
    RecyclerView recyclerView;
    static ArrayList<CalenderEvent> events;
    EventsDBAdapter eventsDBAdapter;

    private void scheduleEvent(CalenderEvent calenderEvent, int requestCode) {
        Intent alarmIntent = new Intent(EventListActivity.this, AlarmReceiver.class);
        Bundle extras = new Bundle();
        if(EventListActivity.events != null) {
            extras.putString("NAME", calenderEvent.title);
            //Log.d("cat2", calenderEvent.latlong.toString().substring(8,23)) ;
            if(calenderEvent.latlong != null)
                extras.putString("LATLONG",
                        Double.toString(calenderEvent.latlong.getLatitude()) + " " +
                        Double.toString(calenderEvent.latlong.getLongitude()));
            else extras.putString("LATLONG", "NULL");
        }
        alarmIntent.putExtras(extras);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(EventListActivity.this, requestCode,
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        manager.set(AlarmManager.RTC_WAKEUP, calenderEvent.date.getTime(), pendingIntent);
        Log.d("Swag", "Scheduled for " + calenderEvent.date.toString());
        Toast.makeText(this, "Alarm Ready: "+calenderEvent.title, Toast.LENGTH_SHORT).show();
    }

    private String hashEvent(CalenderEvent event) {
        int val = event.title.length()+ event.location.length();
        String temp = event.title+event.location+val;
        temp = temp.replace(" ","_");
        Log.d("EventList","Hash: "+temp);
        return temp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        if (events == null)
            events = new ArrayList<>();
        else
            events.clear();

        eventsDBAdapter = new EventsDBAdapter(this);
        eventsDBAdapter.open();


        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/events",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /** parse results add to event adapter **/

                        try {
                            if (response.getJSONObject() != null) {
                                JSONObject jsoo = response.getJSONObject();
                                JSONArray jsa = jsoo.getJSONArray("data");
                                SimpleDateFormat stdformatter =
                                        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
                                //SimpleDateFormat cmpformatter =
                                        //new SimpleDateFormat("yyyyMMdd");
                                //Date today = new Date();

                                for (int i = 0; i < jsa.length(); i++) {
                                    JSONObject js_event = jsa.getJSONObject(i);
                                    String title = js_event.getString("name");

                                    String date_string = js_event.getString("start_time");
                                    Date date = stdformatter.parse(date_string);

                                    if (new Date().after(date)) continue;

                                    /** TODO: track their actual locations **/

                                    String location =
                                            js_event.getJSONObject("place").getString("name");
                                    Log.d("SAYTHIS", js_event.getJSONObject("place").toString());

                                    Location loc = null;
                                    if(js_event.getJSONObject("place").has("location")) {
                                        Double lat = js_event.getJSONObject("place")
                                                .getJSONObject("location")
                                                .getDouble("latitude");
                                        Double lon = js_event.getJSONObject("place")
                                                .getJSONObject("location")
                                                .getDouble("longitude");
                                        loc = new Location("Events");
                                        loc.setLatitude(lat);
                                        loc.setLongitude(lon);
                                        Log.d("cat", loc.getLatitude() + " " + loc.getLongitude()) ;
                                    }
                                    events.add(new CalenderEvent(title, date, location, loc));
                                }

                                ArrayList<String> existingHashes = eventsDBAdapter.fetchAllEventHashes();
                                int counter = 0;
                                for(CalenderEvent event : events) {
                                    boolean alreadyExists = false;
                                    String hashToCheck = hashEvent(event);
                                    for(String existingHash : existingHashes) {
                                        if(hashToCheck.equals(existingHash)) {
                                            // There is a match, don't add this
                                            alreadyExists = true;
                                            break;
                                        }
                                    }
                                    if(!alreadyExists) {
                                        //Add it to the database & queue it up
                                        scheduleEvent(event,counter);
                                        counter += 1;
                                        eventsDBAdapter.createEvent(event.title,hashToCheck);
                                        Log.d("ListActivity","FIRST TIME: "+event.title+". Added to db.");
                                    } else {
                                        //Already exists, do nothing
                                        Log.d("ListActivity","NOT FIRST TIME: "+event.title);
                                    }
                                }

                                eventsDBAdapter.close();
                                Collections.sort(events);

                                Log.d("SAYTHIS", "This is size of " + Integer.toString(events.size()));
                                EventAdapter ea = new EventAdapter(events);
                                recyclerView = (RecyclerView) findViewById(R.id.eventsView);
                                recyclerView.setAdapter(ea);
                                recyclerView.setHasFixedSize(true);
                                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                                llm.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(llm);
                            }
                        } catch (JSONException | ParseException error) {
                            Log.d("SAYTHIS", "fuck");
                        }
                    }
                }
        ).executeAsync();
    }

    //Override arrayAdapter so that
    class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
        private List<CalenderEvent> eventList;

        public EventAdapter(List<CalenderEvent> eventList) {
            this.eventList = eventList;
        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }

        @Override
        public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
            CalenderEvent calenderEvent = eventList.get(i);
            eventViewHolder.title.setText(calenderEvent.title);
            eventViewHolder.date.setText(calenderEvent.date.toString());
            eventViewHolder.location.setText(calenderEvent.location);
        }

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.event_cardview, viewGroup, false);

            return new EventViewHolder(itemView);
        }
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView location;
        public TextView date;

        public EventViewHolder(View v) {
            super(v);
            title =  (TextView) v.findViewById(R.id.event_title);
            location = (TextView)  v.findViewById(R.id.event_location);
            date = (TextView)  v.findViewById(R.id.event_date);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_cleardb:
                if(eventsDBAdapter != null) {
                    eventsDBAdapter.open();
                    Toast.makeText(this,"FIXME!",Toast.LENGTH_SHORT).show();
                    eventsDBAdapter.resetTable();
                    eventsDBAdapter.close();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}
