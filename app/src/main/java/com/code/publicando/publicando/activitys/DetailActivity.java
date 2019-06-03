package com.code.publicando.publicando.activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.code.publicando.publicando.R;
import com.code.publicando.publicando.clases.JSONParser;
import com.code.publicando.publicando.clases.Post;
import com.code.publicando.publicando.clases.Product;
import com.code.publicando.publicando.clases.Ubicacion;
import com.code.publicando.publicando.clases.Url;
import com.code.publicando.publicando.fragments.FavoriteFragment;
import com.code.publicando.publicando.fragments.MyAdvertisementsFragment;
import com.code.publicando.publicando.fragments.ServiceDetailFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;
import static com.code.publicando.publicando.activitys.LoginActivity.MY_PREFS_NAME;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener {

    List<Product> productList;
    private static final String TAG_SUCCESS = "StatusCode";
    //the recyclerview
    RecyclerView recyclerView;
    JSONArray jsonarray;
    JSONObject jsonobject;
    JSONParser jParser = new JSONParser();
    private ProgressDialog pDialog;
    private String Type;
    private String IdPost;
    private Integer IdUser;
    private String Detail;
    private String View = "";
    private TextView txt;
    private Double Latitude;
    private Double Longuitude;
    private Post posts = new Post();
    private Context context;
    private View view;
    RatingBar mRatingBar;
    TextView mRatingScale;
    private GoogleMap mMap;
    private MapView mapView;
    Ubicacion ubicacion = new Ubicacion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Publicando");

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null){
            IdPost = b.getString("idPost","");
            IdUser = b.getInt("idUser",0);
            Detail = b.getString("Detail","");
            Type = b.getString("Type","");
            View = b.getString("View","");
            Latitude = b.getDouble("Latitude",0);
            Longuitude = b.getDouble("Longuitude",0);
        }

         Boolean flag = null;
        try {
            flag = new UserUbicationTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            flag = new ObtenerDestacados().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        mapView = (MapView) findViewById(R.id.mapFragment);
        if (mapView  != null)
        {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sevilla = new LatLng(Double.parseDouble(posts.getLatitude()),Double.parseDouble(posts.getLongitude()));
        mMap.addMarker(new MarkerOptions().position(sevilla).title("Hola desde Sevilla!").draggable(true));

        final CameraPosition camera = new CameraPosition.Builder()
                .target(sevilla)
                .zoom(11.0f)           // limit -> 21
                .bearing(0)         // 0 - 365º
                .tilt(30)           // limit -> 90
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
    }

    public class ObtenerDestacados extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailActivity.this);
            pDialog.setMessage("Obteniendo publicación...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //posts = new ArrayList<Post>();

            List parames = new ArrayList();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("IdPost", IdPost));
            Url url = new Url();
            JSONObject json = jParser.makeHttpRequest(url.getDireccion()  + "/api/master/GetPostById/", "POST", nameValuePairs);

            try {
                if(json != null){

                    posts.setIdPost(json.optInt("IdPost"));
                    posts.setIdUser(json.optInt("IdUser"));
                    posts.setCelular(json.getString("Celular"));
                    posts.setDescription(json.getString("Description"));
                    posts.setImageUrl(json.getString("ImageUrl"));
                    posts.setLatitude(json.getString("Latitude"));
                    posts.setLongitude(json.getString("Longitude"));
                    posts.setPhone(json.getString("Phone"));
                    posts.setTypeWork(json.getString("TypeWork"));
                    posts.setRadius(json.optInt("Radius"));
                    posts.setWorkDetail(json.getString("WorkDetail"));
                    posts.setFavorite(json.getInt("Favorite"));
/*                    ubicacion.Calle = json.getString("Calle");
                    ubicacion.Altura = json.getInt("Altura");
                    ubicacion.Localidad = json.getString("Localidad");
                    ubicacion.Partido = json.getString("Partido");*/
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            pDialog.dismiss();

            txt = findViewById(R.id.description);
            txt.setText(posts.getDescription());
            TextView calle = findViewById(R.id.Calle);
            calle.setText(ubicacion.getCalle() + ' ' + String.valueOf(ubicacion.getAltura()));
            TextView Partido = findViewById(R.id.partido);
            Partido.setText(ubicacion.getPartido() + ' ' + ubicacion.getLocalidad());

            ImageView imagenPost = findViewById(R.id.imagenPost);
            Url url = new Url();
            Picasso.with(DetailActivity.this)
                    .load(url.getDireccion() + "/Imagenes/" + posts.getImageUrl().substring((posts.getImageUrl().length()-6)).replaceAll("\\\\", ""))
                    .resize(1400, 850)
                    .into(imagenPost);

        }

        @Override
        protected void onCancelled() {
            pDialog.dismiss();
            //mAuthTask = null;
            Toast.makeText(DetailActivity.this,"Sin conexión",Toast.LENGTH_LONG).show();
        }
    }

    public class UserUbicationTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        UserUbicationTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean flag = false;
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("idPost", String.valueOf(IdPost)));

            String Resultado="";
            Url url = new Url();
            JSONObject json = jParser.makeHttpRequest(url.getDireccion() + "/api/master/GetUbicationPost", "POST", nameValuePairs);

            try {
                if (json != null){
                    int success = json.getInt(TAG_SUCCESS);
                    if (success == 200){
                        ubicacion.Calle = json.getString("Calle");
                        ubicacion.Altura = json.getInt("Altura");
                        ubicacion.Localidad = json.getString("Localidad");
                        ubicacion.Partido = json.getString("Partido");
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

        }

        @Override
        protected void onCancelled() {
            pDialog.dismiss();
            //mAuthTask = null;
            Toast.makeText(DetailActivity.this,"Sin conexión",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem registrar = menu.findItem(R.id.registrarse);
        MenuItem cerrar = menu.findItem(R.id.cerrarSesion);
        if (IdUser != null)
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
            startActivity(new Intent(DetailActivity.this, HomeActivity.class));
            finish();
        }
        if (id == R.id.registrarse) {
            getSupportFragmentManager().beginTransaction().
                    remove(getSupportFragmentManager().findFragmentById(R.id.content_frame)).commit();
            Intent myIntent = new Intent(DetailActivity.this, HomeActivity.class);
            myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            DetailActivity.this.startActivity(myIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean fragmentTransaction = false;
        Fragment fragment = null;
        Bundle args;
        int id = item.getItemId();

        if(IdUser == null)
        {

            getSupportFragmentManager().beginTransaction().
                    remove(getSupportFragmentManager().findFragmentById(R.id.content_frame)).commit();

            Intent myIntent = new Intent(DetailActivity.this, HomeActivity.class);
            myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            DetailActivity.this.startActivity(myIntent);
        }
        else{
            switch (id)
            {
                case R.id.nav_publicar:
                    Intent myIntent = new Intent(DetailActivity.this, CreatePostActivity.class);
                    myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    myIntent.putExtra("idUser", IdUser); //Optional parameters
                    DetailActivity.this.startActivity(myIntent);
                    break;
                case R.id.nav_anuncios:
                    myIntent = new Intent(DetailActivity.this, MainActivity.class);
                    myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    myIntent.putExtra("idUser", IdUser); //Optional parameters
                    myIntent.putExtra("Frame", "Anuncios"); //Optional parameters
                    DetailActivity.this.startActivity(myIntent);
                    break;
                case R.id.nav_fav:
                    myIntent = new Intent(DetailActivity.this, MainActivity.class);
                    myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    myIntent.putExtra("idUser", IdUser); //Optional parameters
                    myIntent.putExtra("Frame", "Favorite"); //Optional parameters
                    DetailActivity.this.startActivity(myIntent);
                    break;
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(!View.isEmpty())
        {
            if(View.equals("Favoritos"))
            {
                Intent myIntent = new Intent(DetailActivity.this, MainActivity.class);
                myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                myIntent.putExtra("Type", Type);
                myIntent.putExtra("Detail", Detail);
                myIntent.putExtra("idUser", IdUser);
                myIntent.putExtra("Frame", "Favorite");
                myIntent.putExtra("Latitude",Double.parseDouble(posts.getLatitude()));
                myIntent.putExtra("Longuitude",Double.parseDouble(posts.getLongitude()));
                DetailActivity.this.startActivity(myIntent);
            }
            if(View.equals("Anuncios"))
            {
                Intent myIntent = new Intent(DetailActivity.this, MainActivity.class);
                myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                myIntent.putExtra("Type", Type);
                myIntent.putExtra("Detail", Detail);
                myIntent.putExtra("idUser", IdUser);
                myIntent.putExtra("Frame", "Anuncios");
                myIntent.putExtra("Latitude",Double.parseDouble(posts.getLatitude()));
                myIntent.putExtra("Longuitude",Double.parseDouble(posts.getLongitude()));
                DetailActivity.this.startActivity(myIntent);
            }
        }
        else{
            Intent myIntent = new Intent(DetailActivity.this, ListMain.class);
            myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            myIntent.putExtra("Type", Type);
            myIntent.putExtra("Detail", Detail);
            myIntent.putExtra("idUser", IdUser);
            myIntent.putExtra("Latitude", Latitude);
            myIntent.putExtra("Longuitude", Longuitude);
            DetailActivity.this.startActivity(myIntent);
        }
    }
}
