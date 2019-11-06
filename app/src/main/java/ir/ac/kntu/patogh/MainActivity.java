package ir.ac.kntu.patogh;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static android.app.ActivityOptions.makeSceneTransitionAnimation;

public class MainActivity extends AppCompatActivity {

    final int currentApiVersion = android.os.Build.VERSION.SDK_INT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Glide.with(this.getApplicationContext())
                .load(R.drawable.back)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(7, 3)))
                .into((ImageView) findViewById(R.id.bg));

    }

    public void clickHandler(View view) {
        if(view.getId() == R.id.btn_signup) {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this, view, "transition");

            int revealX = (int) (view.getX() + view.getWidth() / 2);
            int revealY = (int) (view.getY() + view.getHeight() / 2);
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            intent.putExtra(SignUpActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
            intent.putExtra(SignUpActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);

            ActivityCompat.startActivity(this, intent, options.toBundle());
        }
        if (view.getId() == R.id.button3){
            //ActivityOptionsCompat options = ActivityOptionsCompat.
           // makeSceneTransitionAnimation(this, view, "transition");

            //int revealX = (int) (view.getX() + view.getWidth() / 2);
           // int revealY = (int) (view.getY() + view.getHeight() / 2);
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
//            intent.putExtra(SignUpActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
//            intent.putExtra(SignUpActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);

            startActivity(intent);
        }
    }
}
