package ir.ac.kntu.patogh.ui.home;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ir.ac.kntu.patogh.Event;
import ir.ac.kntu.patogh.Activities.EventActivity;
import ir.ac.kntu.patogh.EventAdapter;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.KeyboardUtils;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

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
    private EventAdapter eventAdapter;
    private Unbinder unbinder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, root);

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
                btnImgSort.setVisibility(View.INVISIBLE);
                btnImgCancel.setVisibility(View.VISIBLE);
            }
        });
        btnImgCancel.setOnClickListener(this);
        btnImgSearch.setOnClickListener(this);
        btnImgSort.setOnClickListener(this);
        loadEventsData();
        return root;
    }

    public String generateRandomString(int length) {
        String out = "";
        String []alphabet = {"ا","ب","ی","ت","س","ج","م","د","ر"};

        for (int i = 0; i < length; i++) {
            out += alphabet[(int)(Math.random()*9)];
        }
        return out;
    }

    private void loadEventsData() {
        showEventDataView();
        Event []events = new Event[20];
        for (int i = 0; i < events.length; i++) {
            events[i] = new Event(generateRandomString(5), generateRandomString(25)
                    , "98/08/18", "ظرفیت : "+(int)(Math.random()*20+10)+ "");
        }
//        System.out.println(events.length);
        eventAdapter.setEventData(events);
    }

    private void showEventDataView() {
        /* First, make sure the error is invisible */
//        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        rvEvents.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_img_search_bar_sort) {
            Toast.makeText(view.getContext(), "sort", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.btn_img_search_bar_search) {
            Toast.makeText(view.getContext(), "search", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.btn_img_search_bar_cancel) {
            btnImgSort.setVisibility(View.VISIBLE);
            btnImgCancel.setVisibility(View.INVISIBLE);
            edtSearch.clearFocus();
            edtSearch.setText(R.string.edt_home_page_search_hint);
            KeyboardUtils.hideKeyboard(this.getActivity());
        }
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