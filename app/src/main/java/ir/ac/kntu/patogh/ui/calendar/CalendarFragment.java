package ir.ac.kntu.patogh.ui.calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.Activities.MainActivity;
import ir.ac.kntu.patogh.Adapters.TimeLineAdapter;
import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.Interfaces.TimeLine;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.Dorehami;
import ir.ac.kntu.patogh.Utils.Event;
import ir.ac.kntu.patogh.Utils.TimeLineArrow;
import ir.ac.kntu.patogh.Utils.TimeLineEvent;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CalendarFragment extends Fragment implements TimeLineAdapter.TimeLineAdapterOnClickHandler {

    private CalendarViewModel calendarViewModel;
    private SharedPreferences sharedPreferences;
    private String baseURL = "http://patogh.potatogamers.ir:7701/api/";
    private ArrayList<TimeLine> timeLineData;
    private TimeLineAdapter timeLineAdapter;

    @BindView(R.id.rv_time_line)
    RecyclerView rvTimeLine;
    @BindView(R.id.swipe_time_line)
    SwipeRefreshLayout swipeContainer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel =
                ViewModelProviders.of(this).get(CalendarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, root);
        timeLineData = new ArrayList<>();
        sharedPreferences = getActivity()
                .getSharedPreferences("TokenPref", 0);
        setLocale("fa");
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvTimeLine.setLayoutManager(layoutManager);

        rvTimeLine.setHasFixedSize(true);
        rvTimeLine.setItemAnimator(new SlideInLeftAnimator());
        timeLineAdapter = new TimeLineAdapter(this);
        rvTimeLine.setAdapter(timeLineAdapter);
        getHistory();

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHistory();
            }
        });

        return root;
    }

    private void getHistory() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .build();
        Gson gson = new Gson();
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        String token = sharedPreferences.getString("Token", "none");
        if (token.equals("none")) {
            Toast.makeText(getContext(), "توکن شما پایان یافته.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }

        patoghApi.getJoinedDorehami("Bearer " + token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println(response.code());
                    String res = response.body().string();
                    if (response.code() == 200) {
                        swipeContainer.setRefreshing(false);
                        timeLineData.clear();
                        timeLineAdapter.clear();
                        JsonObject jsonObject1 = new Gson().fromJson(res, JsonObject.class);
                        String returnValue = jsonObject1.get("returnValue").toString();
                        Type dorehamiType = new TypeToken<ArrayList<Dorehami>>() {
                        }.getType();
                        ArrayList<Dorehami> dorehamis = gson.fromJson(returnValue, dorehamiType);
                        for (Dorehami dorehami : dorehamis) {
                            timeLineData.add(new TimeLineArrow());
                            timeLineData.add(new TimeLineEvent(dorehami.getName()
                                    , dorehami.getStartTime()
                                    , dorehami.getThumbnailId()));
                        }
                        timeLineAdapter.addAll(timeLineData);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getContext().getResources().updateConfiguration(config,
                getContext().getResources().getDisplayMetrics());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLocale("fa");
    }

    @Override
    public void onClick(Event selectedEvent) {

    }
}