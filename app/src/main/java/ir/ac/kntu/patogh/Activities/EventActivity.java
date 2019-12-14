package ir.ac.kntu.patogh.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Visibility;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.muddzdev.styleabletoast.StyleableToast;

import org.neshan.core.Bounds;
import org.neshan.core.LngLat;
import org.neshan.layers.VectorElementLayer;
import org.neshan.services.NeshanMapStyle;
import org.neshan.services.NeshanServices;
import org.neshan.styles.MarkerStyle;
import org.neshan.styles.MarkerStyleCreator;
import org.neshan.ui.MapView;
import org.neshan.utils.BitmapUtils;
import org.neshan.vectorelements.Marker;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.ApiDataTypes.TypeFavDorehamiAdd;
import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.Dorehami;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

public class EventActivity extends AppCompatActivity {

    @BindView(R.id.tv_event_name)
    TextView tvName;
    @BindView(R.id.tv_event_desc)
    TextView tvDesc;
    @BindView(R.id.tv_event_date)
    TextView tvDate;
    @BindView(R.id.tv_event_capacity)
    TextView tvCapacity;
    @BindView(R.id.tv_event_address)
    TextView tvAddress;
    @BindView(R.id.tv_event_location)
    TextView tvCity;
    @BindView(R.id.tv_event_category)
    TextView tvCategory;
    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.tv_map_hint)
    TextView tvMapHint;
    @BindView(R.id.img_event)
    ImageView imgEvent;
    @BindView(R.id.progress_event)
    ProgressBar progressBar;
    @BindView(R.id.btn_img_event_like)
    LikeButton likeButton;
    VectorElementLayer markerLayer;
    SharedPreferences sharedPreferences;
    private String eventId = "";
    private AlertDialog dialog;
    private String baseURL = "http://eg.potatogamers.ir:7701/api/";
    boolean success;
    boolean isLiked;
    boolean isJoined;

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
        Sprite threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);
        getDetail(eventId);
        ExtendedFloatingActionButton fab = findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().hasExtra("event_id")) {
                    String dorehamiId = getIntent().getStringExtra("event_id");
                    if (fab.getText().toString().contains("شرکت")) {
                        joinDorehami(dorehamiId);
                        fab.setText("شما عضو رویداد هستید");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            fab.setIcon(getDrawable(R.drawable.ic_done_white_48dp));
                        }
                    } else {
                        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_leave_event, null);

                        AlertDialog.Builder dialogBuilder  =
                                new AlertDialog.Builder(EventActivity.this)
                                        .setView(customLayout);
                        dialog = dialogBuilder.create();
                        dialog.show();

                        customLayout.findViewById(R.id.dialog_accept).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                leaveDorehami(dorehamiId);
                                fab.setText("شرکت در رویداد");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    fab.setIcon(getDrawable(R.drawable.ic_add));
                                }
                                dialog.dismiss();
                            }
                        });

                        customLayout.findViewById(R.id.dialog_reject).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                    }
                }
            }
        });
        if (getIntent().hasExtra("event_liked")) {
            likeButton.setLiked(getIntent().getBooleanExtra("event_liked", false));
        }
        likeButton.setEnabled(true);
        likeButton.bringToFront();
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                favDorehamiAdd(eventId);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                favDorehamiRemove(eventId);
            }
        });
        GestureDetector gestureDetector = new GestureDetector(this, new SingleTapConfirm());
        map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    Uri geoLocationUri = Uri.parse("geo:" + 0 + "," + 0 + "?q="
                            + map.getFocalPointPosition().getY() + ","
                            + map.getFocalPointPosition().getX());
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
        if(getIntent().hasExtra("event_name")) {
            tvName.setText(getIntent().getStringExtra("event_name"));
        }
        if(getIntent().hasExtra("event_desc")) {
            tvDesc.setText(getIntent().getStringExtra("event_desc"));
        }
        if(getIntent().hasExtra("event_date")) {
            tvDate.setText(getIntent().getStringExtra("event_date"));
        }
        if(getIntent().hasExtra("event_id")) {
            eventId = getIntent().getStringExtra("event_id");
        }
        if(getIntent().hasExtra("event_capacity")) {
            tvCapacity.setText("ظرفیت باقی مانده : " + getIntent().getStringExtra("event_capacity") + " نفر");
        }
        if(getIntent().hasExtra("class") && getIntent().getStringExtra("class").equals("favorite")) {
            likeButton.setLiked(true);
            isLiked=true;
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

        if (id == R.id.action_share) {
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
                .baseUrl(baseURL)
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
                } else {
                    System.out.println("response code not 200" + " " + id + " " + response.code());
                    System.out.println(response.message());
                    new StyleableToast
                            .Builder(EventActivity.this)
                            .text("خطایی رخ داده")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.argb(255, 255, 94, 100))
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("no responseeeee");
                new StyleableToast
                        .Builder(EventActivity.this)
                        .text("لطفا اتصال اینترنت خود را بررسی نمایید")
                        .textColor(Color.WHITE)
                        .backgroundColor(Color.argb(255, 255, 94, 100))
                        .show();
            }
        });
    }

    public void leaveDorehami(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
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
        patoghApi.joinDorehamiRemove("Bearer " + token, requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    System.out.println("removed event with id : " + id);
                } else {
                    System.out.println("response code not 200" + " " + id + " " + response.code());
                    System.out.println(response.message());
                    new StyleableToast
                            .Builder(EventActivity.this)
                            .text("خطایی رخ داده")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.argb(255, 255, 94, 100))
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("no responseeeee");
                new StyleableToast
                        .Builder(EventActivity.this)
                        .text("لطفا اتصال اینترنت خود را بررسی نمایید")
                        .textColor(Color.WHITE)
                        .backgroundColor(Color.argb(255, 255, 94, 100))
                        .show();
            }
        });
    }

    public void downloadImage(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
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
        patoghApi.downloadImage("Bearer " + token, requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    System.out.println("downloaded : " + id);
                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                    Glide.with(getApplicationContext())
                            .load(bmp)
                            .signature(new ObjectKey(id))
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(imgEvent);
                    progressBar.setVisibility(View.GONE);
                } else {
                    System.out.println("failed to download " + id + " " + response.code());
                    System.out.println(response.message());
                    new StyleableToast
                            .Builder(EventActivity.this)
                            .text("خطایی رخ داده")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.argb(255, 255, 94, 100))
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("no responseeeee");
                new StyleableToast
                        .Builder(EventActivity.this)
                        .text("لطفا اتصال اینترنت خود را بررسی نمایید")
                        .textColor(Color.WHITE)
                        .backgroundColor(Color.argb(255, 255, 94, 100))
                        .show();
            }
        });
    }

    public void getDetail(String id) {
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

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json")
                , gson.toJson(new TypeFavDorehamiAdd(id)
                ));

        patoghApi.getDetail("Bearer " + token, requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String res = response.body().string();
                    JsonObject jsonObject1 = new Gson().fromJson(res, JsonObject.class);
                    String returnValue = jsonObject1.get("returnValue").toString();
                    Type dorehamiType = new TypeToken<Dorehami>() {
                    }.getType();
                    Dorehami dorehami = gson.fromJson(returnValue, dorehamiType);
                    tvDesc.setText(dorehami.getDescription());
                    tvAddress.setText(dorehami.getAddress());
                    mapConfiguration(new LngLat(Double.valueOf(dorehami.getLongitude())
                            , Double.valueOf(dorehami.getLatitude())));
                    isJoined = dorehami.isJoined();
                    isLiked = dorehami.isFavorited();
                    tvCity.setText(dorehami.getProvince());
//                    tvCategory.setText();
                    SimpleDateFormat readingFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    try {
                        Date dateStart = readingFormat.parse(dorehami.getStartTime());
                        PersianDate persianDateStart = new PersianDate(dateStart);
                        PersianDateFormat pdformater = new PersianDateFormat("l j F H:i");
                        String startDate = pdformater.format(persianDateStart);
                        tvDate.setText(startDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    tvCapacity.setText(String.format("ظرفیت باقی مانده : %d نفر", dorehami.getSize()));
                    ExtendedFloatingActionButton fab = findViewById(R.id.fab);
                    if (isJoined) {
                        fab.setText("شما عضو رویداد هستید");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            fab.setIcon(getDrawable(R.drawable.ic_done_white_48dp));
                        }
                    } else {
                        fab.setText("شرکت در رویداد");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            fab.setIcon(getDrawable(R.drawable.ic_add));
                        }
                    }
                    downloadImage(dorehami.getImagesIds()[0]);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void mapConfiguration(LngLat focalPoint) {
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
    }

    boolean favDorehamiAdd(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://eg.potatogamers.ir:7701/api/")
                .build();
        Gson gson = new Gson();
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        String token = sharedPreferences.getString("Token", "none");
        if (token.equals("none")) {
            return false;
        }
        TypeFavDorehamiAdd te;
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json")
                , gson.toJson(te = new TypeFavDorehamiAdd(id)
                ));

        Log.d("@@@@@@@@@", te.getIdString());
        patoghApi.favDorehamiAdd("Bearer " + token, requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    System.out.println("liked event with id : " + id);
                    success = true;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                success = false;
            }
        });
        return success;
    }

    boolean favDorehamiRemove(String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://eg.potatogamers.ir:7701/api/")
                .build();
        Gson gson = new Gson();
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        String token = sharedPreferences.getString("Token", "none");
        if (token.equals("none")) {
            return false;
        }
        TypeFavDorehamiAdd te;
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json")
                , gson.toJson(te = new TypeFavDorehamiAdd(id)
                ));

        Log.d("@@@@@@@@@", te.getIdString());
        patoghApi.favDorehamiRemove("Bearer " + token, requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    System.out.println("disliked event with id : " + id);
                    success = true;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                success = false;
            }
        });
        return success;
    }


}
