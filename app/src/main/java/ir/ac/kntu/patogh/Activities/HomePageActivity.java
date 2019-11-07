package ir.ac.kntu.patogh.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ir.ac.kntu.patogh.R;

public class HomePageActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_homepage);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_add:
                        Toast.makeText(HomePageActivity.this,"add event",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_calendar:
                        Toast.makeText(HomePageActivity.this,"this is calendar",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_profile:
                        Toast.makeText(HomePageActivity.this,"your profile",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_home:
                        Toast.makeText(HomePageActivity.this,"this is home page",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }
}
