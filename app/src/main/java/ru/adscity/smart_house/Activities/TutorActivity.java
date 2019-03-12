package ru.adscity.smart_house.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ru.adscity.smart_house.Adapter.FPagerAdapter;
import ru.adscity.smart_house.R;
import ru.adscity.smart_house.Tutor_Section.*;

import ru.adscity.smart_house.User_Section.UserLocalStore;

public class TutorActivity extends AppCompatActivity implements View.OnClickListener {

    private UserLocalStore userLocalStore;
    private LinearLayout Dots_Layout;
    private ImageButton btnSkipTutor;
    private ImageButton btnFirstScreenTutor;
    private ImageButton btnLastScreenTutor;
    private ImageButton btnNextSlideTutor;
    private ImageButton btnBackSlideTutor;
    private Button btnReportABug;
    private ViewPager mPager;
    private  Fragment[] fragmentPages;

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
        final Tutor_first first = new Tutor_first(screenInches);
        final Tutor_second second = new Tutor_second(screenInches);
        final Tutor_third third = new Tutor_third(screenInches);
        final Tutor_fourth fourth = new Tutor_fourth(screenInches);
        final Tutor_fifth fifth = new Tutor_fifth(screenInches);
        final Tutor_sixth sixth = new Tutor_sixth(screenInches);
        final Tutor_seventh seventh = new Tutor_seventh(screenInches);
        final Tutor_eighth eighth = new Tutor_eighth(screenInches);
        final Tutor_ninth ninth = new Tutor_ninth(screenInches);
         fragmentPages = new Fragment[]{first, second, third, fourth, fifth, sixth, seventh, ninth, eighth};
        if(screenInches > 9){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.activity_tutor);
        btnReportABug = (Button) findViewById(R.id.btnReportABug);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
        userLocalStore = new UserLocalStore(this);
        if(userLocalStore.getLanguage(TutorActivity.this).equals("ru")){
            btnReportABug.setText("Сообщить об ошибке");
        }
        btnReportABug.setVisibility(View.GONE);
        btnReportABug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri address = Uri.parse("https://mcflypixc.ru/#report");
                Intent openlinkIntent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(openlinkIntent);
            }
        });
        btnSkipTutor = (ImageButton) findViewById(R.id.btnSkipTutor);
        btnFirstScreenTutor = (ImageButton) findViewById(R.id.btnFirstScreenTutor);
        btnLastScreenTutor = (ImageButton) findViewById(R.id.btnLastScreenTutor);
        btnNextSlideTutor = (ImageButton) findViewById(R.id.btnNextSlideTutor);
        btnBackSlideTutor = (ImageButton) findViewById(R.id.btnBackSlideTutor);
        btnSkipTutor.setOnClickListener(this);
        btnFirstScreenTutor.setOnClickListener(this);
        btnLastScreenTutor.setOnClickListener(this);
        btnNextSlideTutor.setOnClickListener(this);
        btnBackSlideTutor.setOnClickListener(this);
        Dots_Layout = findViewById(R.id.dotsLayout);
        FPagerAdapter fPagerAdapter = new FPagerAdapter(getSupportFragmentManager(), fragmentPages);
        mPager = findViewById(R.id.viewPager);
        mPager.setAdapter(fPagerAdapter);
        CreateDots(0);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {
                btnReportABug.setVisibility(View.GONE);
                if (position != 0 && position != fragmentPages.length - 1) {
                    btnFirstScreenTutor.setVisibility(View.GONE);
                    btnLastScreenTutor.setVisibility(View.GONE);
                    btnNextSlideTutor.setVisibility(View.VISIBLE);
                    btnBackSlideTutor.setVisibility(View.VISIBLE);
                } else if (position == 0) {
                    btnFirstScreenTutor.setVisibility(View.VISIBLE);
                    btnLastScreenTutor.setVisibility(View.GONE);
                    btnNextSlideTutor.setVisibility(View.GONE);
                    btnBackSlideTutor.setVisibility(View.GONE);
                } else if (position == fragmentPages.length - 1) {
                    btnFirstScreenTutor.setVisibility(View.GONE);
                    btnLastScreenTutor.setVisibility(View.VISIBLE);
                    btnNextSlideTutor.setVisibility(View.GONE);
                    btnBackSlideTutor.setVisibility(View.GONE);
                    btnReportABug.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                CreateDots(position);
                btnReportABug.setVisibility(View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void CreateDots(int current_possition) {
        if (Dots_Layout != null) {
            Dots_Layout.removeAllViews();

            ImageView[] dots = new ImageView[fragmentPages.length];
            for (int i = 0; i < fragmentPages.length; i++) {
                dots[i] = new ImageView(this);
                if (i == current_possition) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots));
                } else {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_dots));
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(4, 0, 4, 0);
                Dots_Layout.addView(dots[i], params);
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnSkipTutor:
                Intent i = new Intent(TutorActivity.this, MenuActivity.class);
                startActivity(i);
                finish();
                return;
            case R.id.btnFirstScreenTutor:
                mPager.setCurrentItem(mPager.getCurrentItem()+1);
                return;
            case R.id.btnNextSlideTutor:
                btnNextSlideTutor.setActivated(false);
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                btnNextSlideTutor.setActivated(true);
                return;
            case R.id.btnBackSlideTutor:
                btnBackSlideTutor.setActivated(false);
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                btnBackSlideTutor.setActivated(true);
                return;
            case R.id.btnLastScreenTutor:
                btnLastScreenTutor.setActivated(false);
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                btnLastScreenTutor.setActivated(true);

        }

    }
}
