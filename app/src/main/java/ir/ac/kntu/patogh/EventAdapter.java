package ir.ac.kntu.patogh;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventAdapterViewHolder> {

    private Event[] eventsData;

    private final EventAdapterOnClickHandler mClickHandler;

    public interface EventAdapterOnClickHandler {
        void onClick(Event weatherForDay, TextView v, ImageView imageView);
    }

    public EventAdapter(EventAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class EventAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView eventNameTextView;
        TextView eventSummaryTextView;
        TextView eventDateCapacityTextView;
        ImageView eventImage;
        ImageButton eventLikeButton;

        public EventAdapterViewHolder(View view) {
            super(view);
            eventNameTextView = view.findViewById(R.id.tv_card_view_event_name);
            eventSummaryTextView = view.findViewById(R.id.tv_card_view_event_summary);
            eventDateCapacityTextView = view.findViewById(R.id.tv_card_view_date_capacity);
            eventImage = view.findViewById(R.id.img_card_view_event_pic);
            eventLikeButton = view.findViewById(R.id.btn_img_card_view_like);
            eventLikeButton.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v != eventLikeButton) {
                int adapterPosition = getAdapterPosition();
                Event selectedEvent = eventsData[adapterPosition];
                mClickHandler.onClick(selectedEvent, eventNameTextView, eventImage);
            } else {
                eventLikeButton.setBackgroundColor(Color.parseColor("#F0D919"));
            }
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
