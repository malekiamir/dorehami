package ir.ac.kntu.patogh.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.muddzdev.styleabletoast.StyleableToast;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.PhoneNumber;
import ir.ac.kntu.patogh.R;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.ActivityOptions.makeSceneTransitionAnimation;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.edt_mainpage_phonenumber)
    EditText edtPhone;
    @BindView(R.id.btn_mainpage_submit)
    Button btnSubmit;
    @BindView(R.id.img_mainpage_background)
    ImageView imgBackground;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        Glide.with(this.getApplicationContext())
//                .load(R.drawable.back)
//                .apply(RequestOptions.bitmapTransform(new BlurTransformation(7, 3)))
//                .into((ImageView) findViewById(R.id.img_mainpage_background));
        edtPhone.setOnFocusChangeListener((view, b) -> {
            if (b) {
                edtPhone.setHint(null);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void clickHandler(View view) {
        if (view.getId() == R.id.btn_mainpage_submit) {
            if (checkPhone()) {
                Intent intent = new Intent(MainActivity.this, PhoneVerificationActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(MainActivity.this
                                , imgBackground
                                , ViewCompat.getTransitionName(imgBackground))
                        .makeSceneTransitionAnimation(MainActivity.this
                                , btnSubmit
                                , ViewCompat.getTransitionName(btnSubmit));
                startActivity(intent, options.toBundle());
            }
        }
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
        return requestLogin(phoneNo);
    }

    private boolean requestLogin(String phoneNumber) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://185.252.30.32:7700/api/")
                .build();
        Gson gson = new Gson();
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json")
                , gson.toJson(new PhoneNumber(phoneNumber)));
        patoghApi.requestLogin(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d("~~~~~~~~~~~~~~~~~", response.body().string());
                    System.out.println("ineha " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("ridim baba");
            }
        });
        return true;
    }
}
