package ir.ac.kntu.patogh.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;

import ir.ac.kntu.patogh.ApiDataTypes.TypeFavDorehamiAdd;
import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.Event;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventAdapterViewHolder> {

    private ArrayList<Event> eventsData;
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 1000;
    private final EventAdapterOnClickHandler mClickHandler;
    long now;
    SharedPreferences sharedPreferences;
    boolean success = false;
    Context context;

    public interface EventAdapterOnClickHandler {
        void onClick(Event selectedEvent, TextView v, ImageView imageView);
    }

    public EventAdapter(EventAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        eventsData = new ArrayList<>();
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
                    int adapterPosition = getAdapterPosition();
                    favDorehamiAdd(eventsData.get(adapterPosition).getId());
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    int adapterPosition = getAdapterPosition();
                    favDorehamiRemove(eventsData.get(adapterPosition).getId());
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

        boolean favDorehamiAdd(String id) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://eg.potatogamers.ir:7701/api/")
                    .build();
            Gson gson = new Gson();
            PatoghApi patoghApi = retrofit.create(PatoghApi.class);
            String token = sharedPreferences.getString("Token", "none");
            if (token.equals("none")) {
                return false;
            }
            TypeFavDorehamiAdd te;
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json")
                    , gson.toJson(te = new TypeFavDorehamiAdd(id)
                    ));

            Log.d("@@@@@@@@@", te.getIdString());
            patoghApi.favDorehamiAdd("Bearer " + token, requestBody).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        System.out.println("liked event with id : " + id);
                        success = true;
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    success = false;
                }
            });
            return success;
        }

        boolean favDorehamiRemove(String id) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://eg.potatogamers.ir:7701/api/")
                    .build();
            Gson gson = new Gson();
            PatoghApi patoghApi = retrofit.create(PatoghApi.class);
            String token = sharedPreferences.getString("Token", "none");
            if (token.equals("none")) {
                return false;
            }
            TypeFavDorehamiAdd te;
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json")
                    , gson.toJson(te = new TypeFavDorehamiAdd(id)
                    ));

            Log.d("@@@@@@@@@", te.getIdString());
            patoghApi.favDorehamiRemove("Bearer " + token, requestBody).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        System.out.println("disliked event with id : " + id);
                        success = true;
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    success = false;
                }
            });
            return success;
        }

    }


    @Override
    public EventAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        sharedPreferences = context
                .getSharedPreferences("TokenPref", 0);
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
        downloadThumbnail(selectedEvent.getThumbnailId(), eventAdapterViewHolder.eventImage);

    }


    public void downloadThumbnail(String id, ImageView eventImage) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://eg.potatogamers.ir:7701/api/")
                .build();
        Gson gson = new Gson();
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        String token = sharedPreferences.getString("Token", "none");
        if (token.equals("none")) {
            return;
        }

        TypeFavDorehamiAdd te;
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json")
                , gson.toJson(te = new TypeFavDorehamiAdd(id)
                ));

        Log.d("@@@@@@@@@", te.toString());
        patoghApi.downloadThumbnail("Bearer " + token, requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {

                    System.out.println("downloaded");
                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                    Glide.with(context)
                            .load(bmp)
                            .into(eventImage);
                } else {
                    System.out.println("failed to download");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("no responseeeee");
            }
        });
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
