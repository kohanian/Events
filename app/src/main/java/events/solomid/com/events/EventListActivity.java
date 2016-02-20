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

    private void scheduleEvent(CalenderEvent calenderEvent) {
        Intent alarmIntent = new Intent(EventListActivity.this, AlarmReceiver.class);
        Bundle extras = new Bundle();
        if(EventListActivity.events != null) {
            extras.putInt("COUNT",69);
        }
        alarmIntent.putExtras(extras);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(EventListActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        manager.set(AlarmManager.RTC_WAKEUP, calenderEvent.date.getTime(), pendingIntent);
        Log.d("Swag","Scheduled for "+calenderEvent.date.toString());
        Toast.makeText(this, "Alarm Ready: "+calenderEvent.title, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        if (events == null) events = new ArrayList<>();
        else events.clear();

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/events",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /** parse results add to event adapter **/

                        try {
                            if (response != null) {
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
                                    if(js_event.has("location")) {
                                        Double lat = js_event.getJSONObject("place")
                                                .getJSONObject("location")
                                                .getDouble("latitude");
                                        Double lon = js_event.getJSONObject("place")
                                                .getJSONObject("location")
                                                .getDouble("longitude");
                                        loc = new Location("Events");
                                        loc.setLatitude(lat);
                                        loc.setLongitude(lon);
                                    }
                                    events.add(new CalenderEvent(title, date, location, loc));
                                }

                                Collections.sort(events);

                                Log.d("SAYTHIS", "This is size of " + Integer.toString(events.size()));
                                EventAdapter ea = new EventAdapter(events);
                                recyclerView = (RecyclerView) findViewById(R.id.eventsView);
                                recyclerView.setAdapter(ea);
                                recyclerView.setHasFixedSize(true);
                                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                                llm.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(llm);

                                //TODO: Schedule all events not just the first
                                scheduleEvent(events.get(0));
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
}
