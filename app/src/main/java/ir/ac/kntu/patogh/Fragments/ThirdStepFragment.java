package ir.ac.kntu.patogh.Fragments;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.neshan.core.LngLat;
import org.neshan.layers.VectorElementLayer;
import org.neshan.services.NeshanMapStyle;
import org.neshan.services.NeshanServices;
import org.neshan.styles.MarkerStyle;
import org.neshan.styles.MarkerStyleCreator;
import org.neshan.ui.ClickData;
import org.neshan.ui.ClickType;
import org.neshan.ui.MapEventListener;
import org.neshan.ui.MapView;
import org.neshan.utils.BitmapUtils;
import org.neshan.vectorelements.Marker;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.R;
import vn.luongvo.widget.iosswitchview.SwitchView;

public class ThirdStepFragment extends Fragment implements Step {

    @BindView(R.id.map_add_event)
    MapView map;
    @BindView(R.id.switch_add_event_physical)
    SwitchView switchMaterial;
    @BindView(R.id.img_add_event_location_icon)
    ImageView imgLocIcon;
    @BindView(R.id.tv_add_event_location_guide)
    TextView tvLocGuide;
    @BindView(R.id.tv_event_add_address_title)
    TextView tvAddressTitle;
    @BindView(R.id.edt_add_event_address)
    EditText edtAddress;
    @BindView(R.id.textInputLayout_add_event_address)
    TextInputLayout ledtAddress;
    VectorElementLayer markerLayer;

    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_third_step, container, false);
        ButterKnife.bind(this, root);
        sharedPreferences = getActivity()
                .getSharedPreferences("TokenPref", 0);
        mapConfiguration();
        switchMaterial.setOnCheckedChangeListener(new SwitchView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchView switchView, boolean b) {
                if (!b) {
                    map.animate().alpha(0.0f).setDuration(300).start();
                    tvLocGuide.animate().alpha(0.0f).setDuration(300).start();
                    imgLocIcon.animate().alpha(0.0f).setDuration(300).start();
                    tvAddressTitle.animate().alpha(0.0f).setDuration(300).start();
                    ledtAddress.animate().alpha(0.0f).setDuration(300).start();
                } else {
                    imgLocIcon.setVisibility(View.VISIBLE);
                    tvLocGuide.setVisibility(View.VISIBLE);
                    tvAddressTitle.setVisibility(View.VISIBLE);
                    ledtAddress.setVisibility(View.VISIBLE);
                    map.setVisibility(View.VISIBLE);

                    imgLocIcon.animate().alpha(1.0f).setDuration(300).start();
                    tvLocGuide.animate().alpha(1.0f).setDuration(300).start();
                    tvAddressTitle.animate().alpha(1.0f).setDuration(300).start();
                    ledtAddress.animate().alpha(1.0f).setDuration(300).start();
                    map.animate().alpha(1.0f).setDuration(300).start();
                }
            }
        });
        return root;
    }

    private void mapConfiguration() {
        LngLat focalPoint = new LngLat(51.3890, 35.6892);
        map.setFocalPointPosition(focalPoint, 1);
        map.setZoom(15, 1);
        markerLayer = NeshanServices.createVectorElementLayer();
        map.getLayers().add(NeshanServices.createBaseMap(NeshanMapStyle.STANDARD_DAY));
        map.getLayers().add(markerLayer);
        MarkerStyleCreator markStCr = new MarkerStyleCreator();
        markStCr.setSize(20f);
        markStCr.setBitmap(BitmapUtils.createBitmapFromAndroidBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_marker)));
        MarkerStyle markSt = markStCr.buildStyle();
        map.setMapEventListener(new MapEventListener() {

            @Override
            public void onMapMoved() {
                super.onMapMoved();
                // by calling getClickPos(), we can get position of clicking (or tapping)
                LngLat clickedLocation = map.getFocalPointPosition();
                // addMarker adds a marker (pretty self explanatory :D) to the clicked location
                Marker marker = new Marker(clickedLocation, markSt);
                markerLayer.clear();
                markerLayer.add(marker);
            }

            @Override
            public void onMapClicked(ClickData mapClickInfo) {
                if (mapClickInfo.getClickType() == ClickType.CLICK_TYPE_LONG) {
                    // by calling getClickPos(), we can get position of clicking (or tapping)
                    LngLat clickedLocation = mapClickInfo.getClickPos();
                    // addMarker adds a marker (pretty self explanatory :D) to the clicked location
                    Marker marker = new Marker(clickedLocation, markSt);
                    markerLayer.clear();
                    markerLayer.add(marker);
                }
            }
        });
//        Marker marker = new Marker(focalPoint, markSt);
//        markerLayer.add(marker);
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        String address = edtAddress.getText().toString();
        boolean error = false;

        if(switchMaterial.isChecked()) {

        }
        if (!address.equals("")) {
            editor.putString("PATOGH_EVENT_ADDRESS", address);
            editor.apply();
        } else {
            ledtAddress.setError("آدرس رویداد خالیه!");
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
