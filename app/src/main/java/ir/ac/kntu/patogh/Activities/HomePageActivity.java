package ir.ac.kntu.patogh.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.KeyboardUtils;

public class HomePageActivity extends AppCompatActivity {

    @BindView(R.id.edt_home_page_search_bar)
    EditText edtSearch;
    @BindView(R.id.btn_img_search_bar_search)
    ImageButton btnImgSearch;
    @BindView(R.id.btn_img_search_bar_sort)
    ImageButton btnImgSort;
    @BindView(R.id.btn_img_search_bar_cancel)
    ImageButton btnImgCancel;
    @BindView(R.id.nav_bottom_home)
    BubbleNavigationConstraintView navBottom;


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

