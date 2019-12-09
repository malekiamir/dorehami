package ir.ac.kntu.patogh.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.Badge;

public class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.BadgeAdapterViewHolder> {

    private ArrayList<Badge> eventsData;
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 1000;
    private final BadgeAdapter.BadgeAdapterOnClickHandler mClickHandler;
    long now;
    boolean success = false;

    public interface BadgeAdapterOnClickHandler {
        void onClick(Badge selectedBadge,ImageView imageView);
    }

    public BadgeAdapter(BadgeAdapter.BadgeAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class BadgeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView badgeImage;

        public BadgeAdapterViewHolder(View view) {
            super(view);
            badgeImage = view.findViewById(R.id.badge_icon);
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
            Badge selectedBadge = eventsData.get(adapterPosition);
            mClickHandler.onClick(selectedBadge,badgeImage);
        }

    }


    @Override
    public BadgeAdapter.BadgeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.badge_card;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new BadgeAdapter.BadgeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeAdapterViewHolder holder, int position) {
        Badge selectedBadge = eventsData.get(position);
        selectedBadge.setImageId(R.id.badge_icon);
        
    }



    @Override
    public int getItemCount() {
        if (null == eventsData) return 0;
        return eventsData.size();
    }


    public void setEventData(ArrayList<Badge> eventData) {
        eventsData = eventData;
        notifyDataSetChanged();
    }

}
