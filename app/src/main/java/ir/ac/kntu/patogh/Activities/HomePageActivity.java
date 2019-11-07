package ir.ac.kntu.patogh.Activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.R;

public class HomePageActivity extends AppCompatActivity {

    @BindView(R.id.edt_home_page_search_bar) EditText edtSearch;
    @BindView(R.id.btn_img_search_bar_search) ImageButton btnImgSearch;
    @BindView(R.id.btn_img_search_bar_sort) ImageButton btnImgSort;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        ButterKnife.bind(this);
        edtSearch.setOnFocusChangeListener((view, b) -> {
            if (b) {
                edtSearch.setHint(null);
            }
        });

//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_homepage);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch(item.getItemId()){
//                    case R.id.action_add:
//                        Toast.makeText(HomePageActivity.this,"add event",Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.action_calendar:
//                        Toast.makeText(HomePageActivity.this,"this is calendar",Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.action_profile:
//                        Toast.makeText(HomePageActivity.this,"your profile",Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.action_home:
//                        Toast.makeText(HomePageActivity.this,"this is home page",Toast.LENGTH_SHORT).show();
//                        break;
//                }
//                return true;
//            }
//        });
    }
}
