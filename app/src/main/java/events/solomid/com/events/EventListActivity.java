package events.solomid.com.events;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends Activity {
    RecyclerView recyclerView;
    ArrayList<CalenderEvent> calenderEvents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        calenderEvents = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.eventsView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
    }

    //Override arrayAdapter so that
    class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
        private List<CalenderEvent> eventList;

        public EventAdapter(List<CalenderEvent> contactList) {
            this.eventList = contactList;
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
