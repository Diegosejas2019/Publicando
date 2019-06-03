package com.code.publicando.publicando.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.code.publicando.publicando.R;
import com.code.publicando.publicando.clases.ListAdapter;
import com.code.publicando.publicando.clases.ListFilter;
import com.code.publicando.publicando.clases.Servicios;
import com.code.publicando.publicando.clases.ServiciosAdapter;
import com.code.publicando.publicando.fragments.FavoriteFragment;
import com.code.publicando.publicando.fragments.MainFragment;
import com.code.publicando.publicando.fragments.MyAdvertisementsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;
import static com.code.publicando.publicando.activitys.LoginActivity.MY_PREFS_NAME;

public class NewServiceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    List<ListFilter> serviciosList;

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
    private Double mLatitude;
    private Double mLongitud;
    private Integer mRadius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_service);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setActionBarTitle("Publicando");

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerNew);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null){
            mIdUser = b.getInt("idUser");
            mLatitude = b.getDouble("Latitud");
            mLongitud = b.getDouble("Longuitud");
            mRadius = b.getInt("Radius");
        }

        //initializing the productlist
        serviciosList = new ArrayList<>();


        //adding some items to our list
        serviciosList.add(
                new ListFilter(
                        1,
                        "Abogado"));

        serviciosList.add(
                new ListFilter(
                        2,
                        "Arquitecto"));

        serviciosList.add(
                new ListFilter(
                        3,
                        "Electricista"));

        serviciosList.add(
                new ListFilter(
                        4,
                        "Pintor"));

        serviciosList.add(
                new ListFilter(
                        5,
                        "Gacista"));

        serviciosList.add(
                new ListFilter(
                        6,
                        "Programador"));
        //creating recyclerview adapter
        ListAdapter adapterServicio = new ListAdapter(NewServiceActivity.this, serviciosList,mIdUser,mLatitude,mLongitud);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapterServicio);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
/*        int count = getFragmentManager().getBackStackEntryCount();

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
        }*/
        Intent myIntent = new Intent(NewServiceActivity.this, MainActivity.class);
        myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        //myIntent.putExtra("key", IDuser); //Optional parameters
        NewServiceActivity.this.startActivity(myIntent);
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
            startActivity(new Intent(NewServiceActivity.this, HomeActivity.class));
            finish();
        }
        if (id == R.id.registrarse) {
            getSupportFragmentManager().beginTransaction().
                    remove(getSupportFragmentManager().findFragmentById(R.id.content_frame)).commit();
            Intent myIntent = new Intent(NewServiceActivity.this, HomeActivity.class);
            myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            NewServiceActivity.this.startActivity(myIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean fragmentTransaction = false;
        Fragment fragment = null;
        Bundle args;
        int id = item.getItemId();

        if(mIdUser == null)
        {

            getSupportFragmentManager().beginTransaction().
                    remove(getSupportFragmentManager().findFragmentById(R.id.content_frame)).commit();

            Intent myIntent = new Intent(NewServiceActivity.this, HomeActivity.class);
            myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            NewServiceActivity.this.startActivity(myIntent);
        }
        else{
            switch (id)
            {
                case R.id.nav_publicar:
                    Intent myIntent = new Intent(NewServiceActivity.this, CreatePostActivity.class);
                    myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    myIntent.putExtra("idUser", mIdUser); //Optional parameters
                    NewServiceActivity.this.startActivity(myIntent);
                    break;
                case R.id.nav_anuncios:
                    myIntent = new Intent(NewServiceActivity.this, MainActivity.class);
                    myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    myIntent.putExtra("idUser", mIdUser); //Optional parameters
                    myIntent.putExtra("Frame", "Anuncios"); //Optional parameters
                    NewServiceActivity.this.startActivity(myIntent);
                    break;
                case R.id.nav_fav:
                    myIntent = new Intent(NewServiceActivity.this, MainActivity.class);
                    myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    myIntent.putExtra("idUser", mIdUser); //Optional parameters
                    myIntent.putExtra("Frame", "Favorite"); //Optional parameters
                    NewServiceActivity.this.startActivity(myIntent);
                    break;
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
