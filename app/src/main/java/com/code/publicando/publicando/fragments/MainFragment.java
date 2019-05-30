package com.code.publicando.publicando.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.code.publicando.publicando.activitys.MainActivity;
import com.code.publicando.publicando.clases.JSONParser;
import com.code.publicando.publicando.clases.MpagerAdapter;
import com.code.publicando.publicando.R;
import com.code.publicando.publicando.clases.Url;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener{

    private static final int FEATURED_IMAGE_RATIO = 0;
    ViewPager viewPager;
    private int[] layouts = {R.layout.detail_first_slide,R.layout.detail_second_slide,R.layout.detail_third_slide};
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 2000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 6000;
    private View baseLayout;
    private ImageView img;
    private Context context;
    JSONArray jsonarray;
    JSONObject jsonobject;
    //private String url = "http://10.0.2.2/";
    //private String url = "http://192.168.1.149/";
    JSONParser jParser = new JSONParser();
    private ProgressDialog pDialog;
    private Integer IDuser;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private static final String TAG_SUCCESS = "StatusCode";
    private static final float MIN_SCALE = 0.75f;
    private List<String> Direcciones;
    private Handler myHandler;
    private Runnable Update;
    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = inflater.getContext();
        ((MainActivity) getActivity())
                .setActionBarTitle("Publicando");
        //new ObtenerDestacados().execute();

        try {
            Boolean str_result = new ObtenerDestacados().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        MpagerAdapter viewPagerAdapter = new MpagerAdapter(layouts,context);

        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();

                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0f);

                } else if (position <= 0) { // [-1,0]
                    // Use the default slide transition when moving to the left page
                    view.setAlpha(1f);
                    view.setTranslationX(0f);
                    view.setScaleX(1f);
                    view.setScaleY(1f);

                } else if (position <= 1) { // (0,1]
                    // Fade the page out.
                    view.setAlpha(1 - position);

                    // Counteract the default slide transition
                    view.setTranslationX(pageWidth * -position);

                    // Scale the page down (between MIN_SCALE and 1)
                    float scaleFactor = MIN_SCALE
                            + (1 - MIN_SCALE) * (1 - Math.abs(position));
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0f);
                }
            }
        });
        viewPager.setAdapter(viewPagerAdapter);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((MainActivity) getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        //Toast.makeText(context, String.valueOf(height) + ',' +  String.valueOf(width), Toast.LENGTH_LONG).show();
        //FEATURED_IMAGE_RATIO = height / 2;
/*        ViewTreeObserver viewTreeObserver = viewPager.getViewTreeObserver();
        viewTreeObserver
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);

                        int viewPagerWidth = viewPager.getWidth();
                        float viewPagerHeight = (float) (viewPagerWidth * 1000);

                        layoutParams.width = viewPagerWidth;
                        layoutParams.height = (int) viewPagerHeight;

                        viewPager.setLayoutParams(layoutParams);
                        viewPager.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });*/



        /*After setting the adapter use the timer */
        //final Handler handler = new Handler();
        myHandler = new Handler();
        Update = new Runnable() {

            public void run() {
                if (currentPage == layouts.length) {
                    currentPage = 0;
                }
                Url url =  new Url();
                //new ObtenerDestacados().execute();
                 switch (currentPage)
                {
                    case 0:
                        baseLayout = viewPager.findViewWithTag(layouts[currentPage]);
                        if(baseLayout == null)
                        {
                            MpagerAdapter viewPagerAdapter = new MpagerAdapter(layouts,context);
                            viewPager.setAdapter(viewPagerAdapter);
                            baseLayout = viewPager.findViewWithTag(layouts[currentPage]);
                        }
                        img = baseLayout.findViewById(R.id.imagen);


                        if (Direcciones.size() == 0 || Direcciones.size() > 0)
                        {
                            Picasso.with(context)
                                    //.load(url.getDireccion() + "/Imagenes/" + Direcciones.get(0))
                                    .load("http://mrryzen22.asuscomm.com:49152/Imagenes/" + Direcciones.get(0) )
                                    .resize(1500, 1400)
                                    .into(img);
                        }
                        //img.setImageResource(R.drawable.inmobiliaria);
                        break;
                    case 1:
                        baseLayout = viewPager.findViewWithTag(layouts[currentPage]);
                        if(baseLayout == null)
                        {
                            MpagerAdapter viewPagerAdapter = new MpagerAdapter(layouts,context);
                            viewPager.setAdapter(viewPagerAdapter);
                            baseLayout = viewPager.findViewWithTag(layouts[currentPage]);
                        }
                        img = baseLayout.findViewById(R.id.imagen);
                        Picasso.with(context)
                                //.load(url.getDireccion() + "/Imagenes/" + Direcciones.get(1))
                                .load("http://mrryzen22.asuscomm.com:49152/Imagenes/" + Direcciones.get(1))
                                .resize(1500, 1400)
                                .into(img);
                        //img.setImageResource(R.drawable.heladeria);
                        break;
                    case 2:
                        baseLayout = viewPager.findViewWithTag(layouts[currentPage]);
                        if(baseLayout == null)
                        {
                            MpagerAdapter viewPagerAdapter = new MpagerAdapter(layouts,context);
                            viewPager.setAdapter(viewPagerAdapter);
                            baseLayout = viewPager.findViewWithTag(layouts[currentPage]);
                        }
                        img = baseLayout.findViewById(R.id.imagen);
                        //img.setImageResource(R.drawable.comidas);
                        Picasso.with(context)
                                //.load(url.getDireccion() + "/Imagenes/" + Direcciones.get(2))
                                .load("http://mrryzen22.asuscomm.com:49152/Imagenes/" + Direcciones.get(2))
                                .resize(1500, 1400)
                                .into(img);
                        break;
                }



                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                myHandler.post(Update);
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

    @Override
    public void onDestroy() {
        timer.cancel();  // Terminates this timer, discarding any currently scheduled tasks.
        timer.purge();
        myHandler.removeCallbacks(Update);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }

    public class ObtenerDestacados extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Obteniendo publicaciones...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            List parames = new ArrayList();

            Direcciones = new ArrayList<String>() ;
            Url url = new Url();
            JSONObject json = jParser.makeHttpRequest(url.getDireccion() + "/api/master/GetImageUrl", "GET",parames);

            try {
                if (json != null){
                    jsonarray = json.getJSONArray("Imagenes");
                    for (int i = 0; i < jsonarray.length(); i++) {
                        jsonobject = jsonarray.getJSONObject(i);
                        String direccion = jsonobject.getString("ImageUrl");
                        Direcciones.add(direccion.substring((direccion.length()-6)).replaceAll("\\\\", ""));
                    }
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            myMethod("ok");
            pDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            pDialog.dismiss();
            //mAuthTask = null;
            Toast.makeText(context,"Sin conexión",Toast.LENGTH_LONG).show();
        }

        private String myMethod(String myValue) {
            //handle value
            return myValue;
        }
    }


}
