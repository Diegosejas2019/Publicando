package com.code.publicando.publicando.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.code.publicando.publicando.R;
import com.code.publicando.publicando.clases.JSONParser;
import com.code.publicando.publicando.clases.PreferenceManager;
import com.code.publicando.publicando.clases.Ubicacion;
import com.code.publicando.publicando.clases.Url;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;
import static com.code.publicando.publicando.activitys.LoginActivity.MY_PREFS_NAME;

public class SetZoneActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout Dots_Layout;
    private ImageView[] dots;
    private Button next;
    private double mLatitude;
    private double mLongitud;
    private Integer mIdUser;
    JSONParser jParser = new JSONParser();
    Url url = new Url();
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "StatusCode";
    private int RADIUS_DEFAULT = 2000;
    Ubicacion ubicacion = new Ubicacion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_zone);

        Dots_Layout = (LinearLayout) findViewById(R.id.dotsLayout);
        createDots(1);

        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Provincia");
        List<String> spinnerArray2 =  new ArrayList<String>();
        spinnerArray2.add("Localidad");
        List<String> spinnerArray3 =  new ArrayList<String>();
        spinnerArray3.add("Barrio");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray3);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner sItems = (Spinner) findViewById(R.id.spinner);
        Spinner sItems2 = (Spinner) findViewById(R.id.spinner2);
        Spinner sItems3 = (Spinner) findViewById(R.id.spinner3);
        sItems.setAdapter(adapter);
        sItems2.setAdapter(adapter2);
        sItems3.setAdapter(adapter3);

        next = findViewById(R.id.next);
        next.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        if(b != null){
            mIdUser = b.getInt("idUser");
            //mIdUser = 1;
        }

        //mIdUser = 1;

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Calle y Altura (Opcional)");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                //Toast.makeText(SetZoneActivity.this, place.getName().toString(), Toast.LENGTH_LONG).show();
                LatLng latitude = place.getLatLng();
                mLatitude = latitude.latitude;
                mLongitud = latitude.longitude;
                getAddress(mLatitude,mLongitud);
            }

            @Override
            public void onError(Status status) {

            }
        });
    }

    public void getAddress(double lat, double lng) {
        String currentLocation = "";

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            ubicacion.IdUser = mIdUser;
            ubicacion.Radius = RADIUS_DEFAULT;
            ubicacion.Latitude = String.valueOf(lat);
            ubicacion.Longitude = String.valueOf(lng);
            ubicacion.Provincia = obj.getAdminArea();
            ubicacion.CP = obj.getPostalCode();
            ubicacion.Partido = obj.getSubAdminArea();
            ubicacion.Localidad = obj.getLocality();
            ubicacion.Calle = obj.getThoroughfare();
            ubicacion.Altura = Integer.parseInt(obj.getSubThoroughfare());


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void createDots(int current_position)
    {
        if (Dots_Layout!=null)
            Dots_Layout.removeAllViews();

        dots = new ImageView[3];

        for (int i= 0; i < dots.length; i++)
        {
            dots[i] = new ImageView(this);
            if (i==current_position)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dots));
            }
            else
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.default_dots));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.setMargins(4,0,4,0);

            Dots_Layout.addView(dots[i],params);
        }

    }

    @Override
    public void onClick(View view) {

        new SaveUbicationTask().execute();

/*        Intent myIntent = new Intent(SetZoneActivity.this,
                LocationActivity.class);
        myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        myIntent.putExtra("idUser", mIdUser); //Optional parameters
        myIntent.putExtra("Latitude", Double.toString(mLatitude) ); //Optional parameters
        myIntent.putExtra("Longitud", Double.toString(mLongitud)); //Optional parameters
        startActivity(myIntent);
        SetZoneActivity.this.finish();
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);*/
    }

    public class SaveUbicationTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SetZoneActivity.this);
            pDialog.setMessage("Guardando ubicación...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean flag = false;
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
            nameValuePairs.add(new BasicNameValuePair("IdUser", Integer.toString(ubicacion.getIdUser())));
            nameValuePairs.add(new BasicNameValuePair("Radius", Integer.toString(ubicacion.getRadius())));
            nameValuePairs.add(new BasicNameValuePair("Latitude", ubicacion.getLatitude()));
            nameValuePairs.add(new BasicNameValuePair("Longitude", ubicacion.getLongitude()));
            nameValuePairs.add(new BasicNameValuePair("Provincia", ubicacion.getProvincia()));
            nameValuePairs.add(new BasicNameValuePair("CP", ubicacion.getCP()));
            nameValuePairs.add(new BasicNameValuePair("Partido", ubicacion.getPartido()));
            nameValuePairs.add(new BasicNameValuePair("Localidad", ubicacion.getLocalidad()));
            nameValuePairs.add(new BasicNameValuePair("Calle", ubicacion.getCalle()));
            nameValuePairs.add(new BasicNameValuePair("Altura", Integer.toString(ubicacion.getAltura())));

            String Resultado="";

            JSONObject json = jParser.makeHttpRequest(url.getDireccion() + "/api/master/AddUbication", "POST", nameValuePairs);

            try {
                if (json != null){
                    int success = json.getInt(TAG_SUCCESS);
                    if (success == 200){
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
            if (success) {
                new PreferenceManager(SetZoneActivity.this).clearPreference();
                Intent myIntent = new Intent(SetZoneActivity.this, LocationActivity.class);
                myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                myIntent.putExtra("idUser", mIdUser); //Optional parameters
                myIntent.putExtra("Latitude", mLatitude); //Optional parameters
                myIntent.putExtra("Longitud", mLongitud); //Optional parameters
                myIntent.putExtra("Radius", RADIUS_DEFAULT); //Optional parameters
                SetZoneActivity.this.startActivity(myIntent);
            } else {
                Toast.makeText(SetZoneActivity.this,"Hubo un problema al guardar la ubicación",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            pDialog.dismiss();

            Toast.makeText(SetZoneActivity.this,"Sin conexión",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(SetZoneActivity.this, ChooseZoneActivity.class);
        myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        //myIntent.putExtra("key", IDuser); //Optional parameterse
        SetZoneActivity.this.startActivity(myIntent);
    }
}
