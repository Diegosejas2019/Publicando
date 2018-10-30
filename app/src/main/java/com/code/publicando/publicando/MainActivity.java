package com.code.publicando.publicando;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.code.publicando.publicando.fragments.ChooseZoneFragment;
import com.code.publicando.publicando.fragments.MainFragment;
import com.code.publicando.publicando.fragments.ServiceDetalDialogFragment;
import com.code.publicando.publicando.fragments.ServiceListFragment;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    ViewPager viewPager;
    private int[] layouts = {R.layout.first_slide,R.layout.second_slide,R.layout.third_slide};
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 6000;
    private View baseLayout;
    private ImageView img;
    private String url = "10.0.2.2/api/version";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
/*        viewPager = (ViewPager) findViewById(R.id.viewPager);

        MpagerAdapter viewPagerAdapter = new MpagerAdapter(layouts,MainActivity.this);

        viewPager.setAdapter(viewPagerAdapter);

        *//*After setting the adapter use the timer *//*
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
        }, DELAY_MS, PERIOD_MS);*/
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        /*else {
            super.onBackPressed();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.cerrarSesion) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        boolean fragmentTransaction = false;
        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } /*else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Fragment fragment;
        switch (id) {
            case R.id.btnServicio:
                fragment = new ServiceListFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
                break;
            case R.id.btnComercio:
                // do something else
                break;
            case R.id.btnPromo:
                // i'm lazy, do nothing
                break;
            case R.id.imageView:
                showInfoAlert(R.id.imageView);
                break;
            case R.id.btnZona:
                fragment = new ChooseZoneFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
                break;
            case R.id.btnRubro:
                ServiceDetalDialogFragment dialog = new ServiceDetalDialogFragment();

                dialog.show(getFragmentManager(), "ServiceDetalDialogFragment");
                break;
        }
    }

    private void showInfoAlert(int ids)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.service_detail, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setNegativeButton("Cerrar",null );
        ImageView editText = (ImageView) dialogView.findViewById(R.id.ImageDetail);
        ImageView imagen = findViewById(ids);
        //editText.setImageResource(R.drawable.comidas);
        editText.setImageDrawable(imagen.getDrawable());
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
 /*
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();*/
    }
}
