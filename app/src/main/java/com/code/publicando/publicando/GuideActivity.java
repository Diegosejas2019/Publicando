package com.code.publicando.publicando;

import android.os.Build;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class GuideActivity extends AppCompatActivity {

    private ViewPager mPager;
    private int[] layouts = {R.layout.first_slide,R.layout.second_slide,R.layout.third_slide};
    private  MpagerAdapter mpagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }
}
