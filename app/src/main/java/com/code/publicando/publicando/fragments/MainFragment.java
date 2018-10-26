package com.code.publicando.publicando.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.code.publicando.publicando.MainActivity;
import com.code.publicando.publicando.MpagerAdapter;
import com.code.publicando.publicando.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    ViewPager viewPager;
    private int[] layouts = {R.layout.first_slide,R.layout.second_slide,R.layout.third_slide};
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 6000;
    private View baseLayout;
    private ImageView img;
    private Context context;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = inflater.getContext();
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        MpagerAdapter viewPagerAdapter = new MpagerAdapter(layouts,context);

        viewPager.setAdapter(viewPagerAdapter);

        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == layouts.length) {
                    currentPage = 0;
                }

                 switch (currentPage)
                {
                    case 0:
                        baseLayout = viewPager.findViewWithTag(layouts[currentPage]);
                        img = baseLayout.findViewById(R.id.imagen);
                        img.setImageResource(R.drawable.inmobiliaria);
                        break;
                    case 1:
                        baseLayout = viewPager.findViewWithTag(layouts[currentPage]);
                        img = baseLayout.findViewById(R.id.imagen);
                        img.setImageResource(R.drawable.heladeria);
                        break;
                    case 2:
                        baseLayout = viewPager.findViewWithTag(layouts[currentPage]);
                        img = baseLayout.findViewById(R.id.imagen);
                        img.setImageResource(R.drawable.comidas);
                        break;
                }



                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
/*
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
    }
}
