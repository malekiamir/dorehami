package ir.ac.kntu.patogh.ui.profile;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ir.ac.kntu.patogh.Activities.EventActivity;
import ir.ac.kntu.patogh.Adapters.EventAdapter;
import ir.ac.kntu.patogh.Adapters.FavoriteAdapter;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.Event;
import ir.ac.kntu.patogh.Utils.FavoriteEvent;
import jp.wasabeef.glide.transformations.BlurTransformation;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.mikhaellopez.circularimageview.CircularImageView;

public class ProfileFragment extends Fragment implements FavoriteAdapter.FavoriteAdapterOnClickHandler {



    @BindView(R.id.rv_favorite_events)
    RecyclerView rvFavoriteEvents;
    private FavoriteAdapter favoriteAdapter;
    private Unbinder unbinder;
    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, root);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        rvFavoriteEvents.setLayoutManager(layoutManager);
        favoriteAdapter = new FavoriteAdapter(this);
        loadEventsData();

        Toolbar toolbar = root.findViewById(R.id.toolbar_profile_page);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        setHasOptionsMenu(true);

        Glide.with(this.getContext())
                .load(R.drawable.sixth_back)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(5, 3)))
                .into((ImageView)root.findViewById(R.id.img_profile_back_pic));

  //      ImageView imageView;
//        imageView = root.findViewById(R.id.img_profile_circlar_pic).bringToFront();

//        Glide.with(this) //1
//                .load(R.drawable.back)
//                .placeholder(R.drawable.back)
//                .error(R.drawable.back)
//                .skipMemoryCache(true) //2
//                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
//                .into((ImageView)root.findViewById(R.id.img_profile_back_pic));
        Glide.with(this) //1
                .load(R.drawable.back)
                .placeholder(R.drawable.back)
                .error(R.drawable.back)
                .circleCrop()
                .skipMemoryCache(true) //2
                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                .into((ImageView)root.findViewById(R.id.img_profile_circlar_pic));
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
        FavoriteEvent []events = new FavoriteEvent[20];
        for (int i = 0; i < events.length; i++) {
            events[i] = new FavoriteEvent(generateRandomString(5), "98/08/18", "ظرفیت : "+(int)(Math.random()*20+10)+ "");
        }
//        System.out.println(events.length);
        favoriteAdapter.setEventData(events);
    }

    private void showEventDataView() {
        /* First, make sure the error is invisible */
//        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        rvFavoriteEvents.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(FavoriteEvent selectedEvent) {
//        Context context = getContext();
//        Intent intent = new Intent(context, EventActivity.class);
//        intent.putExtra("event_name", selectedEvent.getName());
//        intent.putExtra("event_date", selectedEvent.getDate());
//        intent.putExtra("event_capacity", selectedEvent.getCapacity());
//        Pair[] pairs = new Pair[2];
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            context.startActivity(intent);
//        }
    }
}