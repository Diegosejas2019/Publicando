package com.code.publicando.publicando.activitys;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.code.publicando.publicando.R;
import com.code.publicando.publicando.clases.JSONParser;
import com.code.publicando.publicando.clases.Post;
import com.code.publicando.publicando.clases.Product;
import com.code.publicando.publicando.clases.ProductAdapter;
import com.code.publicando.publicando.clases.Url;
import com.code.publicando.publicando.fragments.ServiceListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.code.publicando.publicando.activitys.LoginActivity.MY_PREFS_NAME;


public class MyAdvertisementsActivity extends AppCompatActivity {

    List<Product> productList;

    //the recyclerview
    RecyclerView recyclerView;
    JSONArray jsonarray;
    JSONObject jsonobject;
    JSONParser jParser = new JSONParser();
    private ProgressDialog pDialog;
    ArrayList<Post> posts;
    private Integer mIdUser;
    private static final String TAG_SUCCESS = "StatusCode";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_advertisements);


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Integer restoredText = prefs.getInt("idUser", 0);
        if (restoredText != 0) {
            mIdUser = prefs.getInt("idUser", 0);
        }

        new ObtenerDestacados().execute();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
    }

    public class ObtenerDestacados extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyAdvertisementsActivity.this);
            pDialog.setMessage("Obteniendo publicaciones...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            posts = new ArrayList<Post>();

            List parames = new ArrayList();
            Url url = new Url();
            JSONObject json = jParser.makeHttpRequest(url.getDireccion()  + "/api/master/GetAllPostByUser/" + mIdUser, "GET", parames);

            try {
                if (json != null){

                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 200){
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

                            posts.add(post);
                        }
                }}

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
                productList.add(
                        new Product(
                                posts.get(i).IdPost,
                                posts.get(i).TypeWork,
                                posts.get(i).Description,
                                posts.get(i).ImageUrl,1,""));
            }
            ProductAdapter adapter = new ProductAdapter(MyAdvertisementsActivity.this, productList,1);

            recyclerView.setAdapter(adapter);
        }

        @Override
        protected void onCancelled() {
            pDialog.dismiss();
            //mAuthTask = null;
            Toast.makeText(MyAdvertisementsActivity.this,"Sin conexión",Toast.LENGTH_LONG).show();
        }
    }
}
