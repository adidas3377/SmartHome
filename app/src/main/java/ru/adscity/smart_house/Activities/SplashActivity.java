package ru.adscity.smart_house.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import ru.adscity.smart_house.Manifest;
import ru.adscity.smart_house.R;
import ru.adscity.smart_house.User_Section.UserLocalStore;

public class SplashActivity extends AppCompatActivity {

    private TextView welcome;
    private TextView make;
    private TextView create;
    private static Typeface roboto;
    private final String TAG = "lifecycle";
    UserLocalStore checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 22) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
        double screenInches = Math.sqrt(x+y);
        if(screenInches > 9){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
        Log.d(TAG, "Activity создано"); // DEBUG


        welcome = (TextView) findViewById(R.id.welcomeTextView);
        make = (TextView) findViewById(R.id.makeTextView);
        create = (TextView) findViewById(R.id.createTextView);
        roboto = Typeface.createFromAsset(getAssets(), "fonts/FallingSky.otf");
        welcome.setTypeface(roboto);
        make.setTypeface(roboto);
        create.setTypeface(roboto);
        checked = new UserLocalStore(this);
        try {
            if (checked.getLanguage(SplashActivity.this).equals("ru")) {
                welcome.setText("Добро пожаловать!");
                welcome.setTextSize(30);
                make.setText("Сделаем вашу жизнь проще");
                create.setText("Разработан командой Takers");
            }
        }
        catch(Exception e) {
            Log.d("TAG",e.getLocalizedMessage());
        }
        Animation myAlpha = AnimationUtils.loadAnimation(this, R.anim.myalpha);
        Animation myAlpha1 = AnimationUtils.loadAnimation(this, R.anim.myalpha1);
        welcome.startAnimation(myAlpha);
        make.startAnimation(myAlpha1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    if(checked.getUserLoggedIn()){
                        Intent i = new Intent(SplashActivity.this, MenuActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
            }
        }, 5 * 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SplashActivity.this,"Спасибо, мы получили доступ!", Toast.LENGTH_SHORT).show();
                // perform your action here

            } else {
                Toast.makeText(SplashActivity.this,"Доступ не был получен!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
