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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import com.code.publicando.publicando.R;
import com.code.publicando.publicando.clases.JSONParser;
import com.code.publicando.publicando.clases.Post;
import com.code.publicando.publicando.clases.Product;
import com.code.publicando.publicando.clases.ProductAdapter;
import com.code.publicando.publicando.clases.Url;
import com.code.publicando.publicando.fragments.FavoriteFragment;
import com.code.publicando.publicando.fragments.MyAdvertisementsFragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;
import static com.code.publicando.publicando.activitys.LoginActivity.MY_PREFS_NAME;

public class ListMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    List<Product> productList;

    //the recyclerview
    RecyclerView recyclerView;
    JSONArray jsonarray;
    JSONObject jsonobject;
    JSONParser jParser = new JSONParser();
    private ProgressDialog pDialog;
    private String Type;
    private String Detail;
    private Integer mIdUser;
    ArrayList<Post> posts;
    private Context context;
    private Double Latitude;
    private Double Longuitude;
    private Button btnZona;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



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
            Type = b.getString("Type");
            Detail = b.getString("Detail");
            mIdUser = b.getInt("idUser");
            Latitude = b.getDouble("Latitude");
            Longuitude = b.getDouble("Longuitude");
        }

        getSupportActionBar().setTitle(Detail);

        new ObtenerDestacados().execute();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListMain.this));

        productList = new ArrayList<>();

        ScrollView mainScrollView = (ScrollView)findViewById(R.id.scrolllist);
        mainScrollView.fullScroll(ScrollView.FOCUS_UP);

/*        btnZona = findViewById(R.id.btnZona);
        btnZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Toast.makeText(ListMain.this,"Pendiente", Toast.LENGTH_LONG).show();
            }
        });*/
    }

    public class ObtenerDestacados extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListMain.this );
            pDialog.setMessage("Obteniendo publicaciones...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            posts = new ArrayList<Post>();

            List parames = new ArrayList();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("TypeWork", Type));
            nameValuePairs.add(new BasicNameValuePair("WorkDetail", Detail));
            nameValuePairs.add(new BasicNameValuePair("IdUser", String.valueOf(mIdUser)));
            Url url = new Url();
            JSONObject json = jParser.makeHttpRequest(url.getDireccion()  + "/api/master/GetAllPost/", "POST", nameValuePairs);

            try {

                jsonarray = json.getJSONArray("Imagenes");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);

                    Post post = new Post();

                    post.setIdPost(jsonobject.optInt("IdPost"));
                    post.setIdUser(jsonobject.optInt("IdUser"));
                    post.setCelular(jsonobject.getString("Celular"));
                    post.setDescription(jsonobject.getString("Description"));
                    post.setImageUrl(jsonobject.getString("ImageUrl"));
                    post.setLatitude(jsonobject.getString("Latitude"));
                    post.setLongitude(jsonobject.getString("Longitude"));
                    post.setPhone(jsonobject.getString("Phone"));
                    post.setTypeWork(jsonobject.getString("TypeWork"));
                    post.setRadius(jsonobject.optInt("Radius"));
                    post.setWorkDetail(jsonobject.getString("WorkDetail"));
                    post.setFavorite(jsonobject.getInt("Favorite"));
                    post.setPartido(jsonobject.getString("Partido"));
                    post.setLocalidad(jsonobject.getString("Localidad"));
                    post.setAltura(jsonobject.getInt("Altura"));
                    post.setCalle(jsonobject.getString("Calle"));
                    posts.add(post);
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
            for (int i = 0; i < posts.size(); i++) {
                if (posts.get(i).Favorite == 1){
                    productList.add(
                            new Product(
                                    posts.get(i).IdPost,
                                    posts.get(i).WorkDetail,
                                    posts.get(i).Description,
                                    posts.get(i).ImageUrl,
                                    posts.get(i).Favorite,
                                    posts.get(i).TypeWork,
                                    posts.get(i).Localidad,
                                    posts.get(i).Altura,
                                    posts.get(i).Calle,
                                    posts.get(i).Partido,
                                    Double.parseDouble(posts.get(i).Latitude),
                                    Double.parseDouble(posts.get(i).Longitude)));
                }
                else
                {
                    productList.add(
                            new Product(
                                    posts.get(i).IdPost,
                                    posts.get(i).WorkDetail,
                                    posts.get(i).Description,
                                    posts.get(i).ImageUrl,
                                    0,
                                    posts.get(i).TypeWork,
                                    posts.get(i).Localidad,
                                    posts.get(i).Altura,
                                    posts.get(i).Calle,
                                    posts.get(i).Partido,
                                    Double.parseDouble(posts.get(i).Latitude),
                                    Double.parseDouble(posts.get(i).Longitude)));
                }
            }
            ProductAdapter adapter = new ProductAdapter(ListMain.this   , productList,mIdUser,Latitude,Longuitude);

            recyclerView.setAdapter(adapter);
        }

        @Override
        protected void onCancelled() {
            pDialog.dismiss();
            //mAuthTask = null;
            Toast.makeText(ListMain.this,"Sin conexión",Toast.LENGTH_LONG).show();
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
            startActivity(new Intent(ListMain.this, HomeActivity.class));
            finish();
        }
        if (id == R.id.registrarse) {
            getSupportFragmentManager().beginTransaction().
                    remove(getSupportFragmentManager().findFragmentById(R.id.content_frame)).commit();
            Intent myIntent = new Intent(ListMain.this, HomeActivity.class);
            myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            ListMain.this.startActivity(myIntent);
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

            Intent myIntent = new Intent(ListMain.this, HomeActivity.class);
            myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            ListMain.this.startActivity(myIntent);
        }
        else{
            switch (id)
            {
                case R.id.nav_publicar:
                    Intent myIntent = new Intent(ListMain.this, CreatePostActivity.class);
                    myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    myIntent.putExtra("idUser", mIdUser); //Optional parameters
                    ListMain.this.startActivity(myIntent);
                    break;
                case R.id.nav_anuncios:
                    myIntent = new Intent(ListMain.this, MainActivity.class);
                    myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    myIntent.putExtra("idUser", mIdUser); //Optional parameters
                    myIntent.putExtra("Frame", "Anuncios"); //Optional parameters
                    ListMain.this.startActivity(myIntent);
                    break;
                case R.id.nav_fav:
                    myIntent = new Intent(ListMain.this, MainActivity.class);
                    myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    myIntent.putExtra("idUser", mIdUser); //Optional parameters
                    myIntent.putExtra("Frame", "Favorite"); //Optional parameters
                    ListMain.this.startActivity(myIntent);
                    break;
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(ListMain.this, NewServiceActivity.class);
        myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        myIntent.putExtra("idUser", mIdUser);
        ListMain.this.startActivity(myIntent);
    }
}
