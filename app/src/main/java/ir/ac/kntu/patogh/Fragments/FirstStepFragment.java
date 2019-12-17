package ir.ac.kntu.patogh.Fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alirezaafkar.sundatepicker.DatePicker;
import com.alirezaafkar.sundatepicker.components.DateItem;
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.R;



public class FirstStepFragment extends Fragment implements Step, View.OnClickListener, DateSetListener {

    @BindView(R.id.start_date)
    Button start;
    private Date mStartDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_first_step, container, false);
        ButterKnife.bind(this, root);
        setLocale("fa");
        //initialize your UI
        mStartDate = new Date();
        start.setOnClickListener(this::onClick);

        return root;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId() == R.id.start_date ? 1 : 2;
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

//        if (mFuture.isChecked()) {
            maxDate.set(Calendar.YEAR, maxDate.get(Calendar.YEAR) + 10);
//        }
//        if (mPast.isChecked()) {
//            minDate.set(Calendar.YEAR, minDate.get(Calendar.YEAR) - 10);
//        }

        DatePicker.Builder builder = new DatePicker.Builder()
                .id(id)
                .minDate(minDate)
                .maxDate(maxDate)
                .setRetainInstance(true);

        if (v.getId() == R.id.start_date)
            builder.date(mStartDate.getDay(), mStartDate.getMonth(), mStartDate.getYear());

        builder.build(this)
                .show(getFragmentManager(), "");
    }

    public void setLocale(String language) {
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
        if (id == 1) {
            mStartDate.setDate(day, month, year);
            start.setText(mStartDate.getDate());
        }
    }


    class Date extends DateItem {
        String getDate() {
            Calendar calendar = getCalendar();
            return String.format(Locale.US,
                    "%d/%d/%d (%d/%d/%d)",
                    getYear(), getMonth(), getDay(),
                    calendar.get(Calendar.YEAR),
                    +calendar.get(Calendar.MONTH) + 1,
                    +calendar.get(Calendar.DAY_OF_MONTH));
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
