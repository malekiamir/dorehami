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
import ir.ac.kntu.patogh.Fragments.SecondStepFragment;
import ir.ac.kntu.patogh.Fragments.ThirdStepFragment;
import ir.ac.kntu.patogh.R;

public class AddEventStepperAdapter extends AbstractFragmentStepAdapter {

    private String[] title = new String[]{"اطلاعات", "پیوست ها", "محل برگزاری"};

    public AddEventStepperAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        if (position == 0) {
            final FirstStepFragment step = new FirstStepFragment();
            Bundle b = new Bundle();
            b.putInt("POS", position);
            step.setArguments(b);
            return step;
        } else if (position == 1) {
            final SecondStepFragment step = new SecondStepFragment();
            Bundle b = new Bundle();
            b.putInt("POS", position);
            step.setArguments(b);
            return step;

        } else {
            final ThirdStepFragment step = new ThirdStepFragment();
            Bundle b = new Bundle();
            b.putInt("POS", position);
            step.setArguments(b);
            return step;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        StepViewModel.Builder builder = new StepViewModel.Builder(context)
                .setTitle(title[position]);
        switch (position) {
            case 0:
                builder
                        .setEndButtonLabel("ادامه")
                        .setBackButtonLabel("")
                        .setNextButtonEndDrawableResId(R.drawable.ms_ic_chevron_right)
                        .setBackButtonStartDrawableResId(StepViewModel.NULL_DRAWABLE);

                break;
            case 1:
                builder
                        .setEndButtonLabel("ادامه")
                        .setBackButtonLabel("مرحله قبل")
                        .setNextButtonEndDrawableResId(R.drawable.ms_ic_chevron_right)
                        .setBackButtonStartDrawableResId(R.drawable.ms_ic_chevron_left);
                break;
            case 2:
                builder
                        .setBackButtonLabel("مرحله قبل")
                        .setEndButtonLabel("ثبت رویداد")
                        .setBackButtonStartDrawableResId(R.drawable.ms_ic_chevron_left);
                break;
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }
        return builder.create();
    }
}