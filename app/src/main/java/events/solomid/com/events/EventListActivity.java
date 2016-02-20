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
        private List<CalenderEvent> contactList;

        public EventAdapter(List<CalenderEvent> contactList) {
            this.contactList = contactList;
        }

        @Override
        public int getItemCount() {
            return contactList.size();
        }

        @Override
        public void onBindViewHolder(EventViewHolder contactViewHolder, int i) {
            CalenderEvent ci = contactList.get(i);
            contactViewHolder.vName.setText(ci.name);
            contactViewHolder.vSurname.setText(ci.surname);
            contactViewHolder.vEmail.setText(ci.email);
            contactViewHolder.vTitle.setText(ci.name + " " + ci.surname);
        }

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.card_layout, viewGroup, false);

            return new EventViewHolder(itemView);
        }

    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vLocation;
        protected TextView vDate;

        public EventViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.kyleTextName);
            vLocation = (TextView)  v.findViewById(R.id.kyleTextLocation);
            vDate = (TextView)  v.findViewById(R.id.txtDate);
        }
    }
}
