package com.code.publicando.publicando.activitys;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.ImageView;

import com.code.publicando.publicando.R;
import com.code.publicando.publicando.fragments.ChooseZoneFragment;
import com.code.publicando.publicando.fragments.FavoriteFragment;
import com.code.publicando.publicando.fragments.MainFragment;
import com.code.publicando.publicando.fragments.MyAdvertisementsFragment;
import com.code.publicando.publicando.fragments.ServiceDetailFragment;
import com.code.publicando.publicando.fragments.ServiceDetalDialogFragment;
import com.code.publicando.publicando.fragments.ServiceListFragment;

import java.util.Timer;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;
import static com.code.publicando.publicando.activitys.LoginActivity.MY_PREFS_NAME;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    ViewPager viewPager;
    private int[] layouts = {R.layout.first_slide,R.layout.detail_first_slide,R.layout.third_slide};
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 6000;
    private View baseLayout;
    private ImageView img;
    //private String url = "10.0.2.2/api/version";
    private Integer mIdUser;
    private String mLatitude;
    private String mLongitud;
    private String mRadius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

/*        SharedPreferences spreferences =  getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor spreferencesEditor = spreferences.edit();
        spreferencesEditor.clear();
        spreferencesEditor.commit();*/

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Integer restoredText = prefs.getInt("idUser", 0);
        if (restoredText != 0) {
            mIdUser = prefs.getInt("idUser", 0);
            mLatitude = prefs.getString("Latitude", null);
            mLongitud = prefs.getString("Longitud", null);
            mRadius = prefs.getString("Radius", null);
/*            item = findViewById(R.id.registrarse);
            item.setVisible(false);*/
        }
        else
         {
             Bundle b = getIntent().getExtras();
             if(b != null){
                 mIdUser = b.getInt("idUser");
                 mLatitude = b.getString("Latitude");
                 mLongitud = b.getString("Longitud");
                 mRadius = b.getString("Radius");
             }
         }

        setActionBarTitle("Publicando");

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
    }



    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            else {
                Fragment fragment = new MainFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
            }
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem registrar = menu.findItem(R.id.registrarse);
        MenuItem cerrar = menu.findItem(R.id.cerrarSesion);
        if (mIdUser != null)
        {
            registrar.setVisible(false);
            cerrar.setVisible(true);
        }
        else{
            registrar.setVisible(true);
            cerrar.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cerrarSesion) {
            getSupportFragmentManager().beginTransaction().
                    remove(getSupportFragmentManager().findFragmentById(R.id.content_frame)).commit();
            SharedPreferences spreferences =  getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor spreferencesEditor = spreferences.edit();
            spreferencesEditor.clear();
            spreferencesEditor.commit();
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }
        if (id == R.id.registrarse) {
            getSupportFragmentManager().beginTransaction().
                    remove(getSupportFragmentManager().findFragmentById(R.id.content_frame)).commit();
            Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
            myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            MainActivity.this.startActivity(myIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        boolean fragmentTransaction = false;
        Fragment fragment = null;
        Bundle args;
        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_publicar:
                getSupportFragmentManager().beginTransaction().
                        remove(getSupportFragmentManager().findFragmentById(R.id.content_frame)).commit();

/*                Intent myIntent = new Intent(MainActivity.this, CreatePostActivity.class);
                myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                myIntent.putExtra("idUser", mIdUser); //Optional parameters
                MainActivity.this.startActivity(myIntent);*/
                if(mIdUser != null){
                    Intent myIntent = new Intent(MainActivity.this, CreatePostActivity.class);
                    myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    myIntent.putExtra("idUser", mIdUser); //Optional parameters
                    MainActivity.this.startActivity(myIntent);
                }
                else{
                    Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
                    myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    MainActivity.this.startActivity(myIntent);
                }
                break;
            case R.id.nav_anuncios:
                fragment = new MyAdvertisementsFragment();
                args = new Bundle();
                args.putString("idUser", mIdUser.toString());
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
                break;
            case R.id.nav_fav:
                fragment = new FavoriteFragment();
                args = new Bundle();
                args.putString("idUser", mIdUser.toString());
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
                break;
        }
        /*if (id == R.id.nav_publicar) {
            getSupportFragmentManager().beginTransaction().
                    remove(getSupportFragmentManager().findFragmentById(R.id.content_frame)).commit();

            Intent myIntent = new Intent(MainActivity.this, CreatePostActivity.class);
            myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            myIntent.putExtra("idUser", mIdUser); //Optional parameters
            MainActivity.this.startActivity(myIntent);
        } else if (id == R.id.nav_anuncios) {
            fragment = new MyAdvertisementsFragment();
            args = new Bundle();
            args.putString("idUser", mIdUser.toString());
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Fragment fragment;
        Bundle args = new Bundle();
        Intent myIntent = new Intent(MainActivity.this, CreatePostActivity.class);
        if(mIdUser != null){
            args.putInt("IdUser", mIdUser);
        }
        switch (id) {
            case R.id.btnServicio:
                fragment = new ServiceListFragment();
                //args = new Bundle();
                args.putString("Type", "Servicio");
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
                break;
            case R.id.btnComercio:
                fragment = new ServiceListFragment();
                //args = new Bundle();
                args.putString("Type", "Comercio");
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
                break;
            case R.id.btnPromo:
                fragment = new ServiceListFragment();
                //args = new Bundle();
                args.putString("Type", "Promocion");
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
                break;
            case R.id.imageView:

                showInfoAlert(R.id.imageView);
                break;
            case R.id.btnZona:
                fragment = new ChooseZoneFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
                break;
            case R.id.btnRubro:
/*                ServiceDetalDialogFragment dialog = new ServiceDetalDialogFragment();

                dialog.show(getFragmentManager(), "ServiceDetalDialogFragment");*/
                //fragment = new ServiceDetailFragment();
                //getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
                break;
            case R.id.btnPublicar:
                    getSupportFragmentManager().beginTransaction().
                            remove(getSupportFragmentManager().findFragmentById(R.id.content_frame)).commit();
                    if(mIdUser != null){
                        myIntent = new Intent(MainActivity.this, CreatePostActivity.class);
                        myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                        myIntent.putExtra("idUser", mIdUser); //Optional parameters
                        MainActivity.this.startActivity(myIntent);
                    }
                    else{
                        myIntent = new Intent(MainActivity.this, HomeActivity.class);
                        myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                        MainActivity.this.startActivity(myIntent);
                    }
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
