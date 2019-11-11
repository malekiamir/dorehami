package ir.ac.kntu.patogh.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import ir.ac.kntu.patogh.R;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class SignUpActivity extends AppCompatActivity {

    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";

    @BindView(R.id.edt_signup_name)
    TextInputLayout textInputLayoutName;
    @BindView(R.id.edt_signup_surname)
    TextInputLayout textInputLayoutSurname;
    @BindView(R.id.edt_signup_email)
    TextInputLayout textInputLayoutEmail;


    private int revealX;
    private int revealY;
    View rootLayout;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        final Intent intent = getIntent();

        rootLayout = findViewById(R.id.root_layout);


        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
            rootLayout.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);


            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }

        } else {
            rootLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRevealActivity();
    }

    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);

            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(1000);
            circularReveal.setInterpolator(new AccelerateInterpolator());

            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            finish();
        }
    }


    protected void unRevealActivity() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            finish();
        } else {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    rootLayout, revealX, revealY, finalRadius, 0);

            circularReveal.setDuration(1000);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rootLayout.setVisibility(View.INVISIBLE);
                    finish();
                }
            });


            circularReveal.start();
        }
    }


    private boolean checkEmailAddress() {
        String EmailInput = textInputLayoutEmail.getEditText().getText().toString().trim();
        if (EmailInput.isEmpty()) {
            textInputLayoutEmail.setError("این فیلد نمیتواند خالی باشد.");
            return false;
        } else if (!(EmailInput.contains("@"))) {
            textInputLayoutEmail.setError("لطفاآدرس ایمیل رامجددا وارد کنید.");
            return false;
        } else {
            textInputLayoutEmail.setError(null);
            return true;
        }
    }

    private boolean checkName(){
        String NameInput = textInputLayoutName.getEditText().getText().toString().trim();
        if (NameInput.isEmpty()) {
            textInputLayoutEmail.setError("این فیلد نمیتواند خالی باشد.");
            return false;
        }else if(NameInput.length()>20){
            textInputLayoutName.setError("لطفا نام معتبر را وارد کنید.");
            return false;
        }else if(NameInput.length()<3){
            textInputLayoutName.setError("لطفا نام معتبر را وارد کنید.");
            return false;
        }else {
            textInputLayoutName.setError(null);
            return true;
        }
    }

    private boolean checkSurname(){
        String SurnameInput = textInputLayoutSurname.getEditText().getText().toString().trim();
        if (SurnameInput.isEmpty()) {
            textInputLayoutEmail.setError("این فیلد نمیتواند خالی باشد.");
            return false;
        }else if(SurnameInput.length()>45){
            textInputLayoutName.setError("لطفا نام خانوادگی معتبر را وارد کنید.");
            return false;
        }else if(SurnameInput.length()<3){
            textInputLayoutName.setError("لطفا نام خانوادگی معتبر را وارد کنید.");
            return false;
        }else {
            textInputLayoutName.setError(null);
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void clickHandler(View view) {
        if (view.getId() == R.id.btn_signup_register) {
            if (checkSurname() && checkName() && checkEmailAddress()) {
                Intent intent = new Intent(SignUpActivity.this, HomePageActivity.class);
                startActivity(intent);
            }
        }
    }
}
