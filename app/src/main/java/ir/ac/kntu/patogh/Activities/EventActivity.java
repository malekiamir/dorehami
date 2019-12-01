package ir.ac.kntu.patogh.Activities;

import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.R;

public class EventActivity extends AppCompatActivity {

    @BindView(R.id.event_name)
    TextView tvName;
    @BindView(R.id.event_desc)
    TextView tvDesc;
    @BindView(R.id.event_date)
    TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);
        getIncomingIntent();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getIncomingIntent() {
        if (getIntent().hasExtra("event_name") && getIntent().hasExtra("event_desc")
                && getIntent().hasExtra("event_date") && getIntent().hasExtra("event_capacity")) {
            tvName.setText(getIntent().getStringExtra("event_name"));
            tvDesc.setText(getIntent().getStringExtra("event_desc"));
            tvDate.setText(getIntent().getStringExtra("event_date"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorite) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if(item.getTitle().equals("heart")) {
                    item.setIcon(getDrawable(R.drawable.ic_heart_1));
                    item.setTitle("heart1");
                } else {
                    item.setIcon(getDrawable(R.drawable.ic_heart));
                    item.setTitle("heart");
                }
//                else
//                    item.setIcon(getDrawable(R.drawable.ic_heart));

            }
            Toast.makeText(EventActivity.this, "Liked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
