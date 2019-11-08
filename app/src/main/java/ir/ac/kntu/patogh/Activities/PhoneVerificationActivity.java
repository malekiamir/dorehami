package ir.ac.kntu.patogh.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.muddzdev.styleabletoast.StyleableToast;
import com.mukesh.OtpView;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.R;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class PhoneVerificationActivity extends AppCompatActivity {

    @BindView(R.id.edt_verification)
    OtpView edtVerification;
    @BindView(R.id.btn_phoneverification_submit)
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        ButterKnife.bind(this);
        Glide.with(this.getApplicationContext())
                .load(R.drawable.back)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(7, 3)))
                .into((ImageView) findViewById(R.id.img_phone_verification_background));

    }

    public void clickHandler(View view) {
        if(view.getId() == R.id.btn_phoneverification_submit) {
//            Intent intent = new Intent(PhoneVerificationActivity.this, HomePageActivity.class);
            if (checkVerificationCode()) {
                Intent intent = new Intent(PhoneVerificationActivity.this, SignUpActivity.class);
                startActivity(intent);
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
            return false;
        }
        return true;
    }
}
