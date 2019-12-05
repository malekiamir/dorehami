package ir.ac.kntu.patogh.Activities;

import android.animation.TimeInterpolator;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

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

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.ApiDataTypes.TypeFavDorehamiAdd;
import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.Dorehami;
import ir.ac.kntu.patogh.Utils.Event;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EventActivity extends AppCompatActivity {

    @BindView(R.id.tv_event_name)
    TextView tvName;
    @BindView(R.id.tv_event_desc)
    TextView tvDesc;
    @BindView(R.id.tv_event_date)
    TextView tvDate;
    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.tv_map_hint)
    TextView tvMapHint;
    VectorElementLayer markerLayer;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        setupWindowAnimations();
        ButterKnife.bind(this);
        getIncomingIntent();
        sharedPreferences = getApplicationContext()
                .getSharedPreferences("TokenPref", 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getIntent().hasExtra("event_id")) {
                    String dorehamiId = getIntent().getStringExtra("event_id");
                    joinDorehami(dorehamiId);
                }
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
                    Uri geoLocationUri = Uri.parse("geo:" + 0 + "," + 0 + "?q=" + focalPoint.getY() + "," + focalPoint.getX());
                    Intent googleMapIntent = new Intent(Intent.ACTION_VIEW, geoLocationUri);
                    googleMapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(googleMapIntent);
                    return true;
                } else {
                    tvMapHint.setAlpha(1.0f);
                    tvMapHint.setVisibility(View.VISIBLE);
                    tvMapHint.animate().alpha(0.0f).setDuration(2000).setInterpolator(new AnticipateInterpolator()).start();
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

    public void joinDorehami(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://eg.potatogamers.ir:7701/api/")
                .build();
        Gson gson = new Gson();
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        String token = sharedPreferences.getString("Token", "none");
        if (token.equals("none")) {
            Toast.makeText(EventActivity.this, "توکن شما پایان یافته.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(EventActivity.this, MainActivity.class);
            startActivity(intent);
        }

        TypeFavDorehamiAdd te;
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json")
                , gson.toJson(te = new TypeFavDorehamiAdd(id)
                ));

        Log.d("@@@@@@@@@", te.toString());
        patoghApi.joinDorehamiAdd("Bearer " + token, requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    System.out.println("joined event with id : " + id);
                }
                System.out.println("response code not 200" + " " + id + " " + response.code());
                System.out.println(response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("no responseeeee");
            }
        });
    }

}
