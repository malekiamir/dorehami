package ir.ac.kntu.patogh.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.muddzdev.styleabletoast.StyleableToast;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import ir.ac.kntu.patogh.ApiDataTypes.TypeEditUserDetails;
import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.R;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity {

    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";

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
    Button buttonSignUp;

    boolean success = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        buttonSignUp.setOnClickListener(view -> {
            checkFields();
        });

        editTextName.addTextChangedListener(signInTextWatcher);
        editTextSurname.addTextChangedListener(signInTextWatcher);
        editTextEmail.addTextChangedListener(signInTextWatcher);

    }

    private void checkFields() {
        if (isSurnameValid() && isSurnameValid()
                && isEmailValid(textInputLayoutEmail.getEditText().getText().toString().trim())
                && editUserDetails()) {

            Intent intent = new Intent(SignUpActivity.this, HomePageActivity.class);
            startActivity(intent);
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://185.252.30.32:7700/api/")
                .build();
        Gson gson = new Gson();
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        String token = getIntent().getStringExtra("token");

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json")
                , gson.toJson(new TypeEditUserDetails(phoneNumber
                        , textInputLayoutName.getEditText().toString()
                        , textInputLayoutSurname.getEditText().toString()
                        , textInputLayoutEmail.getEditText().toString())
                ));

        patoghApi.editUserDetails(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseBody = response.body().string();
                    Log.d("~~~~~~~~~~~~~~~~~", responseBody);
                } catch (IOException e) {
                    e.printStackTrace();
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

        return success;
    }


}