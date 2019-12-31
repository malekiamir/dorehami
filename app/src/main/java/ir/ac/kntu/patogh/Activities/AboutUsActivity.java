package ir.ac.kntu.patogh.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.R;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class AboutUsActivity extends AppCompatActivity {


    @BindView(R.id.text_about_us)
    TextView aboutUs;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = findViewById(R.id.toolbar_setting_about_us);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("درباره ما");
        ButterKnife.bind(this);
        aboutUs.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
