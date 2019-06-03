package com.code.publicando.publicando.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.code.publicando.publicando.R;
import com.code.publicando.publicando.activitys.MainActivity;
import com.code.publicando.publicando.clases.JSONParser;
import com.code.publicando.publicando.clases.Post;
import com.code.publicando.publicando.clases.Product;
import com.code.publicando.publicando.clases.ProductAdapter;
import com.code.publicando.publicando.clases.Url;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAdvertisementsFragment extends Fragment {

    List<Product> productList;

    RecyclerView recyclerView;
    JSONArray jsonarray;
    JSONObject jsonobject;
    JSONParser jParser = new JSONParser();
    private ProgressDialog pDialog;
    private Integer idUser;
    private Double Latitude;
    private Double Longuitude;
    ArrayList<Post> posts;
    private Context context;
    public MyAdvertisementsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = inflater.getContext();
        View view = inflater.inflate(R.layout.fragment_my_advertisements, container, false);

        ((MainActivity) getActivity())
                    .setActionBarTitle("Mis Anuncios");

        Bundle args = getArguments();
        idUser = args.getInt("idUser", 0);
        Latitude = args.getDouble("Latitude", 0);
        Longuitude = args.getDouble("Longuitude", 0);

        try {
            new ObtenerDestacados().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        productList = new ArrayList<>();

        return view;
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
            posts = new ArrayList<Post>();

            List parames = new ArrayList();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("IdUser", String.valueOf(idUser)));
            Url url = new Url();
            JSONObject json = jParser.makeHttpRequest(url.getDireccion()  + "/api/master/GetAllPostByUser/" + idUser, "GET", parames);

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
                    post.setPartido(jsonobject.getString("Partido"));
                    post.setLocalidad(jsonobject.getString("Localidad"));
                    post.setCalle(jsonobject.getString("Calle"));
                    post.setAltura(jsonobject.getInt("Favorite"));
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
                productList.add(
                        new Product(
                                /*posts.get(i).IdPost,
                                posts.get(i).TypeWork,
                                posts.get(i).Description,
                                posts.get(i).ImageUrl,1,"",
                                posts.get(i).Localidad,
                                posts.get(i).Altura,
                                posts.get(i).Calle,
                                posts.get(i).Partido,
                                Double.parseDouble(posts.get(i).Latitude),
                                Double.parseDouble(posts.get(i).Longitude)));*/
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
            ProductAdapter adapter = new ProductAdapter(context, productList,idUser,Latitude,Longuitude,"Anuncios");

            recyclerView.setAdapter(adapter);
        }

        @Override
        protected void onCancelled() {
            pDialog.dismiss();
            //mAuthTask = null;
            Toast.makeText(context,"Sin conexión",Toast.LENGTH_LONG).show();
        }
    }
}
