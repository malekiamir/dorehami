package ir.ac.kntu.patogh.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.ApiDataTypes.TypeEditUserDetails;
import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.R;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.edt_signup_name)
    EditText editTextName;
    @BindView(R.id.edt_signup_surname)
    EditText editTextSurname;
    @BindView(R.id.edt_signup_email)
    EditText editTextEmail;

    @BindView(R.id.textInputLayout_signup_namefield)
    TextInputLayout textInputLayoutName;
    @BindView(R.id.textInputLayout_signup_email)
    TextInputLayout textInputLayoutEmail;
    @BindView(R.id.textInputLayout_signup_surnamefield)
    TextInputLayout textInputLayoutSurname;

    @BindView(R.id.btn_signup_register)
    CircularProgressButton buttonSignUp;

    @BindView(R.id.sign_up_constraint_layout)
    FrameLayout layout;

    private SharedPreferences sharedPreferences;

    boolean success = false;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        buttonSignUp.setOnClickListener(view -> checkFields());

         sharedPreferences = getApplicationContext()
                .getSharedPreferences("TokenPref",0);
        Glide.with(this.getApplicationContext())
                .load(R.drawable.back)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(7, 3)))
                .into((ImageView) findViewById(R.id.img_signuppage_background));
        editTextName.addTextChangedListener(signInTextWatcher);
        editTextSurname.addTextChangedListener(signInTextWatcher);
        editTextEmail.addTextChangedListener(signInTextWatcher);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (editTextName.hasFocus()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    editTextName.clearFocus();
                } else if (editTextSurname.hasFocus()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    editTextSurname.clearFocus();
                } else if (editTextEmail.hasFocus()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    editTextEmail.clearFocus();
                }
                return true;
            }
        });
    }

    private void checkFields() {
        if (isSurnameValid() && isNameValid()
                && isEmailValid(textInputLayoutEmail.getEditText().getText().toString().trim())
        ) {
            editUserDetails();
        }
        if (!(isNameValid())) {
            textInputLayoutName.setError("نام صحیح را وارد کنید.");

        } else if (isNameValid()) {
            textInputLayoutName.setError(null);
        }
        if (!(isEmailValid(textInputLayoutEmail.getEditText().getText().toString().trim()))) {
            textInputLayoutEmail.setError("ایمیل صحیح را وارد کنید.");
        } else if (isEmailValid(textInputLayoutEmail.getEditText().getText().toString().trim())) {
            textInputLayoutEmail.setError(null);
        }
        if (!(isSurnameValid())) {
            textInputLayoutSurname.setError("نام خانوادگی صحیح را وارد کنید.");
        } else if (isSurnameValid()) {
            textInputLayoutSurname.setError(null);
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

    public static boolean isEmailValid(String Email) {
        boolean isValid = true;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = Email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (!matcher.matches()) {
            isValid = false;
        }
        return isValid;
    }

    public boolean isNameValid() {
        boolean isValid = true;
        if (textInputLayoutName.getEditText().getText().toString().trim().length() > 20 ||
                textInputLayoutName.getEditText().getText().toString().trim().length() < 3) {
            isValid = false;
        }
        return isValid;
    }

    public boolean isSurnameValid() {
        boolean isValid = true;
        if (textInputLayoutSurname.getEditText().getText().toString().trim().length() > 40 ||
                textInputLayoutSurname.getEditText().getText().toString().trim().length() < 3) {
            isValid = false;
        }
        return isValid;
    }


    private boolean editUserDetails() {
        buttonSignUp.startAnimation();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://eg.potatogamers.ir:7701/api/")
                .build();
        Gson gson = new Gson();
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
//        String token = getIntent().getStringExtra("token");
        String token = sharedPreferences.getString("Token", "none");
        if(token.equals("none")) {
            Intent intent = new Intent(SignUpActivity.this, PhoneVerificationActivity.class);
            startActivity(intent);
            finish();
        }
        TypeEditUserDetails te;
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json")
                , gson.toJson(te = new TypeEditUserDetails(phoneNumber
                        , editTextName.getText().toString()
                        , editTextSurname.getText().toString()
                        , editTextEmail.getText().toString())
                ));

        Log.d("@@@@@@@@@", te.toString());


        patoghApi.editUserDetails("Bearer " + token, requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Intent intent = new Intent(SignUpActivity.this, HomePageActivity.class);
                    startActivity(intent);
                    finish();
                }
                success = true;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                new StyleableToast
                        .Builder(SignUpActivity.this)
                        .text("لطفا اتصال اینترنت را بررسی نمایید و سپس مجددا تلاش نمایید.")
                        .textColor(Color.WHITE)
                        .backgroundColor(Color.argb(255, 255, 94, 100))
                        .show();
                success = false;
            }
        });
        buttonSignUp.revertAnimation();
        return success;
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
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

}