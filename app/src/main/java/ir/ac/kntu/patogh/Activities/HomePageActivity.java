package ir.ac.kntu.patogh.Activities;

import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.KeyboardUtils;


public class HomePageActivity extends AppCompatActivity {

    @BindView(R.id.nav_view)
    BottomNavigationView navBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//                navController.navigate(destination.getId());
            if (!destination.getLabel().equals("Home")) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                if (!getCurrentFocus().equals(null))
//                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                KeyboardUtils.hideKeyboard(this);
            }
            System.out.println(destination.getLabel());
        });

    }

}

