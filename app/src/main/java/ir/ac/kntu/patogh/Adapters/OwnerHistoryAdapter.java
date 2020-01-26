package ir.ac.kntu.patogh.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.muddzdev.styleabletoast.StyleableToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;


public class OwnerHistoryAdapter extends RecyclerView.Adapter<OwnerHistoryAdapter.OwnerHistoryAdapterViewHolder> {

    private ArrayList<Event> eventsData;
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 1000;
    private final OwnerHistoryAdapterOnClickHandler mClickHandler;
    private long now;
    private SharedPreferences sharedPreferences;
    private boolean success = false;
    private Context context;
    private String baseUrl = "http://patogh.potatogamers.ir:7701/api/";



    public interface OwnerHistoryAdapterOnClickHandler {
        void onClick(Event selectedEvent);
    }

    public OwnerHistoryAdapter(OwnerHistoryAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        eventsData = new ArrayList<>();
    }

    public class OwnerHistoryAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView eventNameTextView;
        TextView eventSummaryTextView;
        TextView eventDateTextView;
        TextView eventCapacityTextView;
        TextView eventCityTextView;
        ImageView eventImage;
        ImageButton eventDeleteButton;
        private int mRecentlyDeletedItemPosition;
        private Event mRecentlyDeletedItem;

        public OwnerHistoryAdapterViewHolder(View view) {
            super(view);
            eventNameTextView = view.findViewById(R.id.tv_card_view_event_name);
            eventSummaryTextView = view.findViewById(R.id.tv_card_view_event_summary);
            eventDateTextView = view.findViewById(R.id.tv_card_view_date);
            eventCapacityTextView = view.findViewById(R.id.tv_card_view_capacity);
            eventCityTextView = view.findViewById(R.id.tv_card_view_city_name);
            eventImage = view.findViewById(R.id.img_card_view_event_pic);
            eventDeleteButton = view.findViewById(R.id.btn_img_card_view_delete);
            view.setOnClickListener(this);
            eventDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRecentlyDeletedItemPosition = getAdapterPosition();
                    boolean isDeleted = deleteDorehami(eventsData
                            .get(mRecentlyDeletedItemPosition).getId());
                    if (isDeleted) {
                        mRecentlyDeletedItem = eventsData.get(mRecentlyDeletedItemPosition);
                        eventsData.remove(mRecentlyDeletedItemPosition);
                        notifyItemRemoved(mRecentlyDeletedItemPosition);
                        showUndoSnackbar(view);
                    } else {
                        new StyleableToast
                                .Builder(view.getContext())
                                .text("مجددا تلاش نمایید")
                                .textColor(Color.WHITE)
                                .backgroundColor(Color.argb(255, 255, 94, 100))
                                .show();
                    }
                }
            });
        }

        private void showUndoSnackbar(View view) {
            Snackbar snackbar = Snackbar.make(view, "رویداد شما حذف شد",
                    Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.GRAY);
            snackbar.setAction("پشیمون شدم", v -> undoDelete());
            snackbar.show();
        }

        private void undoDelete() {
            eventsData.add(mRecentlyDeletedItemPosition,
                    mRecentlyDeletedItem);
            notifyItemInserted(mRecentlyDeletedItemPosition);
        }

        boolean deleteDorehami(String id) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
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
            patoghApi.deleteDorehami("Bearer " + token, requestBody).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        System.out.println("deleted event with id : " + id);
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


        @Override
        public void onClick(View v) {
            now = System.currentTimeMillis();
            if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                return;
            }
            mLastClickTime = now;
            int adapterPosition = getAdapterPosition();
            Event selectedEvent = eventsData.get(adapterPosition);
            mClickHandler.onClick(selectedEvent);
        }
    }


    @Override
    public OwnerHistoryAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        sharedPreferences = context
                .getSharedPreferences("TokenPref", 0);
        int layoutIdForListItem = R.layout.owner_event_card;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new OwnerHistoryAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OwnerHistoryAdapterViewHolder historyAdapterViewHolder, int position) {
        Event selectedEvent = eventsData.get(position);
        historyAdapterViewHolder.eventNameTextView.setText(selectedEvent.getName());
        historyAdapterViewHolder.eventSummaryTextView.setText(selectedEvent.getDesc());
        if (selectedEvent.isPhysical()) {
            historyAdapterViewHolder.eventCityTextView.setText(selectedEvent.getCity());
        } else {
            historyAdapterViewHolder.eventCityTextView.setText("مجازی");
        }
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
        downloadThumbnail(selectedEvent.getThumbnailId(), historyAdapterViewHolder.eventImage);
    }


    private void downloadThumbnail(String id, ImageView eventImage) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
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
                            .transform(new RoundedCorners(6))
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


    public void clear() {
        eventsData.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<Event> list) {
        eventsData.addAll(list);
        notifyDataSetChanged();
    }

}

