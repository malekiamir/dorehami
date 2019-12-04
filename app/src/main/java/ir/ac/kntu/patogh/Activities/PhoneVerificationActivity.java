package ir.ac.kntu.patogh.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.muddzdev.styleabletoast.StyleableToast;
import com.mukesh.OtpView;

import java.io.IOException;
import java.util.Objects;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.ApiDataTypes.TypeAuthentication;
import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.R;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PhoneVerificationActivity extends AppCompatActivity {

    @BindView(R.id.edt_verification)
    OtpView edtVerification;
    @BindView(R.id.btn_phoneverification_submit)
    CircularProgressButton btnSubmit;
    boolean success = false;
    @BindView(R.id.tv_phone_verification_timer)
    TextView tvTimer;
    @BindView(R.id.phone_verification_constraint_layout)
    ConstraintLayout layout;

    private SharedPreferences sharedPreferences;


    boolean doubleBackToExitPressedOnce = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setEnterTransition(new Explode());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        ButterKnife.bind(this);

        sharedPreferences = getApplicationContext()
                .getSharedPreferences("TokenPref",MODE_PRIVATE);

        Glide.with(this.getApplicationContext())
                .load(R.drawable.back)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(7, 3)))
                .into((ImageView) findViewById(R.id.img_phone_verification_background));

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvTimer.setText("کد تایید تا " + millisUntilFinished / 1000 + " ثانیه دیگر برای شما ارسال خواهد شد.");
            }

            public void onFinish() {
                tvTimer.setText("در صورت عدم دریافت کد مجددا تلاش نمایید.");
            }
        }.start();
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(edtVerification.hasFocus()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    edtVerification.clearFocus();
                }
                return true;
            }
        });
    }


    public void clickHandler(View view) {
        if (view.getId() == R.id.btn_phoneverification_submit) {
            btnSubmit.startAnimation();
            System.out.println(Objects.requireNonNull(edtVerification.getText()).toString());
            checkVerificationCode();
        }
        if (view.getId() == R.id.btn_phoneverification_edit) {
            Intent intent = new Intent(PhoneVerificationActivity.this, MainActivity.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Fade fade = new Fade();
                getWindow().setEnterTransition(fade);
                getWindow().setExitTransition(fade);
            }
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(
                            PhoneVerificationActivity.this, btnSubmit, Objects.requireNonNull(ViewCompat.getTransitionName(btnSubmit)));
            startActivity(intent, activityOptionsCompat.toBundle());
            finish();
        }
    }

    private boolean checkVerificationCode() {
        String verificationCode = Objects.requireNonNull(edtVerification.getText()).toString();
        if (verificationCode.equals("")) {
            Animation shake = AnimationUtils.loadAnimation(PhoneVerificationActivity.this, R.anim.shake);
            edtVerification.startAnimation(shake);
            new StyleableToast
                    .Builder(this)
                    .text("مقدار کد نمی تواند خالی باشد")
                    .textColor(Color.WHITE)
                    .backgroundColor(Color.argb(255, 255, 94, 100))
                    .show();
            btnSubmit.postDelayed(() -> btnSubmit.revertAnimation(), 500);
            return false;
        } else if (verificationCode.length() != 4) {
            Animation shake = AnimationUtils.loadAnimation(PhoneVerificationActivity.this, R.anim.shake);
            edtVerification.startAnimation(shake);
            new StyleableToast
                    .Builder(this)
                    .text("کد وارد شده صحیح نمی باشد.")
                    .textColor(Color.WHITE)
                    .backgroundColor(Color.argb(255, 255, 94, 100))
                    .show();
            btnSubmit.postDelayed(() -> btnSubmit.revertAnimation(), 500);
            return false;
        }
        return authenticate(edtVerification.getText().toString());
    }

    private boolean authenticate(String code) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://eg.potatogamers.ir:7701/api/")
                .build();
        Gson gson = new Gson();
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json")
                , gson.toJson(new TypeAuthentication(phoneNumber, code)));
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        patoghApi.authenticate(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.code()!=200) {
                        new StyleableToast
                                .Builder(PhoneVerificationActivity.this)
                                .text("کد صحیح نمی باشد.")
                                .textColor(Color.WHITE)
                                .backgroundColor(Color.argb(255, 255, 94, 100))
                                .show();
                        success = false;
                        btnSubmit.revertAnimation();
                    } else {
                        btnSubmit.revertAnimation();
                        JsonObject jsonObject1 = new Gson().fromJson(response.body().string(), JsonObject.class);
                        String returnValue = jsonObject1.get("returnValue").toString();
                        JsonObject jsonObject2 = new Gson().fromJson(returnValue, JsonObject.class);
                        String token = jsonObject2.get("value").getAsString();
                        success = true;
                        editor.putString("Token", token);
                        editor.apply();
                        System.out.println("~~~" + token);
                        System.out.println(response.toString());
                        goToNextPage(token);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                new StyleableToast
                        .Builder(PhoneVerificationActivity.this)
                        .text("لطفا اتصال اینترنت را بررسی نمایید و سپس مجددا تلاش نمایید.")
                        .textColor(Color.WHITE)
                        .backgroundColor(Color.argb(255, 255, 94, 100))
                        .show();
                success = false;

            }
        });

        return success;
    }

    public void goToNextPage(String token) {
        Intent intent = new Intent(PhoneVerificationActivity.this, SignUpActivity.class);
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        intent.putExtra("phoneNumber", phoneNumber);
        intent.putExtra("token", token);
        startActivity(intent);
        finish();
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

