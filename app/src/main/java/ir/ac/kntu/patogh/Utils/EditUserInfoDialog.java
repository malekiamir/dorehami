package ir.ac.kntu.patogh.Utils;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.Button;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.muddzdev.styleabletoast.StyleableToast;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.Activities.MainActivity;
import ir.ac.kntu.patogh.Activities.PhoneVerificationActivity;
import ir.ac.kntu.patogh.ApiDataTypes.TypeEditUserDetails;
import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.R;

import butterknife.BindView;
import ir.ac.kntu.patogh.R;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static ir.ac.kntu.patogh.Activities.SignUpActivity.isEmailValid;

public class EditUserInfoDialog extends Dialog {


    @BindView(R.id.btn_cancel)
    Button cancelEdit;
    @BindView(R.id.edt_user_edit_firstname)
    EditText editFirstName;
    @BindView(R.id.edt_user_edit_lastname)
    EditText editLastName;
    @BindView(R.id.edt_user_edit_email)
    EditText editEmail;
    @BindView(R.id.edt_user_edit_phone)
    EditText editPhone;
    @BindView(R.id.btn_ok)
    CircularProgressButton edit;
    private SharedPreferences sharedPreferences;
    private String baseURL = "http://patogh.potatogamers.ir:7701/api/";


    public EditUserInfoDialog(@NonNull Context context) {
        super(context);
    }

    public EditUserInfoDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected EditUserInfoDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_user_info);
        ButterKnife.bind(this);
        sharedPreferences = getContext()
                .getSharedPreferences("TokenPref", 0);
        getUserDetails();


        cancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((CircularProgressButton) view).startAnimation();
                checkFields();
            }
        });

    }

    private void getUserDetails() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .build();
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        String token = sharedPreferences.getString("Token", "none");

        patoghApi.getUserDetails("Bearer " + token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println(response.body());
                    String res = response.body().string();
                    System.out.println(res);
                    JsonObject jsonObject1 = new Gson().fromJson(res, JsonObject.class);
                    String returnValue = jsonObject1.get("returnValue").toString();
                    JsonObject jsonObject2 = new Gson().fromJson(returnValue, JsonObject.class);
                    String firstName = jsonObject2.get("firstName").getAsString();
                    String lastName = jsonObject2.get("lastName").getAsString();
                    String email = jsonObject2.get("email").getAsString();
                    String phoneNumber = jsonObject2.get("phoneNumber").getAsString();
                    editFirstName.setText(firstName);
                    editLastName.setText(lastName);
                    editEmail.setText(email);
                    editPhone.setText(phoneNumber);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void editUserDetails(String phoneNumber, String firstName, String lastName, String email) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .build();
        Gson gson = new Gson();
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        String token = sharedPreferences.getString("Token", "none");
        if (token.equals("none")) {
            return;
        }
        TypeEditUserDetails typeEditUserDetails;
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json")
                , gson.toJson(typeEditUserDetails = new TypeEditUserDetails(phoneNumber,
                        firstName, lastName, email)
                ));

        patoghApi.editUserDetails("Bearer " + token, requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.code() == 200) {
                        edit.revertAnimation();
                        dismiss();
                        new StyleableToast
                                .Builder(getContext())
                                .text("اطلاعات شما به روزرسانی شد.")
                                .textColor(Color.WHITE)
                                .backgroundColor(Color.argb(255, 124, 179, 66))
                                .show();
                    }else{
                        edit.revertAnimation();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                edit.revertAnimation();
                new StyleableToast
                        .Builder(getContext())
                        .text("لطفا اتصال اینترنت را بررسی نمایید و سپس مجددا تلاش نمایید.")
                        .textColor(Color.WHITE)
                        .backgroundColor(Color.argb(255, 255, 94, 100))
                        .show();
            }
        });
    }

    private void checkFields() {
        if (isSurnameValid() && isNameValid()
                && isEmailValid(editEmail.getText().toString().trim()) && isPhoneValid()
        ) {
            editUserDetails(editPhone.getText().toString(), editFirstName.getText().toString()
                    , editLastName.getText().toString(), editEmail.getText().toString());
        }
        if (!(isNameValid())) {
            editFirstName.setError("نام صحیح را وارد کنید.");

        } else if (isNameValid()) {
            editFirstName.setError(null);
        }
        if (!(isEmailValid(editEmail.getText().toString().trim()))) {
            editEmail.setError("ایمیل صحیح را وارد کنید.");
        } else if (isEmailValid(editEmail.getText().toString().trim())) {
            editEmail.setError(null);
        }
        if (!(isSurnameValid())) {
            editLastName.setError("نام خانوادگی صحیح را وارد کنید.");
        } else if (isSurnameValid()) {
            editLastName.setError(null);
        }
        if (!(isPhoneValid())) {
            editPhone.setError("شماره همراه صحیح را وارد کنید.");
        } else if (isPhoneValid()) {
            editPhone.setError(null);
        }

    }

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
        if (editFirstName.getText().toString().trim().length() > 20 ||
                editFirstName.getText().toString().trim().length() < 3) {
            isValid = false;
        }
        return isValid;
    }

    public boolean isSurnameValid() {
        boolean isValid = true;
        if (editLastName.getText().toString().trim().length() > 40 ||
                editLastName.getText().toString().trim().length() < 3) {
            isValid = false;
        }
        return isValid;
    }

    public boolean isPhoneValid() {
        boolean isValid = true;
        if (editPhone.getText().toString().trim().length() != 12) {
            isValid = false;
        }
        return isValid;
    }
}
