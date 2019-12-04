package ir.ac.kntu.patogh.Activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.transition.Visibility;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.neshan.core.Bounds;
import org.neshan.core.LngLat;
import org.neshan.layers.VectorElementLayer;
import org.neshan.services.NeshanMapStyle;
import org.neshan.services.NeshanServices;
import org.neshan.styles.AnimationStyle;
import org.neshan.styles.AnimationStyleBuilder;
import org.neshan.styles.AnimationType;
import org.neshan.styles.MarkerStyle;
import org.neshan.styles.MarkerStyleCreator;
import org.neshan.ui.MapView;
import org.neshan.utils.BitmapUtils;
import org.neshan.vectorelements.Marker;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.R;

public class EventActivity extends AppCompatActivity {

    @BindView(R.id.tv_event_name)
    TextView tvName;
    @BindView(R.id.tv_event_desc)
    TextView tvDesc;
    @BindView(R.id.tv_event_date)
    TextView tvDate;
    @BindView(R.id.map)
    MapView map;
    VectorElementLayer markerLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        setupWindowAnimations();
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
        LngLat focalPoint = new LngLat(51.336434, 35.6990015);
        map.setFocalPointPosition(focalPoint, 1);
        map.setZoom(15, 1);
        markerLayer = NeshanServices.createVectorElementLayer();
        map.getLayers().add(NeshanServices.createBaseMap(NeshanMapStyle.NESHAN));
        map.getLayers().add(markerLayer);
        map.getOptions().setPanBounds(new Bounds(focalPoint, focalPoint));
        MarkerStyleCreator markStCr = new MarkerStyleCreator();
        markStCr.setSize(20f);
        markStCr.setBitmap(BitmapUtils.createBitmapFromAndroidBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_marker)));
        MarkerStyle markSt = markStCr.buildStyle();
        Marker marker = new Marker(focalPoint, markSt);
        markerLayer.add(marker);
        GestureDetector gestureDetector = new GestureDetector(this, new SingleTapConfirm());
        map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    Uri geoLocationUri = Uri.parse("geo:" + 0 + "," + 0 + "q?=" + focalPoint.getY() + "," + focalPoint.getX() );
                    Intent googleMapIntent = new Intent(Intent.ACTION_VIEW, geoLocationUri);
                    googleMapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(googleMapIntent);
                    return true;
                }
                return false;
            }
        });
    }

    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.setDuration(300);
            getWindow().setEnterTransition(fade);

            Fade fadeOut = new Fade(Visibility.MODE_OUT);
            fadeOut.setDuration(300);
            getWindow().setExitTransition(fadeOut);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        map.setVisibility(View.GONE);
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
                if (item.getTitle().equals("heart")) {
                    item.setIcon(getDrawable(R.drawable.ic_heart_1));
                    item.setTitle("heart1");
                } else {
                    item.setIcon(getDrawable(R.drawable.ic_heart_toolbar));
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

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }

}
