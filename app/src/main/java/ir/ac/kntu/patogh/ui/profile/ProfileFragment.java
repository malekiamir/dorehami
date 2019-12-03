package ir.ac.kntu.patogh.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.kntu.patogh.R;
import jp.wasabeef.glide.transformations.BlurTransformation;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

public class ProfileFragment extends Fragment {

//    @BindView(R.id.img_profile_back_pic)
//    ImageView ivProfile;
    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
//
//        Glide.with(this.getContext())
//                .load(R.drawable.back)
//                .apply(RequestOptions.bitmapTransform(new BlurTransformation(7, 3)))
//                .into((ImageView)root.findViewById(R.id.img_profile_back_pic));
//
//        Glide.with(this) //1
//                .load(R.drawable.back)
//                .placeholder(R.drawable.back)
//                .error(R.drawable.back)
//                .skipMemoryCache(true) //2
//                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
//                .into((ImageView)root.findViewById(R.id.img_profile_back_pic));
//        Glide.with(this) //1
//                .load(R.drawable.back)
//                .placeholder(R.drawable.back)
//                .error(R.drawable.back)
//                .circleCrop()
//                .skipMemoryCache(true) //2
//                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
//                .into((ImageView)root.findViewById(R.id.img_profile_circlar_pic));
        return root;
    }

}