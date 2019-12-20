package ir.ac.kntu.patogh.Adapters;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import ir.ac.kntu.patogh.Fragments.FirstStepFragment;
import ir.ac.kntu.patogh.R;

public class AddEventStepperAdapter extends AbstractFragmentStepAdapter {

    private String[] title = new String[]{"اطلاعات", "پیوست ها", "محل برگزاری"};

    public AddEventStepperAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        final FirstStepFragment step = new FirstStepFragment();
        Bundle b = new Bundle();
        b.putInt("POS", position);
        step.setArguments(b);
        return step;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        return new StepViewModel.Builder(context)
                .setTitle(title[position]) //can be a CharSequence instead
                .create();
    }
}