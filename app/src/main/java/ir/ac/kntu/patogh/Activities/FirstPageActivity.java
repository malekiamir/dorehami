package ir.ac.kntu.patogh.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.muddzdev.styleabletoast.StyleableToast;

import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.Typewriter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FirstPageActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private SharedPreferences sharedPreferences;
    private String baseUrl = "http://patogh.potatogamers.ir:7701/api/";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        sharedPreferences = getApplicationContext()
                .getSharedPreferences("TokenPref",0);

        Typewriter writer = findViewById(R.id.tv_firstPage_animated_text);
        writer.setText("");
        writer.setCharacterDelay(120);
        writer.animateText("پاتوق");
        checkValidToken();

    }

    private void checkValidToken() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .build();
        long t1 = System.currentTimeMillis();
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        String token = sharedPreferences.getString("Token", "none");
        if (token.equals("none")) {
            long t2 = System.currentTimeMillis();
            if(t2-t1 >= 1450) {
                Intent intent = new Intent(FirstPageActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                new Thread()  {
                    @Override
                    public void run() {
                        try {
                            sleep(1450-t2+t1);
                            Intent intent = new Intent(FirstPageActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
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
                        long t2 = System.currentTimeMillis();
                        if(t2-t1 >= 1450) {
                            Intent intent = new Intent(FirstPageActivity.this, HomePageActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            new Thread()  {
                                @Override
                                public void run() {
                                    try {
                                        sleep(1450-t2+t1);
                                        Intent intent = new Intent(FirstPageActivity.this, HomePageActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                        }
                    } else {
                        long t2 = System.currentTimeMillis();
                        if(t2-t1 >= 1450) {
                            Intent intent = new Intent(FirstPageActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            new Thread()  {
                                @Override
                                public void run() {
                                    try {
                                        sleep(1450-t2+t1);
                                        Intent intent = new Intent(FirstPageActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("exception");
                    long t2 = System.currentTimeMillis();
                    if(t2-t1 >= 1450) {
                        Intent intent = new Intent(FirstPageActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        new Thread()  {
                            @Override
                            public void run() {
                                try {
                                    sleep(1450-t2+t1);
                                    Intent intent = new Intent(FirstPageActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        new StyleableToast
                .Builder(this)
                .text("برای خروج دوباره دکمه بازگشت را فشار دهید")
                .textColor(Color.WHITE)
                .font(R.font.iransans_mobile_font)
                .backgroundColor(Color.argb(250, 30, 30, 30))
                .show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }
}
