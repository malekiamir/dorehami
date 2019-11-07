package ir.ac.kntu.patogh.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mukesh.OtpView;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;

import ir.ac.kntu.patogh.R;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class PhoneVerificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        Glide.with(this.getApplicationContext())
                .load(R.drawable.back)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(7, 3)))
                .into((ImageView) findViewById(R.id.bg));
        OtpView otpView;
        otpView = findViewById(R.id.otp_view);

    }

    public void clickHandler(View view) {
        if(view.getId() == R.id.submit) {
//            Intent intent = new Intent(PhoneVerificationActivity.this, HomePageActivity.class);
            Intent intent = new Intent(PhoneVerificationActivity.this, SignUpActivity.class);
            startActivity(intent);
        }
    }
}
