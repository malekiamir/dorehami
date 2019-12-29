package ir.ac.kntu.patogh.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.Adapters.EventAdapter;
import ir.ac.kntu.patogh.Adapters.HistoryAdapter;
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

public class HistoryActivity extends AppCompatActivity implements HistoryAdapter.HistoryAdapterOnClickHandler {

    @BindView(R.id.rv_profile_page_history)
    RecyclerView rvHistory;

    private HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = findViewById(R.id.toolbar_history_page);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rvHistory.setLayoutManager(layoutManager);

        rvHistory.setHasFixedSize(true);
        rvHistory.setItemAnimator(new SlideInLeftAnimator());
        historyAdapter = new HistoryAdapter(this);

        AlphaInAnimationAdapter a = new AlphaInAnimationAdapter(historyAdapter);
        a.setDuration(200);
        a.setFirstOnly(false);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(a);
        scaleInAnimationAdapter.setDuration(150);
        scaleInAnimationAdapter.setFirstOnly(false);
        rvHistory.setAdapter(scaleInAnimationAdapter);
    }


    @Override
    public void onClick(Event selectedEvent, TextView v, ImageView imageView) {

    }

//    public void getSummery() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://eg.potatogamers.ir:7701/api/")
//                .build();
//        Gson gson = new Gson();
//        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
//        String token = sharedPreferences.getString("Token", "none");
//        if (token.equals("none")) {
//            Toast.makeText(getContext(), "توکن شما پایان یافته.", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(getActivity(), MainActivity.class);
//            startActivity(intent);
//        }
//
//        patoghApi.getSummery("Bearer " + token).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    String res = response.body().string();
//                    if (serverResponse != null && serverResponse.equals(res)) {
//                        swipeContainer.setRefreshing(false);
//                        return;
//                    } else {
//                        eventAdapter.clear();
//                        events.clear();
//                        serverResponse = res;
//                    }
//                    JsonObject jsonObject1 = new Gson().fromJson(res, JsonObject.class);
//                    String returnValue = jsonObject1.get("returnValue").toString();
//                    Type dorehamiType = new TypeToken<ArrayList<Dorehami>>() {
//                    }.getType();
//                    ArrayList<Dorehami> dorehamis = gson.fromJson(returnValue, dorehamiType);
//                    for (Dorehami dorehami : dorehamis) {
//                        events.add(new Event(dorehami.getName(), dorehami.getSummery()
//                                , dorehami.getStartTime(), String.format("ظرفیت باقی مانده : %d نفر", dorehami.getSize())
//                                , dorehami.getId(), dorehami.getThumbnailId(), dorehami.isJoined()
//                                , dorehami.isFavorited(), dorehami.getImagesIds(), dorehami.getProvince()
//                                , dorehami.getLongitude(), dorehami.getLatitude(), dorehami.getCategory()));
//                    }
//                    eventAdapter.addAll(events);
//                    swipeContainer.setRefreshing(false);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//            }
//        });
//    }
}
