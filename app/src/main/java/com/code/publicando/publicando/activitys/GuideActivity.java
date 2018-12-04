package com.code.publicando.publicando.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.code.publicando.publicando.R;
import com.code.publicando.publicando.clases.MpagerAdapter;
import com.code.publicando.publicando.clases.PreferenceManager;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;
import static com.code.publicando.publicando.activitys.LoginActivity.MY_PREFS_NAME;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mPager;
    private int[] layouts = {R.layout.first_slide,R.layout.detail_first_slide,R.layout.third_slide};
    private MpagerAdapter mpagerAdapter;

    private LinearLayout Dots_Layout;
    private ImageView[] dots;
    private Button skip,next;
    private Integer mIdUser;
    private Integer mRadius;
    private Integer mLatitude;
    private Integer mLongitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (new PreferenceManager(this).checkPreference())
        {
            loadHome();
        }

        if (Build.VERSION.SDK_INT >= 19)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else{
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        setContentView(R.layout.activity_guide);

        mPager = (ViewPager) findViewById(R.id.viewPager);
        mpagerAdapter = new MpagerAdapter(layouts,this);
        mPager.setAdapter(mpagerAdapter);

        Dots_Layout = (LinearLayout) findViewById(R.id.dotsLayout);

        skip = findViewById(R.id.skip);
        next = findViewById(R.id.next);
        skip.setOnClickListener(this);
        next.setOnClickListener(this);

        createDots(0);

        Bundle b = getIntent().getExtras();
        if(b != null){
            mIdUser = b.getInt("idUser");
            mLatitude = b.getInt("Latitude");
            mLongitud = b.getInt("Longitud");
            mRadius = b.getInt("Radius");
        }

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                createDots(position);

                if (position == layouts.length-1)
                {
                    next.setText("Comenzar");
                    skip.setVisibility(View.INVISIBLE);
                }
                else
                {
                    next.setText("Siguiente");
                    skip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void createDots(int current_position)
    {
        if (Dots_Layout!=null)
            Dots_Layout.removeAllViews();

        dots = new ImageView[layouts.length];

        for (int i= 0; i < layouts.length; i++)
        {
            dots[i] = new ImageView(this);
            if (i==current_position)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.tes));
            }
            else
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.default_dots));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.setMargins(4,0,4,0);

            Dots_Layout.addView(dots[i],params);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.next:
                loadNextSlide();
                break;
            case R.id.skip:
                loadHome();
                new PreferenceManager(this).writePreference();
                break;
        }
    }

    private void loadHome()
    {
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("idUser", mIdUser);
        editor.putInt("Latitude", mLatitude);
        editor.putInt("Longitud", mLongitud);
        editor.putInt("Radius", mRadius);
        editor.apply();
        new PreferenceManager(GuideActivity.this).clearPreference();
        Intent myIntent = new Intent(GuideActivity.this, MainActivity.class);
        myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        myIntent.putExtra("idUser", mIdUser);
        myIntent.putExtra("Latitude", mLatitude);
        myIntent.putExtra("Longitud", mLongitud);
        myIntent.putExtra("Radius", mRadius);

        GuideActivity.this.startActivity(myIntent);
        finish();
    }

    private void loadNextSlide()
    {
        int next_slide = mPager.getCurrentItem() + 1;

        if (next_slide<layouts.length)
        {
            mPager.setCurrentItem(next_slide);
        }
        else
        {
            loadHome();
            new PreferenceManager(this).writePreference();
        }
    }
}
