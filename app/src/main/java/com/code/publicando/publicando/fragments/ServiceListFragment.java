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

import com.code.publicando.publicando.clases.JSONParser;
import com.code.publicando.publicando.clases.Post;
import com.code.publicando.publicando.clases.Product;
import com.code.publicando.publicando.clases.ProductAdapter;
import com.code.publicando.publicando.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceListFragment extends Fragment {

    List<Product> productList;

    //the recyclerview
    RecyclerView recyclerView;

    JSONArray jsonarray;
    JSONObject jsonobject;
    private String url = "http://10.0.2.2/api/login/";
    JSONParser jParser = new JSONParser();
    private ProgressDialog pDialog;
    ArrayList<Post> posts;
    private Context context;
    public ServiceListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        context = inflater.getContext();
        View view = inflater.inflate(R.layout.fragment_service_list, container, false);

/*        Button btnZona = view.findViewById(R.id.btnZona);
        btnZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new ChooseZoneFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
                transaction.replace(R.id.content_frame, newFragment);
                transaction.addToBackStack(null);

// Commit the transaction
                transaction.commit();
            }
        });*/

        new ObtenerDestacados().execute();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        //initializing the productlist
        productList = new ArrayList<>();


        //adding some items to our list


/*
        productList.add(
                new Product(
                        1,
                        "Resto Modelo 1",
                        "Parrilla",
                        R.drawable.comidas));

        productList.add(
                new Product(
                        1,
                        "Promos Modelo 1",
                        "Increibles descuentos",
                        R.drawable.promos));

        productList.add(
                new Product(
                        1,
                        "Comercio modelo 1",
                        "10 Sucursales en zona oeste",
                        R.drawable.comercio));

        productList.add(
                new Product(
                        1,
                        "Resto Modelo 1",
                        "Parrilla",
                        R.drawable.comidas));

        productList.add(
                new Product(
                        1,
                        "Promos Modelo 1",
                        "Increibles descuentos",
                        R.drawable.promos));*/

        return view;
    }

    public void onClickZona() {
        Toast.makeText(context,"algo",Toast.LENGTH_LONG).show();
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

            JSONObject json = jParser.makeHttpRequest(url  + "/GetAllPost", "GET", parames);

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
                                posts.get(i).IdPost,
                                posts.get(i).TypeWork,
                                posts.get(i).Description,
                                posts.get(i).ImageUrl));
            }
            ProductAdapter adapter = new ProductAdapter(context, productList);

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
