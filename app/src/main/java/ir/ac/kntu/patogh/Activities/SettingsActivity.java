package ir.ac.kntu.patogh.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.EditUserInfoDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SettingsActivity extends AppCompatActivity {

//    @BindView(R.id.edt_user_edit_firstname)
//    com.rengwuxian.materialedittext.MaterialEditText editFirstName;
    @BindView(R.id.img_profile_pic)
    ImageView profilePic;
    @BindView(R.id.btn_edit_user_info)
    Button editInfoButton;
    @BindView(R.id.btn_log_out)
    Button logOutButton;
//    @BindView(R.id.btn_cancel)
//    Button cancelEdit;
    @BindView(R.id.btn_change_profile)
    ImageButton changeProfile;
    private SharedPreferences sharedPreferences;
    private AlertDialog dialog;
    private AlertDialog exitDialog;
    MaterialEditText editFirstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);
        sharedPreferences = getApplicationContext()
                .getSharedPreferences("TokenPref", 0);
        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePicker.Companion.with(SettingsActivity.this)
                        .crop(4, 4)                    //Crop image(Optional), Check Customization for more option
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
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.setContentView(R.layout.dialog_edit_user_info);
                dialog.show();
                dialog.getWindow().setAttributes(lp);
                WindowManager.LayoutParams lpp = dialog.getWindow().getAttributes();
                lpp.dimAmount=0.0f;
                dialog.getWindow().setAttributes(lpp);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = data.getData();
            Glide.with(this)
                    .load(fileUri)
                    .into(profilePic);
            //You can get File object from intent
            File file = ImagePicker.Companion.getFile(data);
            //You can also get File Path from intent
            String filepath = ImagePicker.Companion.getFilePath(data);
            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
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



}
