package ir.ac.kntu.patogh.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.muddzdev.styleabletoast.StyleableToast;
import com.mukesh.OtpView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.IOException;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.ApiDataTypes.TypeAuthentication;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setEnterTransition(new Explode());
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
//                bundle.getBundle("phoneNumber").toString();
        setContentView(R.layout.activity_phone_verification);
        ButterKnife.bind(this);
        Glide.with(this.getApplicationContext())
                .load(R.drawable.back)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(7, 3)))
                .into((ImageView) findViewById(R.id.img_phone_verification_background));
    }

    public void clickHandler(View view) {
        if (view.getId() == R.id.btn_phoneverification_submit) {
            btnSubmit.startAnimation();
            System.out.println(edtVerification.getText().toString());
            if (checkVerificationCode()) {
//                btnSubmit.doneLoadingAnimation(Color.rgb(100,50,100)
//                        , BitmapFactory.decodeResource(getResources(), R.drawable.back));
            }

        }
    }

    private boolean checkVerificationCode() {
        String verificationCode = edtVerification.getText().toString();
        if (verificationCode.equals("")) {
            Animation shake = AnimationUtils.loadAnimation(PhoneVerificationActivity.this, R.anim.shake);
            edtVerification.startAnimation(shake);
            new StyleableToast
                    .Builder(this)
                    .text("مقدار کد نمی تواند خالی باشد")
                    .textColor(Color.WHITE)
                    .backgroundColor(Color.argb(255, 255, 94, 100))
                    .show();
            btnSubmit.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnSubmit.revertAnimation();
                }
            }, 500);
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
            btnSubmit.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnSubmit.revertAnimation();
                }
            }, 500);
            return false;
        }
        return authenticate(edtVerification.getText().toString());
    }

    private boolean authenticate(String code) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://185.252.30.32:7700/api/")
                .build();
        Gson gson = new Gson();
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json")
                , gson.toJson(new TypeAuthentication(phoneNumber, code)));
        patoghApi.authenticate(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Response<ResponseBody> saveResponse = response;
                    String responseBody = response.body().string();
                    Log.d("~~~~~~~~~~~~~~~~~", responseBody);
                    if (!responseBody.contains("value")) {
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
                        JsonObject jsonObject1 = new Gson().fromJson(responseBody, JsonObject.class);
                        String returnValue = jsonObject1.get("returnValue").toString();
                        JsonObject jsonObject2 = new Gson().fromJson(returnValue, JsonObject.class);
                        String token = jsonObject2.get("value").getAsString();
                        success = true;
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
    }
}

