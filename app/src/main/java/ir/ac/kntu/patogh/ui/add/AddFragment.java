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
    private TypeCreateEvent event;
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
        String name = sharedPreferences.getString("PATOGH_EVENT_NAME", "");
        String startDate = sharedPreferences.getString("PATOGH_EVENT_START_DATE", "");
        String endDate = sharedPreferences.getString("PATOGH_EVENT_END_DATE", "");
        String startTime = sharedPreferences.getString("PATOGH_EVENT_START_TIME", "");
        String endTime = sharedPreferences.getString("PATOGH_EVENT_END_TIME", "");
        String imageId = sharedPreferences.getString("PATOGH_EVENT_IMAGE_ID", "");
        String summary = sharedPreferences.getString("PATOGH_EVENT_SUMMARY", "");
        String description = sharedPreferences.getString("PATOGH_EVENT_DESCRIPTION", "");
        String subject = sharedPreferences.getString("PATOGH_EVENT_SUBJECT", "");
        String tags = sharedPreferences.getString("PATOGH_EVENT_TAGS", "");
        String latitude = sharedPreferences.getString("PATOGH_EVENT_LATITUDE", "");
        String longitude = sharedPreferences.getString("PATOGH_EVENT_LONGITUDE", "");
        String address = sharedPreferences.getString("PATOGH_EVENT_ADDRESS", "");
        boolean isPhysical = sharedPreferences.getBoolean("PATOGH_EVENT_IS_PHYSICAL", false);

        startTime = startTime.split(" : ")[0] + ":" + startTime.split(" : ")[1];
        endTime = endTime.split(" : ")[0] + ":" + endTime.split(" : ")[1];

        if(isPhysical) {
            event = new TypeCreateEvent(name, farsiToDecimal(startDate.substring(2) + " " + startTime)
                    , farsiToDecimal(endDate.substring(2) + " " + endTime), summary, subject
                    , 10, isPhysical, farsiToDecimal(latitude), farsiToDecimal(longitude), "تهران"
                    , imageId, address, description, new String[]{imageId}, tags.split(" {2}"));
        } else {
            event = new TypeCreateEvent(name, farsiToDecimal(startDate.substring(2) + " " + startTime)
                    , farsiToDecimal(endDate.substring(2) + " " + endTime), summary, subject
                    , 10, isPhysical, farsiToDecimal(latitude), farsiToDecimal(longitude), ""
                    , imageId, null, description, new String[]{imageId}, tags.split(" {2}"));
        }
        System.out.println(event);
        createEvent();
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

    private static String farsiToDecimal(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            else if (ch=='٫')
                ch = '.';
            chars[i] = ch;
        }
        return new String(chars);
    }

    private void createEvent() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .build();
        Gson gson = new Gson();
        PatoghApi patoghApi = retrofit.create(PatoghApi.class);
        String token = sharedPreferences.getString("Token", "none");
        if (token.equals("none")) {
            return;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json")
                , gson.toJson(event));

        patoghApi.createDorehami("Bearer " + token, requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.code() == 200) {
                        new StyleableToast
                                .Builder(getContext())
                                .text("رویداد با موفقیت ثبت شد")
                                .textColor(Color.WHITE)
                                .backgroundColor(Color.argb(255, 124, 179, 66))
                                .show();
                        clearCachedData();
                        mStepperLayout.setCurrentStepPosition(0);
                    } else {
                        new StyleableToast
                                .Builder(getContext())
                                .text("مشکلی در ساخت رویداد رخ داده")
                                .textColor(Color.WHITE)
                                .backgroundColor(Color.argb(255, 255, 94, 100))
                                .show();
                    }
                    System.out.println(response.code());
                    System.out.println(response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                new StyleableToast
                        .Builder(getContext())
                        .text("لطفا اتصال اینترنت را بررسی نمایید و سپس مجددا تلاش نمایید.")
                        .textColor(Color.WHITE)
                        .backgroundColor(Color.argb(255, 255, 94, 100))
                        .show();
            }
        });
    }

    private void clearCachedData() {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PATOGH_EVENT_NAME", "");
        editor.putString("PATOGH_EVENT_START_DATE", "");
        editor.putString("PATOGH_EVENT_END_DATE", "");
        editor.putString("PATOGH_EVENT_START_TIME", "");
        editor.putString("PATOGH_EVENT_END_TIME", "");
        editor.putString("PATOGH_EVENT_IMAGE_ID", "");
        editor.putString("PATOGH_EVENT_SUMMARY", "");
        editor.putString("PATOGH_EVENT_DESCRIPTION", "");
        editor.putString("PATOGH_EVENT_SUBJECT", "");
        editor.putString("PATOGH_EVENT_TAGS", "");
        editor.putString("PATOGH_EVENT_LATITUDE", "");
        editor.putString("PATOGH_EVENT_LONGITUDE", "");
        editor.putString("PATOGH_EVENT_ADDRESS", "");
        editor.putBoolean("PATOGH_EVENT_IS_PHYSICAL", true);
        editor.apply();
    }

}