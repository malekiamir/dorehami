package ir.ac.kntu.patogh.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.like.LikeButton;
import com.like.OnLikeListener;

import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.FavoriteEvent;



public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteAdapterViewHolder> {

    private FavoriteEvent[] eventsData;
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 1000;
    private final FavoriteAdapter.FavoriteAdapterOnClickHandler mClickHandler;
    long now;

    public interface FavoriteAdapterOnClickHandler {
        void onClick(FavoriteEvent weatherForDay);
    }

    public FavoriteAdapter(FavoriteAdapter.FavoriteAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class FavoriteAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView eventNameTextView;
        TextView eventDateTextView;
        TextView eventCapacityTextView;
        ImageView eventImage;
        ConstraintLayout constraintLayout;


        public FavoriteAdapterViewHolder(View view) {
            super(view);
            eventNameTextView = view.findViewById(R.id.tv_favorite_card_event_name);
            eventDateTextView = view.findViewById(R.id.tv_favorite_card_date);
            eventCapacityTextView = view.findViewById(R.id.tv_favorite_card_capacity);
            eventImage = view.findViewById(R.id.img_favorite_card_event_image);
            constraintLayout = view.findViewById(R.id.constraintLayout_favorite_card);
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
            FavoriteEvent selectedEvent = eventsData[adapterPosition];
            mClickHandler.onClick(selectedEvent);
        }
    }


    @Override
    public FavoriteAdapter.FavoriteAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.favorite_card;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new FavoriteAdapter.FavoriteAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteAdapter.FavoriteAdapterViewHolder eventAdapterViewHolder, int position) {
        FavoriteEvent selectedEvent = eventsData[position];
        eventAdapterViewHolder.eventNameTextView.setText(selectedEvent.getName());
        eventAdapterViewHolder.eventDateTextView.setText(String.format("%s"
                , selectedEvent.getDate()));
        eventAdapterViewHolder.eventCapacityTextView.setText(String.format("%s"
                ,selectedEvent.getCapacity()));
    }


    @Override
    public int getItemCount() {
        if (null == eventsData) return 0;
        return eventsData.length;
    }


    public void setEventData(FavoriteEvent[] eventData) {
        eventsData = eventData;
        notifyDataSetChanged();
    }



}
