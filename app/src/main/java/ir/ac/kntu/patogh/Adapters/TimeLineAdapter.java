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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ir.ac.kntu.patogh.ApiDataTypes.TypeFavDorehamiAdd;
import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.Interfaces.TimeLine;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.Event;
import ir.ac.kntu.patogh.Utils.TimeLineArrow;
import ir.ac.kntu.patogh.Utils.TimeLineEvent;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pl.hypeapp.materialtimelineview.MaterialTimelineView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

public class TimeLineAdapter extends RecyclerView.Adapter {

    private ArrayList<TimeLine> timeLineData;
    private final TimeLineAdapterOnClickHandler mClickHandler;
    private Context context;
    private SharedPreferences sharedPreferences;
    private String baseUrl = "http://patogh.potatogamers.ir:7701/api/";


    public interface TimeLineAdapterOnClickHandler {
        void onClick(Event selectedEvent);
    }

    public TimeLineAdapter(TimeLineAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        timeLineData = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return timeLineData.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        sharedPreferences = context
                .getSharedPreferences("TokenPref", 0);
        View itemView;
        if (viewType == TimeLine.TYPE_EVENT) {
            itemView = LayoutInflater.from(context)
                    .inflate(R.layout.item_time_line_event, viewGroup, false);
            return new TimeLineEventViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(context)
                    .inflate(R.layout.item_time_line_arrow, viewGroup, false);
            return new TimeLineArrowViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TimeLine.TYPE_EVENT) {
            ((TimeLineEventViewHolder) holder).bindView(position);
        } else {
            ((TimeLineArrowViewHolder) holder).bindView(position);
        }
    }

    @Override
    public int getItemCount() {
        if (timeLineData == null) {
            return 0;
        } else {
            return timeLineData.size();
        }
    }

    public void clear() {
        timeLineData.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<TimeLine> data) {
        timeLineData.addAll(data);
        notifyDataSetChanged();
    }


    class TimeLineEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName;
        TextView tvTime;
        ImageView imgEvent;
        MaterialTimelineView timelineView;


        TimeLineEventViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_time_line_event_name);
            tvTime = itemView.findViewById(R.id.tv_time_line_event_time);
            imgEvent = itemView.findViewById(R.id.img_time_line_event);
            timelineView = itemView.findViewById(R.id.material_timeline_view);
        }

        void bindView(int position) {
            TimeLineEvent timeLineEvent = (TimeLineEvent) timeLineData.get(position);
            tvName.setText(timeLineEvent.getEventName());
            SimpleDateFormat readingFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                Date dateStart = readingFormat.parse(timeLineEvent.getDate());
                PersianDate persianDateStart = new PersianDate(dateStart);
                PersianDateFormat pdformater = new PersianDateFormat("l j F H:i");
                String startDate = pdformater.format(persianDateStart);
                startDate = decimalToFarsi(startDate);
                tvTime.setText(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (position == timeLineData.size() - 1) {
                timelineView.setPosition(2);
                setMargins(timelineView, 16, 0, 16, 10);
            }

            Glide.with(context)
                    .load(R.drawable.rounded_rect_image_not_loaded)
                    .into(imgEvent);
            downloadThumbnail(timeLineEvent.getThumbnailId(), imgEvent);
        }

        @Override
        public void onClick(View view) {

        }

        void setMargins(View v, int l, int t, int r, int b) {
            if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                p.setMargins(l, t, r, b);
                v.requestLayout();
            }
        }
    }

    class TimeLineArrowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        MaterialTimelineView timelineView;

        TimeLineArrowViewHolder(View itemView) {
            super(itemView);
            timelineView = itemView.findViewById(R.id.material_timeline_view2);
        }

        void bindView(int position) {
            TimeLineArrow timeLineArrow = (TimeLineArrow) timeLineData.get(position);
            if (position == 0) {
                timelineView.setPosition(0);
                timelineView.setRadioMarginStart(100f);
                timelineView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                        , 300));
            }
        }

        @Override
        public void onClick(View view) {

        }

        void setMargins(View v, int l, int t, int r, int b) {
            if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                p.setMargins(l, t, r, b);
                v.requestLayout();
            }
        }
    }

    private static String decimalToFarsi(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= '0' && ch <= '9')
                ch += 0x0660 - '0';
            chars[i] = ch;
        }
        return new String(chars);
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

}
