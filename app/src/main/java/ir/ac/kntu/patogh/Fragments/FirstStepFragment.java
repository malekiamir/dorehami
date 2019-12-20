package ir.ac.kntu.patogh.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.components.DateItem;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.R;
import ir.ac.kntu.patogh.Utils.TimeDialog;


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
    @BindView(R.id.uploaded_image)
    ImageView image;
    private Date mStartDate;
    private Date mEndDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_first_step, container, false);
        ButterKnife.bind(this, root);
        setLocale("fa");

        mStartDate = new Date();
        mEndDate = new Date();
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

                    builder.build(FirstStepFragment.this::onDateSetStart)
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
                    dialog.getButtonSubmit().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                edtStartTime.setText(String.format("%d : %d", dialog.getTimePicker().getHour()
                                        , dialog.getTimePicker().getMinute()));
                            } else {
                                edtStartTime.setText(String.format("%d : %d", dialog.getTimePicker().getCurrentHour()
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
                    dialog.getButtonSubmit().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                edtEndTime.setText(String.format("%d : %d", dialog.getTimePicker().getHour()
                                        , dialog.getTimePicker().getMinute()));
                            } else {
                                edtEndTime.setText(String.format("%d : %d", dialog.getTimePicker().getCurrentHour()
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
        Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();
        mStartDate.setDate(day, month, year);
        edtStartDate.setText(mStartDate.getDate());
        edtStartDate.clearFocus();
    }

    private void onDateSetEnd(int id, Calendar calendar, int day, int month, int year) {
        Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();
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

    class Date extends DateItem {
        String getDate() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return String.format(Locale.forLanguageTag("fa"),
                        "%d/%d/%d",
                        getYear(), getMonth(), getDay());
            } else {
                return String.format("%d/%d/%d",
                        getYear(), getMonth(), getDay());
            }
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
