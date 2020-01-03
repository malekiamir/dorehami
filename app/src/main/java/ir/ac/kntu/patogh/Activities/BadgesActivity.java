package ir.ac.kntu.patogh.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindView;
import ir.ac.kntu.patogh.Adapters.DetailedBadgeAdapter;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.DetailedBadge;
import ir.ac.kntu.patogh.Utils.EqualSpacingItemDecoration;

public class BadgesActivity extends AppCompatActivity {


    private DetailedBadgeAdapter detailedBadgeAdapter;
    RecyclerView rvBadges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges);

        rvBadges = findViewById(R.id.rv_badges_recyclerview);
        Toolbar toolbar = findViewById(R.id.badges_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rvBadges.setLayoutManager(layoutManager);
        detailedBadgeAdapter = new DetailedBadgeAdapter(this);
        rvBadges.setAdapter(detailedBadgeAdapter);
        loadBadges();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void loadBadges() {
        ArrayList<DetailedBadge> badges = new ArrayList<>();
        badges.add(new DetailedBadge(R.drawable.ic_success,"ثبت نام موفق",1));
        badges.add(new DetailedBadge(R.drawable.ic_achievement_boronz,"اولین دورهمی",2));
        badges.add(new DetailedBadge(R.drawable.ic_achievement_silver,"پنج دورهمی",3));
        badges.add(new DetailedBadge(R.drawable.ic_achievement,"ده دورهمی",6));
        badges.add(new DetailedBadge(R.drawable.ic_medal_silver,"برگزاری یک دورهمی",4));
        badges.add(new DetailedBadge(R.drawable.ic_medal_gold,"برگزاری پنج دورهمی",5));
        badges.add(new DetailedBadge(R.drawable.ic_win,"بیست دورهمی",7));
        detailedBadgeAdapter.setEventData(badges);
    }

    private void showEventDataView() {
        rvBadges.setVisibility(View.VISIBLE);
    }

}
