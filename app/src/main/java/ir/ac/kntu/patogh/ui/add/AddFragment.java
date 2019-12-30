package ir.ac.kntu.patogh.ui.add;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.muddzdev.styleabletoast.StyleableToast;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ir.ac.kntu.patogh.Adapters.AddEventStepperAdapter;
import ir.ac.kntu.patogh.ApiDataTypes.TypeCreateEvent;
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

public class AddFragment extends Fragment implements StepperLayout.StepperListener {

    private AddViewModel addViewModel;
    private Unbinder unbinder;
    @BindView(R.id.stepperLayout)
    StepperLayout mStepperLayout;
    private String baseUrl = "http://patogh.potatogamers.ir:7701/api/";

    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add, container, false);
        unbinder = ButterKnife.bind(this, root);
        sharedPreferences = getActivity()
                .getSharedPreferences("TokenPref", 0);
        mStepperLayout.setAdapter(new AddEventStepperAdapter(getFragmentManager(), getContext()));
        mStepperLayout.setListener(this);
        return root;
    }

    @Override
    public void onCompleted(View completeButton) {
//        createEvent();
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

//    private void createEvent(String phoneNumber, String firstName, String lastName, String email) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .build();
//        Gson gson = new Gson();
//        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
//        String token = sharedPreferences.getString("Token", "none");
//        if (token.equals("none")) {
//            return;
//        }
//        TypeCreateEvent typeCreateEvent;
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json")
//                , gson.toJson(typeCreateEvent = new TypeCreateEvent(phoneNumber,
//                        firstName, lastName, email)
//                ));
//
//        patoghApi.editUserDetails("Bearer " + token, requestBody).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//                    if (response.code() == 200) {
//                        new StyleableToast
//                                .Builder(getContext())
//                                .text("رویداد با موفقیت ثبت شد.")
//                                .textColor(Color.WHITE)
//                                .backgroundColor(Color.argb(255, 94, 255, 100))
//                                .show();
//                    } else {
//                        new StyleableToast
//                                .Builder(getContext())
//                                .text("با این رویداد ساختنت.")
//                                .textColor(Color.WHITE)
//                                .backgroundColor(Color.argb(255, 255, 94, 100))
//                                .show();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                new StyleableToast
//                        .Builder(getContext())
//                        .text("لطفا اتصال اینترنت را بررسی نمایید و سپس مجددا تلاش نمایید.")
//                        .textColor(Color.WHITE)
//                        .backgroundColor(Color.argb(255, 255, 94, 100))
//                        .show();
//            }
//        });
//    }

}