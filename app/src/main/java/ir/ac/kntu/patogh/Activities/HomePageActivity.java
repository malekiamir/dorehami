package ir.ac.kntu.patogh.Activities;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.KeyboardUtils;
import ir.ac.kntu.patogh.ui.add.AddFragment;
import ir.ac.kntu.patogh.ui.calendar.CalendarFragment;
import ir.ac.kntu.patogh.ui.home.HomeFragment;


public class HomePageActivity extends AppCompatActivity {

    @BindView(R.id.edt_home_page_search_bar)
    EditText edtSearch;
    @BindView(R.id.btn_img_search_bar_search)
    ImageButton btnImgSearch;
    @BindView(R.id.btn_img_search_bar_sort)
    ImageButton btnImgSort;
    @BindView(R.id.btn_img_search_bar_cancel)
    ImageButton btnImgCancel;
    @BindView(R.id.nav_view)
    BottomNavigationView navBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);
        edtSearch.setOnFocusChangeListener((view, b) -> {
            if (b) {
                edtSearch.setText(null);
                btnImgSort.setVisibility(View.INVISIBLE);
                btnImgCancel.setVisibility(View.VISIBLE);
            }
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
//                navController.navigate(destination.getId());
                System.out.println(destination.getLabel());
            }
        });

        navBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        navBottom.setItemTextColor(ColorStateList.valueOf(R.drawable.nav_home_color));
                    case R.id.navigation_add:
                        navBottom.setItemTextColor(ColorStateList.valueOf(R.drawable.nav_add_color));
                    case R.id.navigation_profile:
                        navBottom.setItemTextColor(ColorStateList.valueOf(R.drawable.nav_profile_color));
                    case R.id.navigation_calendar:
                        navBottom.setItemTextColor(ColorStateList.valueOf(R.drawable.nav_calendar_color));
                }
                return true;
            }
        });

    }

    public void searchBarClickHandler(View view) {
        if (view.getId() == R.id.btn_img_search_bar_sort) {
            Toast.makeText(this, "sort", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.btn_img_search_bar_search) {
            Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.btn_img_search_bar_cancel) {
            btnImgSort.setVisibility(View.VISIBLE);
            btnImgCancel.setVisibility(View.INVISIBLE);
            edtSearch.clearFocus();
            edtSearch.setText(R.string.edt_home_page_search_hint);
            KeyboardUtils.hideKeyboard(this);
        }
    }
}

