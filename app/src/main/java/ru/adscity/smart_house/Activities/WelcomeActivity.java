package ru.adscity.smart_house.Activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ru.adscity.smart_house.Adapter.FPagerAdapter;
import ru.adscity.smart_house.R;
import ru.adscity.smart_house.Welcome_Section.*;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {


    private final String SP_SETTING = "mySetting";
    private final String KEY_CHECK = "EnteredIn";
    private SharedPreferences check;
    private Button btnSkip;
    private Button btnNext;
    private Button btnGotIt;
    private LinearLayout dotsLayot;
    private final Welcome_first first = new Welcome_first();
    private final Welcome_second second = new Welcome_second();
    private final Welcome_third third = new Welcome_third();
    private final Welcome_fourth fourth = new Welcome_fourth();
    private final Welcome_fifth fifth = new Welcome_fifth();
    private final Fragment[] fragmentPages = new Fragment[]{first, second, third, fourth,fifth};
    private ViewPager mPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        check = WelcomeActivity.this.getSharedPreferences(SP_SETTING, 0);
        setContentView(R.layout.activity_welcome);
        if (check.getBoolean(KEY_CHECK, false)) {
            transition();
        }

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
        dotsLayot = findViewById(R.id.dotsLayoutFragment);
        btnSkip = findViewById(R.id.btnSkipFragment);
        btnNext = findViewById(R.id.btnNextFragment);
        btnGotIt = findViewById(R.id.btnGotItFragment);
        btnSkip.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnGotIt.setOnClickListener(this);

        FPagerAdapter fPagerAdapter = new FPagerAdapter(getSupportFragmentManager(), fragmentPages);
         mPager = findViewById(R.id.viewPagerFragment);
        mPager.setAdapter(fPagerAdapter);
        CreateDots(0);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int p) {
                CreateDots(p);
                if(p == 4){
                    btnGotIt.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.GONE);
                    btnSkip.setVisibility(View.GONE);
                } else{
                    btnGotIt.setVisibility(View.GONE);
                    btnNext.setVisibility(View.VISIBLE);
                    btnSkip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSkipFragment:
                transition();
                break;
            case R.id.btnNextFragment:
                mPager.setCurrentItem(mPager.getCurrentItem()+1);
                break;
            case R.id.btnGotItFragment:
                transition();
                break;
        }
    }

    private void transition() {
        SharedPreferences.Editor spEditor = check.edit();
        spEditor.putBoolean(KEY_CHECK, true);
        spEditor.apply();
        Intent i = new Intent(WelcomeActivity.this, SplashActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    private void CreateDots(int current_possition) {
        if (dotsLayot != null) {
            dotsLayot.removeAllViews();
            ImageView[] dots = new ImageView[fragmentPages.length];
            for (int i = 0; i < fragmentPages.length; i++) {
                dots[i] = new ImageView(this);
                if (i == current_possition) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots_welcome));
                } else {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_dots_welcome));
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(4, 0, 4, 0);
                dotsLayot.addView(dots[i], params);
            }
        }
    }
}
