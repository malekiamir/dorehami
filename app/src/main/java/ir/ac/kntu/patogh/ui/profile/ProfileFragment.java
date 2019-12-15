package ir.ac.kntu.patogh.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import ir.ac.kntu.patogh.Activities.HomePageActivity;
import ir.ac.kntu.patogh.Activities.MainActivity;
import ir.ac.kntu.patogh.Activities.PhoneVerificationActivity;
import ir.ac.kntu.patogh.Activities.SettingsActivity;
import ir.ac.kntu.patogh.Activities.SignUpActivity;
import ir.ac.kntu.patogh.Adapters.BadgeAdapter;
import ir.ac.kntu.patogh.Adapters.FavoriteAdapter;
import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.Badge;
import ir.ac.kntu.patogh.Utils.Dorehami;
import ir.ac.kntu.patogh.Utils.EqualSpacingItemDecoration;
import ir.ac.kntu.patogh.Utils.FavoriteEvent;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileFragment extends Fragment implements FavoriteAdapter.FavoriteAdapterOnClickHandler, BadgeAdapter.BadgeAdapterOnClickHandler {

    @BindView(R.id.rv_profile_page_badges)
    RecyclerView rvBadge;
    @BindView(R.id.rv_favorite_events)
    RecyclerView rvFavoriteEvents;
    @BindView(R.id.tv_profile_page_name)
    TextView nameTextView;
    private FavoriteAdapter favoriteAdapter;
    private BadgeAdapter badgeAdapter;
    private Unbinder unbinder;
    private ProfileViewModel profileViewModel;
    private SharedPreferences sharedPreferences;
    private ArrayList<FavoriteEvent> favoriteEvents;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        favoriteEvents = new ArrayList<>();
        unbinder = ButterKnife.bind(this, root);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        rvFavoriteEvents.setLayoutManager(layoutManager);
        favoriteAdapter = new FavoriteAdapter(this);
        rvFavoriteEvents.setAdapter(favoriteAdapter);
        rvFavoriteEvents.addItemDecoration(new EqualSpacingItemDecoration(40));
        sharedPreferences = getActivity()
                .getSharedPreferences("TokenPref", 0);
        getUserDetails();
        getFavorites();


        Toolbar toolbar = root.findViewById(R.id.toolbar_profile_page);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_history);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        setHasOptionsMenu(true);


        LinearLayoutManager badgeLayoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvBadge.setLayoutManager(badgeLayoutManager);
        badgeAdapter = new BadgeAdapter(this);
        rvBadge.setAdapter(badgeAdapter);
        rvBadge.addItemDecoration(new EqualSpacingItemDecoration(20));
        loadBadges();


        Glide.with(this)
                .load(R.drawable.back)
                .placeholder(R.drawable.back)
                .error(R.drawable.back)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into((ImageView) root.findViewById(R.id.img_profile_circlar_pic));
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            getFavorites();
            return true;
        } else if (item.getItemId() == R.id.action_gear) {
            Context context = getContext();
            Intent intent = new Intent(context, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        getFavorites();
    }


    private void loadBadges() {
        ArrayList<Badge> badges = new ArrayList<>();
        badges.add(new Badge(R.drawable.ic_sport_badges));
        badges.add(new Badge(R.drawable.ic_win));
        badges.add(new Badge(R.drawable.ic_achievement));
        badges.add(new Badge(R.drawable.ic_success));
        badgeAdapter.setEventData(badges);
    }

    private void showEventDataView() {
        rvFavoriteEvents.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(FavoriteEvent selectedEvent) {
        Context context = getContext();
        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtra("event_name", selectedEvent.getName());
        intent.putExtra("event_date", selectedEvent.getDate());
        intent.putExtra("event_capacity", selectedEvent.getCapacity());
        intent.putExtra("event_id", selectedEvent.getId());
        intent.putExtra("class", "favorite");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            context.startActivity(intent);
        }
    }


    @Override
    public void onClick(Badge selectedBadge, ImageView imageView) {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public void getFavorites() {
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

        showEventDataView();
        patoghApi.getFavorites("Bearer " + token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String res = response.body().string();
                    favoriteAdapter.clear();
                    favoriteEvents.clear();
                    JsonObject jsonObject1 = new Gson().fromJson(res, JsonObject.class);
                    String returnValue = jsonObject1.get("returnValue").toString();
                    Type dorehamiType = new TypeToken<ArrayList<Dorehami>>() {
                    }.getType();
                    ArrayList<Dorehami> dorehamis = gson.fromJson(returnValue, dorehamiType);
                    for (Dorehami dorehami : dorehamis) {
                        favoriteEvents.add(new FavoriteEvent(dorehami.getName(), dorehami.getStartTime()
                                , String.format("ظرفیت باقی مانده : %d نفر", dorehami.getSize())
                                , dorehami.getId(), dorehami.getThumbnailId()));
                    }
                    System.out.println(favoriteEvents.size());
                    favoriteAdapter.addAll(favoriteEvents);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getUserDetails() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://eg.potatogamers.ir:7701/api/")
                .build();
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        String token = sharedPreferences.getString("Token", "none");
        if (token.equals("none")) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }

        patoghApi.getUserDetails("Bearer " + token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println(response.body());
                    String res = response.body().string();
                    System.out.println(res);
                    JsonObject jsonObject1 = new Gson().fromJson(res, JsonObject.class);
                    String returnValue = jsonObject1.get("returnValue").toString();
                    JsonObject jsonObject2 = new Gson().fromJson(returnValue, JsonObject.class);
                    String firstName = jsonObject2.get("firstName").getAsString();
                    if (firstName != null) {
                        nameTextView.setText(firstName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
}