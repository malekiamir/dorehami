package ir.ac.kntu.patogh.ui.home;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.transition.Visibility;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ir.ac.kntu.patogh.Activities.EventActivity;
import ir.ac.kntu.patogh.Activities.MainActivity;
import ir.ac.kntu.patogh.Adapters.EventAdapter;
import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.Dorehami;
import ir.ac.kntu.patogh.Utils.Event;
import ir.ac.kntu.patogh.Utils.KeyboardUtils;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment implements View.OnClickListener, EventAdapter.EventAdapterOnClickHandler {

    private HomeViewModel homeViewModel;
    @BindView(R.id.edt_home_page_search_bar)
    EditText edtSearch;
    @BindView(R.id.btn_img_search_bar_search)
    ImageButton btnImgSearch;
    @BindView(R.id.btn_img_search_bar_sort)
    ImageButton btnImgSort;
    @BindView(R.id.btn_img_search_bar_cancel)
    ImageButton btnImgCancel;
    @BindView(R.id.rv_events)
    RecyclerView rvEvents;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    private EventAdapter eventAdapter;
    private Unbinder unbinder;
    private SharedPreferences sharedPreferences;
    private ArrayList<Event> events;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        events = new ArrayList<>();
        setupWindowAnimations();
        unbinder = ButterKnife.bind(this, root);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDorehami();
            }
        });
        swipeContainer.setColorSchemeResources(R.color.patoghYellow, R.color.patoghBlue);
        sharedPreferences = getActivity()
                .getSharedPreferences("TokenPref",0);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvEvents.setLayoutManager(layoutManager);

        rvEvents.setHasFixedSize(true);
        rvEvents.setItemAnimator(new SlideInLeftAnimator());
        eventAdapter = new EventAdapter(this);

        AlphaInAnimationAdapter a = new AlphaInAnimationAdapter(eventAdapter);
        a.setDuration(200);
        a.setFirstOnly(false);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(a);
        scaleInAnimationAdapter.setDuration(150);
        scaleInAnimationAdapter.setFirstOnly(false);
        rvEvents.setAdapter(scaleInAnimationAdapter);
        loadEventsData();


        edtSearch.setOnFocusChangeListener((view, b) -> {
            if (b) {
                edtSearch.setText(null);
                btnImgSort.setVisibility(View.GONE);
                btnImgCancel.setVisibility(View.VISIBLE);
            }
        });
        btnImgCancel.setOnClickListener(this);
        btnImgSearch.setOnClickListener(this);
        btnImgSort.setOnClickListener(this);
        loadEventsData();
        return root;
    }

    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionSet transitionSet = new TransitionSet();
            transitionSet.setOrdering(TransitionSet.ORDERING_TOGETHER);
            Slide slideIn = new Slide(Gravity.BOTTOM);
            Fade fade = new Fade(Visibility.MODE_IN);
            transitionSet.addTransition(fade);
            transitionSet.addTransition(slideIn);
            transitionSet.setDuration(300);
            getActivity().getWindow().setReenterTransition(transitionSet);

            TransitionSet set = new TransitionSet();
            set.setOrdering(TransitionSet.ORDERING_TOGETHER);
            Slide slide = new Slide(Gravity.BOTTOM);
            Fade fadeOut = new Fade(Visibility.MODE_OUT);
            set.addTransition(fadeOut);
            set.addTransition(slide);
            set.setDuration(700);
            getActivity().getWindow().setExitTransition(set);
        }
    }

    public String generateRandomString(int length) {
        String out = "";
        String[] alphabet = {"ا", "ب", "ی", "ت", "س", "ج", "م", "د", "ر"};

        for (int i = 0; i < length; i++) {
            out += alphabet[(int) (Math.random() * 9)];
        }
        return out;
    }

    private void loadEventsData() {
        showEventDataView();
        for (int i = 0; i < 20; i++) {
            events.add(new Event(generateRandomString(5), generateRandomString(25)
                    , "98/08/18", "ظرفیت : " + (int) (Math.random() * 20 + 10) + "","1"));
        }
        eventAdapter.setEventData(events);
    }

    private void showEventDataView() {
        rvEvents.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_img_search_bar_sort) {
            Toast.makeText(view.getContext(), "sort", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.btn_img_search_bar_search) {
            Toast.makeText(view.getContext(), "search", Toast.LENGTH_SHORT).show();
            getDorehami();
        } else if (view.getId() == R.id.btn_img_search_bar_cancel) {
            btnImgSort.setVisibility(View.VISIBLE);
            btnImgCancel.setVisibility(View.GONE);
            edtSearch.clearFocus();
            edtSearch.setText(R.string.edt_home_page_search_hint);
            KeyboardUtils.hideKeyboard(this.getActivity());
        }
    }

    public void getDorehami() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://eg.potatogamers.ir:7701/api/")
                .build();
        Gson gson = new Gson();
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        String token = sharedPreferences.getString("Token", "none");
        if (token.equals("none")) {
            Toast.makeText(getContext(), "توکن شما پایان یافته.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }

        patoghApi.getDorehami("Bearer " + token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    eventAdapter.clear();
                    System.out.println(response.body());
                    String res = response.body().string();
                    System.out.println(res);
                    JsonObject jsonObject1 = new Gson().fromJson(res, JsonObject.class);
                    String returnValue = jsonObject1.get("returnValue").toString();
                    Type dorehamiType = new TypeToken<ArrayList<Dorehami>>(){}.getType();
                    ArrayList<Dorehami> dorehamis = gson.fromJson(returnValue, dorehamiType);
                    for (Dorehami dorehami : dorehamis) {
                        System.out.println(dorehami.toString());
                        events.add(new Event(dorehami.getName(), dorehami.getDescription()
                                , dorehami.getStartTime(),dorehami.getSize()+"", dorehami.getId()));
                    }
                    eventAdapter.addAll(events);
                    swipeContainer.setRefreshing(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "shit we should work on it", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(Event selectedEvent, TextView textView, ImageView imageView) {
        Context context = getContext();
        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtra("event_name", selectedEvent.getName());
        intent.putExtra("event_desc", selectedEvent.getDesc());
        intent.putExtra("event_date", selectedEvent.getDate());
        intent.putExtra("event_capacity", selectedEvent.getCapacity());
        intent.putExtra("event_id", selectedEvent.getId());
        Pair[] pairs = new Pair[2];
        pairs[0] = new Pair<View, String>(textView, ViewCompat.getTransitionName(textView));
        pairs[1] = new Pair<View, String>(imageView, ViewCompat.getTransitionName(imageView));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(getActivity(), pairs);
            context.startActivity(intent, options.toBundle());
        }

    }
}