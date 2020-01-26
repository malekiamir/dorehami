package ir.ac.kntu.patogh.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SecondStepFragment extends Fragment implements Step {

    @BindView(R.id.spinner_add_event_subject)
    MaterialSpinner spinnerSubject;
    @BindView(R.id.chips_input)
    NachoTextView nachoTextView;
    @BindView(R.id.edt_add_event_summary)
    EditText edtSummary;
    @BindView(R.id.edt_add_event_description)
    EditText edtDescription;
    @BindView(R.id.textInputLayout_add_event_summary)
    TextInputLayout ledtSummary;
    @BindView(R.id.textInputLayout_add_event_description)
    TextInputLayout ledtDescription;
    @BindView(R.id.frame_layout_second_step)
    ScrollView layout;

    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_second_step, container, false);
        ButterKnife.bind(this, root);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                if (spinnerSubject.hasFocus()) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    spinnerSubject.clearFocus();
                } else if (edtDescription.hasFocus()) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    edtDescription.clearFocus();
                } else if (edtSummary.hasFocus()) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    edtSummary.clearFocus();
                } else if (nachoTextView.hasFocus()) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    nachoTextView.clearFocus();
                }
                return true;
            }
        });

        edtSummary.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                edtSummary.setError(null);
                ledtSummary.setError(null);
            }
        });
        edtDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                edtDescription.setError(null);
                ledtDescription.setError(null);
            }
        });

        sharedPreferences = getActivity()
                .getSharedPreferences("TokenPref", 0);
        spinnerSubject.setItems("موضوع رویداد را انتخاب کنید", "ورزشی", "تکنولوژی", "سرگرمی", "تاریخی", "علمی", "گردشگری", "مسابقه");
        spinnerSubject.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
            }
        });

        String[] suggestions = new String[]{"قایقرانی", "کوهنوردی", "مسابقه", "شاد", "هیجانی"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, suggestions);
        nachoTextView.setAdapter(adapter);
        nachoTextView.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        nachoTextView.addChipTerminator('\n', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        return root;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        String summary = edtSummary.getText().toString();
        String description = edtDescription.getText().toString();
        String subject = spinnerSubject.getText().toString();
        String tags = nachoTextView.getText().toString();
        boolean error = false;

        if (!summary.equals("") && summary.length() >= 5) {
            editor.putString("PATOGH_EVENT_SUMMARY", summary);
            editor.apply();
        } else {
            ledtSummary.setError("خلاصه رویداد باید حداقل 5 حرف باشه!");
            error = true;
        }
        if (!description.equals("") && description.length() >= 5) {
            editor.putString("PATOGH_EVENT_DESCRIPTION", description);
            editor.apply();
        } else {
            ledtDescription.setError("توضیحات باید حداقل 5 حرف باشه!");
            error = true;
        }
        if (!subject.equals("") && !subject.equals("موضوع رویداد را انتخاب کنید")) {
            editor.putString("PATOGH_EVENT_SUBJECT", subject);
            editor.apply();
        } else {
            spinnerSubject.setError("موضوع رویداد انتخاب نشده!");
            error = true;
        }
        if (!tags.equals("")) {
            editor.putString("PATOGH_EVENT_TAGS", tags);
            editor.apply();
        } else {
            nachoTextView.setError("برچسبی نداریم!");
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
