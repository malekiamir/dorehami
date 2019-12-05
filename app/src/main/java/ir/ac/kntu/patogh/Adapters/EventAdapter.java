package ir.ac.kntu.patogh.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;

import ir.ac.kntu.patogh.Utils.Event;
import ir.ac.kntu.patogh.R;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventAdapterViewHolder> {

    private ArrayList<Event> eventsData;
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 1000;
    private final EventAdapterOnClickHandler mClickHandler;
    long now;

    public interface EventAdapterOnClickHandler {
        void onClick(Event selectedEvent, TextView v, ImageView imageView);
    }

    public EventAdapter(EventAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class EventAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView eventNameTextView;
        TextView eventSummaryTextView;
        TextView eventDateCapacityTextView;
        ImageView eventImage;
        LikeButton likeButton;

        public EventAdapterViewHolder(View view) {
            super(view);
            eventNameTextView = view.findViewById(R.id.tv_card_view_event_name);
            eventSummaryTextView = view.findViewById(R.id.tv_card_view_event_summary);
            eventDateCapacityTextView = view.findViewById(R.id.tv_card_view_date_capacity);
            eventImage = view.findViewById(R.id.img_card_view_event_pic);
            likeButton = view.findViewById(R.id.btn_img_card_view_like);
            likeButton.setLiked(false);
            likeButton.setEnabled(true);
            likeButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {

                }

                @Override
                public void unLiked(LikeButton likeButton) {

                }
            });
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            now = System.currentTimeMillis();
            if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                return;
            }
            mLastClickTime = now;
            int adapterPosition = getAdapterPosition();
            Event selectedEvent = eventsData.get(adapterPosition);
            mClickHandler.onClick(selectedEvent, eventNameTextView, eventImage);
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
        Event selectedEvent = eventsData.get(position);
        eventAdapterViewHolder.eventNameTextView.setText(selectedEvent.getName());
        eventAdapterViewHolder.eventSummaryTextView.setText(selectedEvent.getDesc());
        eventAdapterViewHolder.eventDateCapacityTextView.setText(String.format("%s\n%s"
                , selectedEvent.getDate(), selectedEvent.getCapacity()));
    }


    @Override
    public int getItemCount() {
        if (null == eventsData) return 0;
        return eventsData.size();
    }


    public void setEventData(ArrayList<Event> eventData) {
        eventsData = eventData;
        notifyDataSetChanged();
    }

    public void clear() {
        eventsData.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Event> list) {
        eventsData.addAll(list);
        notifyDataSetChanged();
    }
}
