package ir.ac.kntu.patogh.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mahfa.dnswitch.DayNightSwitch;
import com.muddzdev.styleabletoast.StyleableToast;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.EditUserInfoDialog;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.img_profile_pic_setting)
    ImageView profilePic;
    @BindView(R.id.btn_edit_user_info)
    Button editInfoButton;
    @BindView(R.id.btn_log_out)
    Button logOutButton;
    @BindView(R.id.constraintLayout_setting_support)
    ConstraintLayout support;
    @BindView(R.id.btn_setting_support)
    ImageButton supportButton;
    @BindView(R.id.constraintLayout_setting_about_us)
    ConstraintLayout aboutUs;
    @BindView(R.id.btn_setting_about_us)
    ImageButton aboutUsButton;
    @BindView(R.id.btn_change_profile)
    ImageButton changeProfile;
    //@BindView(R.id.day_night_switch)
    //DayNightSwitch dayNightSwitch;
    private SharedPreferences sharedPreferences;
    private AlertDialog dialog;
    private AlertDialog exitDialog;
    MaterialEditText editFirstName;
    private String baseUrl = "http://patogh.potatogamers.ir:7701/api/";
    public Bitmap bitmap;
    public Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);

        } else {
            setTheme(R.style.LightTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        DayNightSwitch dayNightSwitch = findViewById(R.id.day_night_switch);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            dayNightSwitch.setIsNight(true);
        }
//        dayNightSwitch.setListener(new DayNightSwitchListener() {
//            @Override
//            public void onSwitch(boolean is_night) {
//                if(is_night){
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    restartApp();
//                }else{
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                   restartApp();
//                }
//            }
//        });
        dayNightSwitch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {

                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    dayNightSwitch.setIsNight(false);
                    restartApp();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    dayNightSwitch.setIsNight(true);
                    restartApp();
                }

                //  startActivity(new Intent(SettingsActivity.this, SettingsActivity.this.getClass()));
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        ButterKnife.bind(this);
        sharedPreferences = getApplicationContext()
                .getSharedPreferences("TokenPref", 0);
        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePicker.Companion.with(SettingsActivity.this)
                        .crop(4, 4)//Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(512, 512)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePicker.Companion.with(SettingsActivity.this)
                        .crop(4, 4)//Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(512, 512)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View firstCustomLayout = getLayoutInflater().inflate(R.layout.dialog_exit_account, null);

                AlertDialog.Builder dialogBuilder =
                        new AlertDialog.Builder(SettingsActivity.this)
                                .setView(firstCustomLayout);
                exitDialog = dialogBuilder.create();
                exitDialog.show();

                firstCustomLayout.findViewById(R.id.dialog_yes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("Token");
                        editor.apply();
                        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        exitDialog.dismiss();
                    }
                });

                firstCustomLayout.findViewById(R.id.dialog_no).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exitDialog.dismiss();
                    }
                });

            }
        });

        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditUserInfoDialog dialog = new EditUserInfoDialog(SettingsActivity.this);
                dialog.setContentView(R.layout.dialog_edit_user_info);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, SupportActivity.class);
                startActivity(intent);
            }
        });

        supportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, SupportActivity.class);
                startActivity(intent);
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });

        aboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });

        setImage();


    }

    private void restartApp() {
        Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            fileUri = data.getData();
            Glide.with(this)
                    .load(fileUri)
                    .transform(new RoundedCornersTransformation(22, 0))
                    .into(profilePic);
            //You can get File object from intent
            File file = ImagePicker.Companion.getFile(data);
            String filePath = file.getPath();
            bitmap = BitmapFactory.decodeFile(filePath);
            uploadProfile(file);
            //You can also get File Path from intent
            String filepath = ImagePicker.Companion.getFilePath(data);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void uploadProfile(File file) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .build();
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        String token = sharedPreferences.getString("Token", "none");
        if (token.equals("none")) {
            Toast.makeText(SettingsActivity.this, "توکن شما پایان یافته.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        }

        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("File", "File", fileReqBody);


        patoghApi.uploadProfile("Bearer " + token, part).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    System.out.println("uploaded");
                    Toast.makeText(SettingsActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    try {
                        JsonObject jsonObject1 = new Gson().fromJson(response.body().string(), JsonObject.class);
                        String returnValue = jsonObject1.get("returnValue").toString();
                        JsonObject jsonObject2 = new Gson().fromJson(returnValue, JsonObject.class);
                        String imageId = jsonObject2.get("idString").getAsString();
                        makeAppFolder();
                        File filePath = Environment.getExternalStorageDirectory();
                        File dir = new File(filePath.getAbsolutePath() + "/PATOGH/Pictures/");
                        dir.mkdir();
                        File file = new File(dir, "profile.jpg");
                        FileOutputStream outputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        setImage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("failed to upload " + " " + response.code() + " " + response.message());

                    new StyleableToast
                            .Builder(SettingsActivity.this)
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
                        .Builder(SettingsActivity.this)
                        .text("لطفا اتصال اینترنت خود را بررسی نمایید")
                        .textColor(Color.WHITE)
                        .backgroundColor(Color.argb(255, 255, 94, 100))
                        .show();
            }
        });
    }


    public void makeAppFolder() {
        String folder_main = "PATOGH";

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        File f1 = new File(Environment.getExternalStorageDirectory() + "/" + folder_main, "Pictures");
        if (!f1.exists()) {
            f1.mkdirs();
        }
    }

    public void setImage() {
        File file = new File(Environment.getExternalStorageDirectory(), "PATOGH/Pictures/profile.jpg");

        if(!file.exists()){
            Glide.with(this)
                    .load(getResources().getDrawable(R.drawable.ic_profile_pic))
                    .transform(new RoundedCornersTransformation(22, 0))
                    .into(profilePic);
        }else{
        Glide.with(SettingsActivity.this)
                .load(file)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .transform(new RoundedCornersTransformation(22, 0))
                .into(profilePic);
    }}

    @Override
    public void onResume() {
        super.onResume();
        setImage();
    }


}



