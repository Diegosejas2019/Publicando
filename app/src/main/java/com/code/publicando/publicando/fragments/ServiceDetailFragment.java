package com.code.publicando.publicando.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.code.publicando.publicando.R;
import com.code.publicando.publicando.activitys.MainActivity;
import com.code.publicando.publicando.clases.JSONParser;
import com.code.publicando.publicando.clases.Post;
import com.code.publicando.publicando.clases.Product;
import com.code.publicando.publicando.clases.ProductAdapter;
import com.code.publicando.publicando.clases.Url;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceDetailFragment extends Fragment implements OnMapReadyCallback {

    List<Product> productList;

    //the recyclerview
    RecyclerView recyclerView;
    JSONArray jsonarray;
    JSONObject jsonobject;
    JSONParser jParser = new JSONParser();
    private ProgressDialog pDialog;
    private String Type;
    private String IdPost;
    private Integer IdUser;
    private TextView txt;
    private Post posts = new Post();
    private Context context;
    private  View view;
    RatingBar mRatingBar;
    TextView mRatingScale;
    private GoogleMap mMap;
    private MapView mapView;
    public ServiceDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = inflater.getContext();
        view = inflater.inflate(R.layout.fragment_service_detail, container, false);
        ((MainActivity) getActivity())
                .setActionBarTitle("Mis Anuncios");

        Bundle args = getArguments();
        IdPost = args.getString("idPost", null);
        IdUser = args.getInt("idUser", 0);

        ((MainActivity) getActivity())
                .setActionBarTitle("Detalle");

        new ObtenerDestacados().execute();



        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.mapFragment);
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
            pDialog = new ProgressDialog(context);
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

            txt = view.findViewById(R.id.description);
            txt.setText(posts.getDescription());
            TextView textView2 = view.findViewById(R.id.textView2);
            textView2.setText("Celular : " + posts.getCelular());
            TextView textView3 = view.findViewById(R.id.textView3);
            textView3.setText("Telefono : " + posts.getPhone());
/*            TextView textView4 = view.findViewById(R.id.textView4);
            textView4.setText(posts.getDescription());*/
            mRatingScale = view.findViewById(R.id.tvRatingScale);
            mRatingBar = (RatingBar) view.findViewById(R.id.ratingBar);

            mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    mRatingScale.setText(String.valueOf(v));
                    switch ((int) ratingBar.getRating()) {
                        case 1:
                            mRatingScale.setText("Malo");
                            break;
                        case 2:
                            mRatingScale.setText("Regular");
                            break;
                        case 3:
                            mRatingScale.setText("Bueno");
                            break;
                        case 4:
                            mRatingScale.setText("Muy Bueno");
                            break;
                        case 5:
                            mRatingScale.setText("Excelente");
                            break;
                        default:
                            mRatingScale.setText("");
                    }
                }
            });

            ImageView imagenPost = view.findViewById(R.id.imagenPost);
            Url url = new Url();
            Picasso.with(context)
                    .load(url.getDireccion() + "/Imagenes/" + posts.getImageUrl().substring((posts.getImageUrl().length()-6)).replaceAll("\\\\", ""))
                    .resize(1400, 850)
                    .into(imagenPost);

/*                if (posts.Favorite == 1){
                    productList.add(
                            new Product(
                                    posts.IdPost,
                                    posts.TypeWork,
                                    posts.Description,
                                    posts.ImageUrl,
                                    posts.Favorite));
                }
                else
                {
                    productList.add(
                            new Product(
                                    posts.IdPost,
                                    posts.TypeWork,
                                    posts.Description,
                                    posts.ImageUrl,
                                    0));
                }

            ProductAdapter adapter = new ProductAdapter(context, productList,IdUser);

            recyclerView.setAdapter(adapter);*/
        }

        @Override
        protected void onCancelled() {
            pDialog.dismiss();
            //mAuthTask = null;
            Toast.makeText(context,"Sin conexión",Toast.LENGTH_LONG).show();
        }
    }
}
