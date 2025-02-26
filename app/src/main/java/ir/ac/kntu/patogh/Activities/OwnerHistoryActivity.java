package ir.ac.kntu.patogh.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.Adapters.HistoryAdapter;
import ir.ac.kntu.patogh.Adapters.OwnerHistoryAdapter;
import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.Dorehami;
import ir.ac.kntu.patogh.Utils.Event;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OwnerHistoryActivity extends AppCompatActivity implements OwnerHistoryAdapter.OwnerHistoryAdapterOnClickHandler {

    @BindView(R.id.rv_owner_history)
    RecyclerView rvHistory;

    private OwnerHistoryAdapter historyAdapter;
    private Event event;
    private ArrayList<Event> lastEvents;
    private SharedPreferences sharedPreferences;
    private String serverResponse;
    private String baseURL = "http://patogh.potatogamers.ir:7701/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_history);

        Toolbar toolbar = findViewById(R.id.toolbar_owner_history_page);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("رویدادهای ایجاد شده");
        ButterKnife.bind(this);

        lastEvents = new ArrayList<>();

        sharedPreferences = this
                .getSharedPreferences("TokenPref", 0);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rvHistory.setLayoutManager(layoutManager);

        rvHistory.setHasFixedSize(true);
        rvHistory.setItemAnimator(new SlideInLeftAnimator());
        historyAdapter = new OwnerHistoryAdapter(this);

        AlphaInAnimationAdapter a = new AlphaInAnimationAdapter(historyAdapter);
        a.setDuration(200);
        a.setFirstOnly(false);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(a);
        scaleInAnimationAdapter.setDuration(150);
        scaleInAnimationAdapter.setFirstOnly(false);
        rvHistory.setAdapter(scaleInAnimationAdapter);
        getOwnedHistory();
    }


//    @Override
//    public void onClick(Event selectedEvent) {
//
//    }

    public void getOwnedHistory() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .build();
        Gson gson = new Gson();
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        String token = sharedPreferences.getString("Token", "none");
        if (token.equals("none")) {
            Toast.makeText(this, "توکن شما پایان یافته.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        patoghApi.getOwnedDorehamies("Bearer " + token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println("OWNER : "+response.code());
                    String res = response.body().string();
                    historyAdapter.clear();
                    lastEvents.clear();
                    JsonObject jsonObject1 = new Gson().fromJson(res, JsonObject.class);
                    String returnValue = jsonObject1.get("returnValue").toString();
                    Type dorehamiType = new TypeToken<ArrayList<Dorehami>>() {
                    }.getType();
                    ArrayList<Dorehami> dorehamis = gson.fromJson(returnValue, dorehamiType);
                    System.out.println(dorehamis.size());
                    SimpleDateFormat readingFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date date = new Date();
                    for (Dorehami dorehami : dorehamis) {
                        if (dorehami.getStartTime().compareTo(farsiToDecimal(readingFormat.format(date))) > 0) {
                            lastEvents.add(new Event(dorehami.getName(), dorehami.getSummery()
                                    , dorehami.getStartTime(), String.format("ظرفیت باقی مانده : %d نفر", dorehami.getSize())
                                    , dorehami.getId(), dorehami.getThumbnailId(), dorehami.isJoined()
                                    , dorehami.isFavorited(), dorehami.getImagesIds(), dorehami.getProvince()
                                    , dorehami.getLongitude(), dorehami.getLatitude(), dorehami.getCategory()
                                    , dorehami.getTags(), dorehami.isPhysical(), String.format("ظرفیت باقی مانده : %d نفر", dorehami.getRemainingSize())));
                        }
                    }
                    historyAdapter.addAll(lastEvents);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private static String farsiToDecimal(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            else if (ch == '٫')
                ch = '.';
            chars[i] = ch;
        }
        return new String(chars);
    }

    @Override
    public void onClick(Event selectedEvent) {
        Context context = OwnerHistoryActivity.this;
        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtra("event_name", selectedEvent.getName());
        intent.putExtra("event_date", selectedEvent.getDate());
        intent.putExtra("event_capacity", selectedEvent.getCapacity());
        intent.putExtra("event_id", selectedEvent.getId());
        intent.putExtra("class", "favorite");
        context.startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
