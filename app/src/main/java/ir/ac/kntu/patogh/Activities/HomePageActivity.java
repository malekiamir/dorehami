package ir.ac.kntu.patogh.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.ui.add.AddFragment;
import ir.ac.kntu.patogh.ui.calendar.CalendarFragment;
import ir.ac.kntu.patogh.ui.home.HomeFragment;
import ir.ac.kntu.patogh.ui.profile.ProfileFragment;


public class HomePageActivity extends AppCompatActivity {

    @BindView(R.id.nav_view)
    BottomNavigationView navBottom;
    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new AddFragment();
    final Fragment fragment3 = new CalendarFragment();
    final Fragment fragment4 = new ProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);

        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.nav_host_fragment, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment1, "1").commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                if (active == fragment1) {
                    return false;
                }
                fm.beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).hide(active).show(fragment1).commit();
                active = fragment1;
                return true;

            case R.id.navigation_add:
                if (active == fragment2)
                    return false;
                if (active == fragment1) {
                    fm.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).hide(active).show(fragment2).commit();
                } else if (active == fragment3 || active == fragment4) {
                    fm.beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).hide(active).show(fragment2).commit();
                }
                active = fragment2;
                return true;

            case R.id.navigation_calendar:
                if (active == fragment3)
                    return false;
                if (active == fragment1 || active == fragment2) {
                    fm.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).hide(active).show(fragment3).commit();
                } else if (active == fragment4) {
                    fm.beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).hide(active).show(fragment3).commit();
                }
                active = fragment3;
                return true;

            case R.id.navigation_profile:
                if (active == fragment4)
                    return false;
                fm.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).hide(active).show(fragment4).commit();
                active = fragment4;
                return true;
        }
        return false;
    };


}

