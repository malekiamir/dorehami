package ir.ac.kntu.patogh.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.like.LikeButton;

import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.Event;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryAdapterViewHolder> {

    private ArrayList<Event> eventsData;
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 1000;
    private final HistoryAdapterOnClickHandler mClickHandler;
    private long now;
    private SharedPreferences sharedPreferences;
    private boolean success = false;
    private Context context;

    public interface HistoryAdapterOnClickHandler {
        void onClick(Event selectedEvent, TextView v, ImageView imageView);
    }

    public HistoryAdapter(HistoryAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        eventsData = new ArrayList<>();
    }

    public class HistoryAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView eventNameTextView;
        TextView eventSummaryTextView;
        TextView eventDateTextView;
        TextView eventCapacityTextView;
        TextView eventCityTextView;
        ImageView eventImage;
        LikeButton likeButton;
        View likeView;

        public HistoryAdapterViewHolder(View view) {
            super(view);
            eventNameTextView = view.findViewById(R.id.tv_card_view_event_name);
            eventSummaryTextView = view.findViewById(R.id.tv_card_view_event_summary);
            eventDateTextView = view.findViewById(R.id.tv_card_view_date);
            eventCapacityTextView = view.findViewById(R.id.tv_card_view_capacity);
            eventCityTextView = view.findViewById(R.id.tv_card_view_city_name);
            eventImage = view.findViewById(R.id.img_card_view_event_pic);
            likeButton = view.findViewById(R.id.btn_img_card_view_like);
          //  likeView = view.findViewById(R.id.view_event_card_around_like_btn);

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
    public HistoryAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        sharedPreferences = context
                .getSharedPreferences("TokenPref", 0);
        int layoutIdForListItem = R.layout.card_event;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new HistoryAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryAdapterViewHolder historyAdapterViewHolder, int position) {
        Event selectedEvent = eventsData.get(position);
        historyAdapterViewHolder.eventNameTextView.setText(selectedEvent.getName());
        historyAdapterViewHolder.eventSummaryTextView.setText(selectedEvent.getDesc());
        historyAdapterViewHolder.eventCityTextView.setText(selectedEvent.getCity());
        historyAdapterViewHolder.likeButton.setVisibility(View.INVISIBLE);
        SimpleDateFormat readingFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date dateStart = readingFormat.parse(selectedEvent.getDate());
            PersianDate persianDateStart = new PersianDate(dateStart);
            PersianDateFormat pdformater = new PersianDateFormat("l j F H:i");
            String startDate = pdformater.format(persianDateStart);
            historyAdapterViewHolder.eventDateTextView.setText(startDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        historyAdapterViewHolder.eventCapacityTextView.setText(String.format(selectedEvent.getCapacity()));
        Glide.with(context)
                .load(R.drawable.rounded_rect_image_not_loaded)
                .into(historyAdapterViewHolder.eventImage);
    }



    @Override
    public int getItemCount() {
        if (null == eventsData) return 0;
        return eventsData.size();
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

