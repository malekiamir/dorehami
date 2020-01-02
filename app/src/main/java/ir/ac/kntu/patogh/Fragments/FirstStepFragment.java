package ir.ac.kntu.patogh.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.components.DateItem;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.muddzdev.styleabletoast.StyleableToast;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.Activities.MainActivity;
import ir.ac.kntu.patogh.Interfaces.PatoghApi;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.TimeDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class FirstStepFragment extends Fragment implements Step {

    @BindView(R.id.edt_add_event_start_date)
    EditText edtStartDate;
    @BindView(R.id.edt_add_event_start_time)
    EditText edtStartTime;
    @BindView(R.id.edt_add_event_end_date)
    EditText edtEndDate;
    @BindView(R.id.edt_add_event_end_time)
    EditText edtEndTime;
    @BindView(R.id.image_upload)
    Button upload;
    @BindView(R.id.edt_add_event_name)
    EditText edtName;
    @BindView(R.id.textInputLayout_add_event_name)
    TextInputLayout ledtName;
    @BindView(R.id.textInputLayout_add_event_start_date)
    TextInputLayout ledtStartDate;
    @BindView(R.id.textInputLayout_add_event_end_date)
    TextInputLayout ledtEndDate;
    @BindView(R.id.textInputLayout_add_event_start_time)
    TextInputLayout ledtStartTime;
    @BindView(R.id.textInputLayout_add_event_end_time)
    TextInputLayout ledtEndTime;
    @BindView(R.id.uploaded_image)
    ImageView image;
    @BindView(R.id.constraint_layout_first_step)
    ConstraintLayout layout;
    private Date mStartDate;
    private Date mEndDate;
    private SharedPreferences sharedPreferences;
    private String baseURL = "http://patogh.potatogamers.ir:7701/api/";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_first_step, container, false);
        ButterKnife.bind(this, root);
        sharedPreferences = getActivity()
                .getSharedPreferences("TokenPref", 0);
        setLocale("fa");
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                if (edtName.hasFocus()) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    edtName.clearFocus();
                }
                return true;
            }
        });
        mStartDate = new Date();
        mEndDate = new Date();

        edtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    ledtName.setError(null);
            }
        });
        edtStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                ledtStartDate.setError(null);
                if (b) {
                    Calendar minDate = Calendar.getInstance();
                    Calendar maxDate = Calendar.getInstance();

                    maxDate.set(Calendar.YEAR, maxDate.get(Calendar.YEAR) + 1);

                    DatePicker.Builder builder = new DatePicker.Builder()
                            .id(R.id.edt_add_event_start_date)
                            .minDate(minDate)
                            .maxDate(maxDate)
                            .setRetainInstance(true);

                    builder.date(mStartDate.getDay(), mStartDate.getMonth(), mStartDate.getYear());

                    builder.build(FirstStepFragment.this::onDateSetStart)
                            .show(getFragmentManager(), "");
                    view.clearFocus();
                }
            }
        });

        edtStartTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    ledtStartTime.setError(null);
                    TimeDialog dialog = new TimeDialog(getContext());
                    dialog.setContentView(R.layout.dialog_time_picker);
                    dialog.show();
                    dialog.getButtonSubmit().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                edtStartTime.setText(String.format("%02d : %02d", dialog.getTimePicker().getHour()
                                        , dialog.getTimePicker().getMinute()));
                            } else {
                                edtStartTime.setText(String.format("%02d : %02d", dialog.getTimePicker().getCurrentHour()
                                        , dialog.getTimePicker().getCurrentMinute()));
                            }
                            dialog.dismiss();
                        }
                    });
                    view.clearFocus();
                }
            }
        });

        edtEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    ledtEndDate.setError(null);
                    Calendar minDate = Calendar.getInstance();
                    Calendar maxDate = Calendar.getInstance();

                    maxDate.set(Calendar.YEAR, maxDate.get(Calendar.YEAR) + 1);

                    DatePicker.Builder builder = new DatePicker.Builder()
                            .id(R.id.edt_add_event_end_date)
                            .minDate(minDate)
                            .maxDate(maxDate)
                            .setRetainInstance(true);

                    builder.date(mEndDate.getDay(), mEndDate.getMonth(), mEndDate.getYear());

                    builder.build(FirstStepFragment.this::onDateSetEnd)
                            .show(getFragmentManager(), "1");
                    view.clearFocus();
                }
            }
        });

        edtEndTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    ledtEndTime.setError(null);
                    TimeDialog dialog = new TimeDialog(getContext());
                    dialog.setContentView(R.layout.dialog_time_picker);
                    dialog.show();
                    dialog.getButtonSubmit().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                edtEndTime.setText(String.format("%02d : %02d", dialog.getTimePicker().getHour()
                                        , dialog.getTimePicker().getMinute()));
                            } else {
                                edtEndTime.setText(String.format("%02d : %02d", dialog.getTimePicker().getCurrentHour()
                                        , dialog.getTimePicker().getCurrentMinute()));
                            }
                            dialog.dismiss();
                        }
                    });
                    view.clearFocus();
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(FirstStepFragment.this)
                        .crop(4, 3)                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .start();


            }
        });

        return root;
    }

    private void onDateSetStart(int id, Calendar calendar, int day, int month, int year) {
        mStartDate.setDate(day, month, year);
        edtStartDate.setText(mStartDate.getDate());
        edtStartDate.clearFocus();
    }

    private void onDateSetEnd(int id, Calendar calendar, int day, int month, int year) {
        mEndDate.setDate(day, month, year);
        edtEndDate.setText(mEndDate.getDate());
        edtEndDate.clearFocus();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = data.getData();
            Glide.with(getContext())
                    .load(fileUri)
                    .centerCrop()
                    .into(image);
            //You can get File object from intent
            File file = ImagePicker.Companion.getFile(data);
            uploadImage(file);
            //You can also get File Path from intent
            String filepath = ImagePicker.Companion.getFilePath(data);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getContext(), ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(getContext(), "Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage(File file) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .build();
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        String token = sharedPreferences.getString("Token", "none");
        if (token.equals("none")) {
            Toast.makeText(getContext(), "توکن شما پایان یافته.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }

        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("File", "File", fileReqBody);


        patoghApi.uploadImage("Bearer " + token, part).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    System.out.println("uploaded");
                    new StyleableToast
                            .Builder(getContext())
                            .text("تصویر آپلود شد")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.argb(255, 0, 200, 83))
                            .show();
                    try {
                        JsonObject jsonObject1 = new Gson().fromJson(response.body().string(), JsonObject.class);
                        String returnValue = jsonObject1.get("returnValue").toString();
                        JsonObject jsonObject2 = new Gson().fromJson(returnValue, JsonObject.class);
                        String imageId = jsonObject2.get("idString").getAsString();

                        final SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("PATOGH_EVENT_IMAGE_ID", imageId);
                        editor.apply();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("failed to upload " + " " + response.code() + " " + response.message());
                    new StyleableToast
                            .Builder(getContext())
                            .text("خطایی رخ داده")
                            .textColor(Color.WHITE)
                            .backgroundColor(Color.argb(255, 255, 94, 100))
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("no responseeeee");
                new StyleableToast
                        .Builder(getContext())
                        .text("لطفا اتصال اینترنت خود را بررسی نمایید")
                        .textColor(Color.WHITE)
                        .backgroundColor(Color.argb(255, 255, 94, 100))
                        .show();
            }
        });
    }


    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getContext().getResources().updateConfiguration(config,
                getContext().getResources().getDisplayMetrics());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLocale("fa");
    }

    class Date extends DateItem {
        String getDate() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return String.format(Locale.forLanguageTag("fa"),
                        "%02d/%02d/%02d",
                        getYear(), getMonth(), getDay());
            } else {
                return String.format("%02d/%02d/%02d",
                        getYear(), getMonth(), getDay());
            }
        }
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        String name = edtName.getText().toString();
        String startDate = edtStartDate.getText().toString();
        String endDate = edtEndDate.getText().toString();
        String startTime = edtStartTime.getText().toString();
        String endTime = edtEndTime.getText().toString();
        boolean error = false;

        // verify name
        if (!name.equals("")) {
            editor.putString("PATOGH_EVENT_NAME", name);
            editor.apply();
        } else {
            ledtName.setError("اسم رویداد خالیه!");
            error = true;
        }
        // verify date
        if (!startDate.equals("")) {
            editor.putString("PATOGH_EVENT_START_DATE", startDate);
            editor.apply();
        } else {
            ledtStartDate.setError("تاریخ شروع خالیه!");
            error = true;
        }
        if (!endDate.equals("")) {
            editor.putString("PATOGH_EVENT_END_DATE", endDate);
            editor.apply();
        } else {
            ledtEndDate.setError("تاریخ پایان خالیه!");
            error = true;
        }
        // verify time
        if (!startTime.equals("")) {
            editor.putString("PATOGH_EVENT_START_TIME", startTime);
            editor.apply();
        } else {
            ledtStartTime.setError("زمان شروع خالیه!");
            error = true;
        }
        if (!endTime.equals("")) {
            editor.putString("PATOGH_EVENT_END_TIME", endTime);
            editor.apply();
        } else {
            ledtEndTime.setError("زمان پایان خالیه!");
            error = true;
        }
        if (error)
            return new VerificationError("اطلاعات کامل نیست!");

        return null;
    }

    @Override
    public void onSelected() {
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
