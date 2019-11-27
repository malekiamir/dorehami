package ir.ac.kntu.patogh.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import butterknife.BindView;
import ir.ac.kntu.patogh.R;

import static ir.ac.kntu.patogh.R.drawable.avd_anim;

public class FirstPageActivity extends AppCompatActivity {

//    @BindView(R.id.img_first_page_animation)
//    ImageView imageView;
    private AnimatedVectorDrawable animatedVectorDrawable;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        ImageView imageView = findViewById(R.id.img_first_page_animation);
        Animatable animatable = (Animatable) imageView.getDrawable();
        animatable.start();
//        AnimatedVectorDrawable animatedLoaderDrawable = (AnimatedVectorDrawable) getDrawable(avd_anim);
//        imageView.setImageDrawable(animatedLoaderDrawable);
//        animatedLoaderDrawable.start();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            animatedVectorDrawable = (AnimatedVectorDrawable) getDrawable(avd_anim);
//        }
//        AnimatedVectorDrawableCompat animatedVectorDrawableCompat = AnimatedVectorDrawableCompat.create(this, avd_anim);
////        imageView.setImageDrawable(R.drawable.avd_anim);
//        Animatable animatable = (Animatable) imageView.getDrawable();
//        animatable.start();
////        Animatable animatable = (Animatable) imageView.getDrawable(R.drawable.avd_anim);
////        animatable.start();
    }
}
