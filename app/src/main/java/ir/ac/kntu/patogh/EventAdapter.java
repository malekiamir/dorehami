package ir.ac.kntu.patogh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventAdapterViewHolder> {

    private Event[] eventsData;

    private final EventAdapterOnClickHandler mClickHandler;

    public interface EventAdapterOnClickHandler {
        void onClick(Event weatherForDay, TextView v);
    }

    public EventAdapter(EventAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class EventAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        @BindView(R.id.tv_card_view_event_name)
        TextView eventNameTextView;
//        @BindView(R.id.tv_card_view_event_summary)
        TextView eventSummaryTextView;
//        @BindView(R.id.tv_card_view_date_capacity)
        TextView eventDateCapacityTextView;

        public EventAdapterViewHolder(View view) {
            super(view);
            eventNameTextView = view.findViewById(R.id.tv_card_view_event_name);
            eventSummaryTextView = view.findViewById(R.id.tv_card_view_event_summary);
            eventDateCapacityTextView = view.findViewById(R.id.tv_card_view_date_capacity);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Event selectedEvent = eventsData[adapterPosition];
            mClickHandler.onClick(selectedEvent, eventNameTextView);
        }
    }

    @Override
    public EventAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.card_event;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new EventAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventAdapterViewHolder eventAdapterViewHolder, int position) {
        Event selectedEvent = eventsData[position];
        eventAdapterViewHolder.eventNameTextView.setText(selectedEvent.getName());
        eventAdapterViewHolder.eventSummaryTextView.setText(selectedEvent.getDesc());
        eventAdapterViewHolder.eventDateCapacityTextView.setText(String.format("%s\n%s"
                , selectedEvent.getDate(), selectedEvent.getCapacity()));

    }


    @Override
    public int getItemCount() {
        if (null == eventsData) return 0;
        return eventsData.length;
    }


    public void setEventData(Event[] eventData) {
        eventsData = eventData;
        notifyDataSetChanged();
    }
}
