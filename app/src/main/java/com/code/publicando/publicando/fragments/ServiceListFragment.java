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
import android.widget.ImageButton;
import android.widget.Toast;

import com.code.publicando.publicando.activitys.MainActivity;
import com.code.publicando.publicando.clases.JSONParser;
import com.code.publicando.publicando.clases.Post;
import com.code.publicando.publicando.clases.Product;
import com.code.publicando.publicando.clases.ProductAdapter;
import com.code.publicando.publicando.R;
import com.code.publicando.publicando.clases.Url;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
    JSONParser jParser = new JSONParser();
    private ProgressDialog pDialog;
    private String Type;
    private String Detail;
    private Integer IdUser;
    ArrayList<Post> posts;
    private Context context;
    public ServiceListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = inflater.getContext();
        View view = inflater.inflate(R.layout.fragment_service_list, container, false);

/*        Button btnZona = view.findViewById(R.id.btnZona);
        btnZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new ChooseZoneFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.commit();
            }
        });*/

/*        ImageButton btnFavorite = view.findViewById(R.id.favorite);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFavorite.setImageResource(R.drawable.ic_favorite_selected);
            }
        });*/

        Bundle args = getArguments();
        Type = args.getString("Type", null);
        IdUser = args.getInt("IdUser", 0);
        Detail = args.getString("Detail", null);

        ((MainActivity) getActivity())
                .setActionBarTitle(Type);

        new ObtenerDestacados().execute();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        productList = new ArrayList<>();

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
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("TypeWork", Type));
            nameValuePairs.add(new BasicNameValuePair("WorkDetail", Detail));
            nameValuePairs.add(new BasicNameValuePair("IdUser", String.valueOf(IdUser)));
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
                                posts.get(i).Favorite,""));
                }
                else
                    {
                        productList.add(
                                new Product(
                                        posts.get(i).IdPost,
                                        posts.get(i).WorkDetail,
                                        posts.get(i).Description,
                                        posts.get(i).ImageUrl,
                                        0,""));
                    }
            }
            ProductAdapter adapter = new ProductAdapter(context, productList,IdUser);

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
