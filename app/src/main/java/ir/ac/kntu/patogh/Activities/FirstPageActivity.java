package ir.ac.kntu.patogh.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.ImageView;

import ir.ac.kntu.patogh.R;

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
        Typewriter writer = findViewById(R.id.tv_firstPage_animated_text);
        Animatable animatable = (Animatable) imageView.getDrawable();
        // Animation animation = imageView.getDrawable();
        //animatable.start();
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1200);
//                    writer.setText("");
//                    writer.setCharacterDelay(120);
//                    writer.animateText("پاتوق");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//        Thread anim = new Thread((Runnable) animatable);
//        anim.start();
//        try {
//            anim.sleep(1200);
//            writer.setText("");
//            writer.setCharacterDelay(120);
//            writer.animateText("پاتوق");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1200);
                    writer.setText("");
                    writer.setCharacterDelay(120);
                    writer.animateText("پاتوق");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        animatable.start();
        thread.start();
    }
}