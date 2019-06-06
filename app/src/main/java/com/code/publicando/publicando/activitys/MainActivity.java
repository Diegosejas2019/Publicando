package com.code.publicando.publicando.activitys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.code.publicando.publicando.R;
import com.code.publicando.publicando.clases.JSONParser;
import com.code.publicando.publicando.clases.Ubicacion;
import com.code.publicando.publicando.clases.Url;
import com.code.publicando.publicando.fragments.ChooseZoneFragment;
import com.code.publicando.publicando.fragments.FavoriteFragment;
import com.code.publicando.publicando.fragments.MainFragment;
import com.code.publicando.publicando.fragments.MyAdvertisementsFragment;
import com.code.publicando.publicando.fragments.ServiceListFragment;
import com.code.publicando.publicando.fragments.filter;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;
import static com.code.publicando.publicando.activitys.LoginActivity.MY_PREFS_NAME;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    JSONParser jParser = new JSONParser();
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
    private int mRadius;
    private static final String TAG_SUCCESS = "StatusCode";
    private ProgressDialog pDialog;
    Ubicacion ubicacion = new Ubicacion();
    Gson gson = new Gson();

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
        try {Integer restoredText = prefs.getInt("idUser", 0);
            if (restoredText != 0) {
                mIdUser = prefs.getInt("idUser", 0);
                new UserUbicationTask().execute();
                /*mLatitude = prefs.getFloat("Latitude",0);
                mLongitud = prefs.getFloat("Longitud",0);
                mRadius = prefs.getString("Radius", null);*/
            }
            else
            {
                Bundle b = getIntent().getExtras();
                if(b != null){
                    mIdUser = b.getInt("idUser");
                    String clase = getIntent().getStringExtra("Ubicacion");
                    ubicacion = gson.fromJson(clase, Ubicacion.class);
                    mLatitude = Double.parseDouble(ubicacion.getLatitude());
                    mLongitud = Double.parseDouble(ubicacion.getLongitude());
                    mRadius = ubicacion.getRadius();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



        setActionBarTitle("Publicando");

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle b = getIntent().getExtras();
        if(b != null){
            String frame = b.getString("Frame",null);
            mIdUser = b.getInt("idUser",0);
            if (frame != null){
                if (frame.equals("Anuncios"))
                {
                    Fragment fragment = new MyAdvertisementsFragment();
                    Bundle args = new Bundle();
                    args.putInt("idUser", mIdUser);
                    mLatitude = b.getDouble("Latitude",0);
                    mLongitud = b.getDouble("Longuitude",0);
                    args.putDouble("Latitude", mLatitude);
                    args.putDouble("Longuitude", mLongitud);
                    fragment.setArguments(args);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
                }
                if (frame.equals("Favorite"))
                {
                    Fragment fragment = new FavoriteFragment();
                    Bundle args = new Bundle();
                    args.putString("idUser", mIdUser.toString());
                    mLatitude = b.getDouble("Latitude",0);
                    mLongitud = b.getDouble("Longuitude",0);
                    args.putDouble("Latitude", mLatitude);
                    args.putDouble("Longuitude", mLongitud);
                    fragment.setArguments(args);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
                }
            }
            else{
                Fragment fragment = new MainFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
            }
        }
        else{
            Fragment fragment = new MainFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
        }



    }

    public class UserUbicationTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Ingresando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        UserUbicationTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean flag = false;
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("IdUser", String.valueOf(mIdUser)));

            String Resultado="";
            Url url = new Url();
            JSONObject json = jParser.makeHttpRequest(url.getDireccion() + "/api/master/GetUbication", "POST", nameValuePairs);

            try {
                if (json != null){
                    int success = json.getInt(TAG_SUCCESS);
                    if (success == 200){
                        mRadius = json.getInt("Radius");
                        if (mRadius > 0)
                        {
                            mLatitude = json.getDouble("Latitude");
                            mLongitud = json.getDouble("Longitude");
                        }
                        flag = true;}
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Resultado = e.getMessage();
            }
            return flag;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            pDialog.dismiss();
            if (mRadius == 0)
            {
                Intent mainIntent = new Intent(MainActivity.this,
                        ChooseZoneActivity.class);
                mainIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                mainIntent.putExtra("idUser", mIdUser);
                startActivity(mainIntent);
                MainActivity.this.finish();
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            }
/*            if (success) {

                Intent mainIntent;
                if(mRadius > 0)
                {
                    mLatitude
                    mainIntent = new Intent(SignInActivity.this,
                            MainActivity.class);
                    mainIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    mainIntent.putExtra("idUser", IDuser);
                    mainIntent.putExtra("Radius", Radius);
                    mainIntent.putExtra("Latitude", Latitude);
                    mainIntent.putExtra("Longuitude", Longuitude);
                    startActivity(mainIntent);
                    SignInActivity.this.finish();
                    overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                }
            } */
        }

        @Override
        protected void onCancelled() {
            pDialog.dismiss();
            //mAuthTask = null;
            Toast.makeText(MainActivity.this,"Sin conexión",Toast.LENGTH_LONG).show();
        }
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
/*                getSupportFragmentManager().beginTransaction().
                        remove(getSupportFragmentManager().findFragmentById(R.id.content_frame)).commit();
                Intent myIntent = new Intent(MainActivity.this, MA.class);
                myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                myIntent.putExtra("idUser", mIdUser); //Optional parameters
                MainActivity.this.startActivity(myIntent);
                MainActivity.this.finish();*/
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

        if(mIdUser == null)
        {

            getSupportFragmentManager().beginTransaction().
                    remove(getSupportFragmentManager().findFragmentById(R.id.content_frame)).commit();

        Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
        myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        MainActivity.this.startActivity(myIntent);
        }
        else{
            switch (id)
            {
                case R.id.nav_publicar:
                    getSupportFragmentManager().beginTransaction().
                            remove(getSupportFragmentManager().findFragmentById(R.id.content_frame)).commit();
    /*                Intent myIntent = new Intent(MainActivity.this, CreatePostActivity.class);
                    myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    myIntent.putExtra("idUser", mIdUser); //Optional parameters
                    MainActivity.this.startActivity(myIntent);*/
                    //if(mIdUser != null){
                    Intent myIntent = new Intent(MainActivity.this, CreatePostActivity.class);
                    myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    myIntent.putExtra("idUser", mIdUser); //Optional parameters
                    // MainActivity.this.startActivity(myIntent);
                    startActivity(myIntent);
                    MainActivity.this.finish();
                    overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                    break;
                case R.id.nav_anuncios:
                    getSupportFragmentManager().beginTransaction().
                            remove(getSupportFragmentManager().findFragmentById(R.id.content_frame)).commit();

                    fragment = new MyAdvertisementsFragment();
                    args = new Bundle();
                    args.putInt("idUser", mIdUser);
                    args.putDouble("Latitude", mLatitude);
                    args.putDouble("Longuitude", mLongitud);
                    fragment.setArguments(args);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
                    break;
                case R.id.nav_fav:
                    getSupportFragmentManager().beginTransaction().
                            remove(getSupportFragmentManager().findFragmentById(R.id.content_frame)).commit();

                    fragment = new FavoriteFragment();
                    args = new Bundle();
                    args.putString("idUser", mIdUser.toString());
                    args.putDouble("Latitude", mLatitude);
                    args.putDouble("Longuitude", mLongitud);
                    fragment.setArguments(args);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
                    break;
            }
        }
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

                getSupportFragmentManager().beginTransaction().
                        remove(getSupportFragmentManager().findFragmentById(R.id.content_frame)).commit();
                myIntent = new Intent(MainActivity.this, NewServiceActivity.class);
                myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                myIntent.putExtra("idUser", mIdUser);
                myIntent.putExtra("Latitud", mLatitude);
                myIntent.putExtra("Longuitud", mLongitud);
                myIntent.putExtra("Radius", mRadius);
                MainActivity.this.startActivity(myIntent);
                MainActivity.this.finish();
                //overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                break;
            case R.id.btnComercio:
                /*fragment = new ServiceListFragment();
                args.putString("Type", "Comercio");
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();*/
                break;
            case R.id.btnPromo:
                /*fragment = new ServiceListFragment();
                args.putString("Type", "Promocion");
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();*/
                break;
            case R.id.setUbicacion:

                myIntent = new Intent(MainActivity.this, MainZone.class);
                myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                myIntent.putExtra("idUser", mIdUser);
                myIntent.putExtra("Latitud", mLatitude);
                myIntent.putExtra("Longuitud", mLongitud);
                myIntent.putExtra("Radius", mRadius);
                getSupportFragmentManager().beginTransaction().
                        remove(getSupportFragmentManager().findFragmentById(R.id.content_frame)).commit();
                MainActivity.this.startActivity(myIntent);
                MainActivity.this.finish();
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
