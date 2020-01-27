package ir.ac.kntu.patogh.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.muddzdev.styleabletoast.StyleableToast;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DeleteAccount extends AppCompatActivity {

    // @BindView(R.id.btn_delete_acc)
    Button deleteAcc;

    private SharedPreferences sharedPreferences;
    private String baseURL = "http://patogh.potatogamers.ir:7701/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        Toolbar toolbar = findViewById(R.id.toolbar_setting_deleteacc);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("حذف حساب");
        ButterKnife.bind(this);
        sharedPreferences = DeleteAccount.this
                .getSharedPreferences("TokenPref", 0);
        deleteAcc = findViewById(R.id.btn_delete_acc);

        deleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteImage();
                callBroadCast();
                deleteUser();
            }
        });
//        deleteImage();
//        callBroadCast();
    }

    private void deleteUser() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .build();
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        String token = sharedPreferences.getString("Token", "none");

        patoghApi.deleteUser("Bearer " + token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.code() == 200) {
                        final SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Token", null);
                        editor.apply();
                        GoToActivityAsNewTask(DeleteAccount.this,MainActivity.class);
                    } else {
                        new StyleableToast
                                .Builder(DeleteAccount.this)
                                .text("در روند حذف مشکلی رخ داده")
                                .textColor(Color.WHITE)
                                .backgroundColor(Color.argb(255, 124, 179, 66))
                                .show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

//    public void deleteAppFolder(File file) throws IOException {
//        file.delete();
//        file.getCanonicalFile().delete();
//    }

    public void deleteImage() {
        String file_dj_path = Environment.getExternalStorageDirectory() + "/PATOGH/Pictures/profile.jpg";
        File fdelete = new File(file_dj_path);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Log.e("-->", "file Deleted :" + file_dj_path);
                callBroadCast();
            } else {
                Log.e("-->", "file not Deleted :" + file_dj_path);
            }
        }
    }

    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                /*
                 *   (non-Javadoc)
                 * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                 */
                public void onScanCompleted(String path, Uri uri) {
                    Log.e("ExternalStorage", "Scanned " + path + ":");
                    Log.e("ExternalStorage", "-> uri=" + uri);
                }
            });
        } else {
            Log.e("-->", " < 14");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;

    }

    public static void GoToActivityAsNewTask(Activity context, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        context.finish();

    }

}