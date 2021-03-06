package com.code.publicando.publicando.activitys;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.code.publicando.publicando.R;
import com.code.publicando.publicando.clases.Product;
import com.code.publicando.publicando.clases.ProductAdapter;
import com.code.publicando.publicando.clases.Servicios;
import com.code.publicando.publicando.clases.ServiciosAdapter;

import java.util.ArrayList;
import java.util.List;

public class ServicesList_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    //a list to store all the products
    List<Servicios> serviciosList;

    //the recyclerview
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_list_);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the productlist
        serviciosList = new ArrayList<>();


        //adding some items to our list
        serviciosList.add(
                new Servicios(
                        1,
                        "Comercio modelo 1"));

        serviciosList.add(
                new Servicios(
                        1,
                        "Resto Modelo 1"));

        serviciosList.add(
                new Servicios(
                        1,
                        "Promos Modelo 1"));

        serviciosList.add(
                new Servicios(
                        1,
                        "Comercio modelo 1"));

        serviciosList.add(
                new Servicios(
                        1,
                        "Resto Modelo 1"));

        serviciosList.add(
                new Servicios(
                        1,
                        "Promos Modelo 1"));
        //creating recyclerview adapter
        ServiciosAdapter adapter = new ServiciosAdapter(this, serviciosList,1);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_publicar) {
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
}
