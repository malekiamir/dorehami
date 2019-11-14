package ir.ac.kntu.patogh.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import ir.ac.kntu.patogh.R;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class SignUpActivity extends AppCompatActivity {

    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";

//    @BindView(R.id.edt_signup_name)
//    EditText editTextName;
//    @BindView(R.id.edt_signup_surname)
//    EditText editTextSurname;
//    @BindView(R.id.edt_signup_email)
//    EditText editTextEmail;


    private int revealX;
    private int revealY;
    View rootLayout;
    private View view;

    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextEmail;
    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        final Intent intent = getIntent();

        rootLayout = findViewById(R.id.root_layout);
        editTextName = findViewById(R.id.edt_signup_name);
        editTextSurname = findViewById(R.id.edt_signup_surname);
        editTextEmail = findViewById(R.id.edt_signup_email);
        buttonSignUp = findViewById(R.id.btn_signup_register);

        editTextName.addTextChangedListener(signInTextWatcher);
        editTextSurname.addTextChangedListener(signInTextWatcher);
        editTextEmail.addTextChangedListener(signInTextWatcher);

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

    private TextWatcher signInTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String signUpName = editTextName.getText().toString().trim();
            String signUpSurname = editTextSurname.getText().toString().trim();
            String signUpEmail = editTextEmail.getText().toString().trim();


            buttonSignUp.setEnabled(!signUpName.isEmpty() && !signUpSurname.isEmpty() && !signUpEmail.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

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


    public static boolean isEmailValid(String editTextEmail) {
        boolean isValid = true;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = editTextEmail;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (!matcher.matches()) {
            isValid = false;
        }
        return isValid;
    }

    public boolean isNameValid() {
        boolean isValid = true;
        // String NameInput = editTextName.getText().toString().trim();
        if (editTextName.length() > 20 || editTextName.length() < 3) {
            isValid = false;
        }
        return isValid;
    }

    public boolean isSurnameValid() {
        boolean isValid = true;
        // String NameInput = editTextName.getText().toString().trim();
        if (editTextSurname.length() > 40 || editTextSurname.length() < 3) {
            isValid = false;
        }
        return isValid;
    }

    @TargetApi(Build.VERSION_CODES.P)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void clickHandler(View view) {
        if (view.getId() == R.id.btn_signup_register) {
            String signUpEmail = editTextEmail.getText().toString().trim();
            if (isEmailValid(signUpEmail) && isNameValid() && isSurnameValid()) {
                Intent intent = new Intent(SignUpActivity.this, HomePageActivity.class);
                startActivity(intent);
            } else if (!(isNameValid())) {
                Toast.makeText(this, "لطفا نام را درست واردکنید.", Toast.LENGTH_LONG).show();
                editTextName.setTextColor(Color.rgb(247, 17, 5));
            } else if (!(isSurnameValid())) {
                Toast.makeText(this, "لطفا نام خانوادگی را درست واردکنید.", Toast.LENGTH_LONG).show();
                editTextName.setTextColor(Color.rgb(247, 17, 5));
            } else if (!(isEmailValid(signUpEmail))) {
                Toast.makeText(this, "لطفاآدرس ایمیل را درست واردکنید.", Toast.LENGTH_LONG).show();
                editTextEmail.setTextColor(Color.rgb(247, 17, 5));
            }
        }
    }

}