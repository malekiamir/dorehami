package ir.ac.kntu.patogh.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Explode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.muddzdev.styleabletoast.StyleableToast;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.ApiDataTypes.TypeRequestLogin;
import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.Dorehami;
import ir.ac.kntu.patogh.Utils.Event;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.edt_mainpage_phonenumber)
    EditText edtPhone;
    @BindView(R.id.btn_mainpage_submit)
    CircularProgressButton btnSubmit;
    @BindView(R.id.img_mainpage_background)
    ImageView imgBackground;
    @BindView(R.id.main_constraint_layout)
    ConstraintLayout layout;

    boolean success = false;
    private boolean doubleBackToExitPressedOnce = false;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementExitTransition(new Explode());
        }
        super.onCreate(savedInstanceState);
//        if(savedInstanceState!=null && savedInstanceState.containsKey("phoneNumber")) {
//            String phoneNumebr = savedInstanceState.getString("phoneNumber");
//            edtPhone.setText(phoneNumebr);
//        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sharedPreferences = getApplicationContext()
                .getSharedPreferences("TokenPref",0);
        Glide.with(this.getApplicationContext())
                .load(R.drawable.back)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(7, 3)))
                .into((ImageView) findViewById(R.id.img_mainpage_background));
        checkValidToken();
        edtPhone.setOnFocusChangeListener((view, b) -> {
            if (b) {
                edtPhone.setHint(null);
            }
        });
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(edtPhone.hasFocus()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    edtPhone.clearFocus();
                }
                return true;
            }
        });
    }

    public void clickHandler(View view) {
        if (view.getId() == R.id.btn_mainpage_submit) {
            if (checkPhone()) {
                goToNextPage();
            }
        }
    }

    public void checkValidToken() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://eg.potatogamers.ir:7701/api/")
                .build();
        Gson gson = new Gson();
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        String token = sharedPreferences.getString("Token", "none");
        if (token.equals("none")) {
            return;
        }

        patoghApi.getUserDetails("Bearer " + token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println(response.body());
                    String res = response.body().string();
                    System.out.println(res);
                    JsonObject jsonObject1 = new Gson().fromJson(res, JsonObject.class);
                    String returnValue = jsonObject1.get("returnValue").toString();
                    JsonObject jsonObject2 = new Gson().fromJson(returnValue, JsonObject.class);
                    String firstName = jsonObject2.get("firstName").getAsString();
                    if(firstName != null) {
                        Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private boolean checkPhone() {
        String phoneNo = edtPhone.getText().toString();
        if (phoneNo.equals("")) {
            Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
            edtPhone.startAnimation(shake);
            new StyleableToast
                    .Builder(this)
                    .text("لطفا شماره همراه خود را وارد نمایید.")
                    .textColor(Color.WHITE)
                    .backgroundColor(Color.argb(255, 255, 94, 100))
                    .show();
            return false;
        } else if (!phoneNo.startsWith("09") || phoneNo.length() != 11) {
            Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
            edtPhone.startAnimation(shake);
            new StyleableToast
                    .Builder(this)
                    .text("شماره وارد شده صحیح نمی باشد.")
                    .textColor(Color.WHITE)
                    .backgroundColor(Color.argb(255, 255, 94, 100))
                    .show();
            return false;
        }
        btnSubmit.startAnimation();
        return requestLogin(phoneNo);
    }

    private boolean requestLogin(String phoneNumber) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://eg.potatogamers.ir:7701/api/")
                .build();
        Gson gson = new Gson();
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json")
                , gson.toJson(new TypeRequestLogin(phoneNumber)));
        patoghApi.requestLogin(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response.code()==200) {
                        Log.d("~~~~~~~~~~~~~~~~~", Objects.requireNonNull(response.body()).string());
                        success = true;
                        btnSubmit.revertAnimation();
                        goToNextPage();
                    } else {
                        new StyleableToast
                                .Builder(MainActivity.this)
                                .text("لطفا از قطع بودن فیلترشکن اطمینان پیدا کنید.")
                                .textColor(Color.WHITE)
                                .backgroundColor(Color.argb(255, 255, 94, 100))
                                .show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                new StyleableToast
                        .Builder(MainActivity.this)
                        .text("لطفا اتصال اینترنت را بررسی نمایید و سپس مجددا تلاش نمایید.")
                        .textColor(Color.WHITE)
                        .backgroundColor(Color.argb(255, 255, 94, 100))
                        .show();
                btnSubmit.revertAnimation();
                success = false;
            }
        });

        return success;
    }

    public void goToNextPage() {
        Intent intent = new Intent(MainActivity.this, PhoneVerificationActivity.class);
        intent.putExtra("phoneNumber", edtPhone.getText().toString());
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(MainActivity.this
                        , imgBackground
                        , ViewCompat.getTransitionName(imgBackground))
                .makeSceneTransitionAnimation(MainActivity.this
                        , btnSubmit
                        , ViewCompat.getTransitionName(btnSubmit));
        startActivity(intent, options.toBundle());
        finish();
    }



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("phoneNumber", edtPhone.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String phoneNumber = savedInstanceState.getString("phoneNumber");
        edtPhone.setText(phoneNumber);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "برای خروج دوباره دکمه بازگشت را فشار دهید", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }
}
