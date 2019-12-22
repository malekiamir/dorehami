package ir.ac.kntu.patogh.Fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.neshan.core.Bounds;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.R;

public class ThirdStepFragment extends Fragment implements Step {

    @BindView(R.id.map_add_event)
    MapView map;
    VectorElementLayer markerLayer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_third_step, container, false);
        ButterKnife.bind(this, root);
        mapConfiguration();
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
        map.setMapEventListener(new MapEventListener(){

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
            public void onMapClicked(ClickData mapClickInfo){
                if(mapClickInfo.getClickType() == ClickType.CLICK_TYPE_LONG) {
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
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
