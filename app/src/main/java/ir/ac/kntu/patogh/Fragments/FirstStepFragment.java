package ir.ac.kntu.patogh.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.components.DateItem;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.io.File;
import java.sql.Time;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.TimeDialog;
import jp.wasabeef.glide.transformations.BlurTransformation;


public class FirstStepFragment extends Fragment implements Step, DateSetListener {

    @BindView(R.id.edt_add_event_start_date)
    EditText edtStartDate;
    @BindView(R.id.edt_add_event_start_time)
    EditText edtStartTime;
    @BindView(R.id.image_upload)
    Button upload;
    @BindView(R.id.uploaded_image)
    ImageView image;
    private Date mStartDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_first_step, container, false);
        ButterKnife.bind(this, root);
        setLocale("fa");

        mStartDate = new Date();
        edtStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
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

                    builder.build(FirstStepFragment.this::onDateSet)
                            .show(getFragmentManager(), "");
                    view.clearFocus();
                }
            }
        });

        edtStartTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    //                new TimePickerDialog(getContext(), R.style.myTimePicker,new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
//                        Toast.makeText(getContext(), i + " : " + i1,Toast.LENGTH_SHORT).show();
//                    }
//                }, 7, 17, true)
//                        .show();
                    TimeDialog dialog = new TimeDialog(getContext());
                    dialog.setContentView(R.layout.dialog_time_picker);
                    dialog.show();
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
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();


            }
        });

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = data.getData();
            Glide.with(getContext())
                    .load(fileUri)
                    .into(image);
            //You can get File object from intent
            File file = ImagePicker.Companion.getFile(data);
            //You can also get File Path from intent
            String filepath = ImagePicker.Companion.getFilePath(data);
            Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getContext(), ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Canceled", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
        mStartDate.setDate(day, month, year);
        edtStartDate.setText(mStartDate.getDate());
        edtStartDate.clearFocus();
    }

    class Date extends DateItem {
        String getDate() {
            return String.format(Locale.US,
                    "%d/%d/%d",
                    getYear(), getMonth(), getDay());
        }
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}