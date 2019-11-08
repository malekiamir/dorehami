package ir.ac.kntu.patogh.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.muddzdev.styleabletoast.StyleableToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.R;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static android.app.ActivityOptions.makeSceneTransitionAnimation;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.edt_mainpage_phonenumber)
    EditText edtPhone;
    @BindView(R.id.btn_mainpage_submit)
    Button btnSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Glide.with(this.getApplicationContext())
                .load(R.drawable.back)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(7, 3)))
                .into((ImageView) findViewById(R.id.img_mainpage_background));
        edtPhone.setOnFocusChangeListener((view, b) -> {
            if (b) {
                edtPhone.setHint(null);
            }
        });
    }

    public void clickHandler(View view) {
        if (view.getId() == R.id.btn_mainpage_submit) {
            if (checkPhone()) {
                Intent intent = new Intent(MainActivity.this, PhoneVerificationActivity.class);
//            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                startActivity(intent);
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
        return true;
    }
}
