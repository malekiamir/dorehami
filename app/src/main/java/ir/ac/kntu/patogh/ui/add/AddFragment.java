package ir.ac.kntu.patogh.ui.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ir.ac.kntu.patogh.Adapters.AddEventStepperAdapter;
import ir.ac.kntu.patogh.R;

public class AddFragment extends Fragment implements StepperLayout.StepperListener{

    private AddViewModel addViewModel;
    private Unbinder unbinder;
    @BindView(R.id.stepperLayout)
    StepperLayout mStepperLayout;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add, container, false);
        unbinder = ButterKnife.bind(this, root);
        mStepperLayout.setAdapter(new AddEventStepperAdapter(getFragmentManager(), getContext()));
        return root;
    }

    @Override
    public void onCompleted(View completeButton) {
        Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT);
    }

    @Override
    public void onError(VerificationError verificationError) {

    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

    }
}