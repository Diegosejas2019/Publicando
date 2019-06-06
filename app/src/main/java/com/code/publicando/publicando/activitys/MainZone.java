package com.code.publicando.publicando.activitys;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.code.publicando.publicando.R;
import com.code.publicando.publicando.clases.BitmapHelper;
import com.code.publicando.publicando.clases.JSONParser;
import com.code.publicando.publicando.clases.PreferenceManager;
import com.code.publicando.publicando.clases.Ubicacion;
import com.code.publicando.publicando.clases.Url;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;

public class MainZone extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener ,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    public static final int REQUEST_PERMISSION = 200;
    private GoogleMap mMap;
    private LinearLayout Dots_Layout;
    private ImageView[] dots;
    private Geocoder geocoder;
    private Address address;

    private SupportMapFragment mapFrag;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private Button next;
    private FloatingActionButton fab;
    private LatLng latLng;
    private int RADIUS_DEFAULT = 1000;
    private Circle mCircle;

    private String mType;
    private String mAuto;
    private Bitmap mBitmap;
    private Integer mIdUser;
    private double mLatitude;
    private double mLongitude;
    Ubicacion ubicacion = new Ubicacion();
    Gson gson = new Gson();

    JSONParser jParser = new JSONParser();
    private ProgressDialog pDialog;

    private static final String TAG_SUCCESS = "StatusCode";
    Url url = new Url();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_zone);

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Cambiar Ubicación");
        }
        catch (Exception e )
        {
            Log.e(e.getMessage(),"TEST");
        }

        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null){
            mIdUser = b.getInt("idUser");
            mLatitude = b.getDouble("Latitud");
            mLongitude = b.getDouble("Longuitud");
            RADIUS_DEFAULT = b.getInt("Radius");
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Dots_Layout = (LinearLayout) findViewById(R.id.dotsLayout);
        createDots(3);

        fab = findViewById(R.id.fabmap);
        fab.setOnClickListener(this);

        next = findViewById(R.id.next);
        next.setOnClickListener(this);

        Button next = findViewById(R.id.next);
        next.setOnClickListener(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Calle y Altura (Opcional)");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                //Toast.makeText(SetZoneActivity.this, place.getName().toString(), Toast.LENGTH_LONG).show();
                mMap.clear();
                LatLng latitude = place.getLatLng();
                mLatitude = latitude.latitude;
                mLongitude = latitude.longitude;
                getAddress(mLatitude,mLongitude);
                LatLng ubicacion = new LatLng(mLatitude,mLongitude);
                mMap.addMarker(new MarkerOptions().position(ubicacion).title("").draggable(true));

                final CameraPosition camera = new CameraPosition.Builder()
                        .target(ubicacion)
                        .zoom(11.0f)           // limit -> 21
                        .bearing(0)         // 0 - 365º
                        .tilt(30)           // limit -> 90
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));

                mCircle = mMap.addCircle(new CircleOptions()
                        .center(ubicacion)
                        .radius(RADIUS_DEFAULT)
                        .strokeColor(Color.BLUE)
                        .fillColor(Color.TRANSPARENT)
                        .strokeWidth(5));
            }

            @Override
            public void onError(Status status) {

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        //this.checkGpsEnable();
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
        }
    }

    private boolean checkGpsEnable() {
        try {
            int gpsSignal = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);

            if (gpsSignal == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void createDots(int current_position)
    {
        if (Dots_Layout!=null)
            Dots_Layout.removeAllViews();

        dots = new ImageView[6];

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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);

            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

        }

        if (mLatitude != 0 && mLongitude != 0)
        {
            //LatLng sevilla = new LatLng(Double.parseDouble(Latitude),Double.parseDouble(Longitud));
            LatLng ubicacion = new LatLng(mLatitude,mLongitude);
            mMap.addMarker(new MarkerOptions().position(ubicacion).title("").draggable(true));

            mLatitude = ubicacion.latitude;
            mLongitude = ubicacion.longitude;
            final CameraPosition camera = new CameraPosition.Builder()
                    .target(ubicacion)
                    .zoom(11.0f)           // limit -> 21
                    .bearing(0)         // 0 - 365º
                    .tilt(30)           // limit -> 90
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));

            mCircle = mMap.addCircle(new CircleOptions()
                    .center(ubicacion)
                    .radius(RADIUS_DEFAULT)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.TRANSPARENT)
                    .strokeWidth(5));
            getAddress(mLatitude,mLongitude);
        }

        SeekBar bar = findViewById(R.id.seekBar);
        bar.setMax((int) mMap.getMaxZoomLevel());
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // do nothing
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // do nothing
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                //RADIUS_DEFAULT = RADIUS_DEFAULT + progress + 100;
                switch (progress){
                    case 1:
                        RADIUS_DEFAULT = 2300;
                        break;
                    case 2:
                        RADIUS_DEFAULT = 2600;
                        break;
                    case 3:
                        RADIUS_DEFAULT = 2900;
                        break;
                    case 4:
                        RADIUS_DEFAULT = 3200;
                        break;
                    case 5:
                        RADIUS_DEFAULT = 3500;
                        break;
                    case 6:
                        RADIUS_DEFAULT = 3800;
                        break;
                    case 7:
                        RADIUS_DEFAULT = 4100;
                        break;
                    case 8:
                        RADIUS_DEFAULT = 4400;
                        break;
                    case 9:
                        RADIUS_DEFAULT = 4700;
                        break;
                    case 10:
                        RADIUS_DEFAULT = 5000;
                        break;
                    case 11:
                        RADIUS_DEFAULT = 5300;
                        break;
                    case 12:
                        RADIUS_DEFAULT = 5600;
                        break;
                    case 13:
                        RADIUS_DEFAULT = 5900;
                        break;
                    case 14:
                        RADIUS_DEFAULT = 6200;
                        break;
                    case 15:
                        RADIUS_DEFAULT = 6500;
                        break;
                    case 16:
                        RADIUS_DEFAULT = 6800;
                        break;
                    case 17:
                        RADIUS_DEFAULT = 7100;
                        break;
                    case 18:
                        RADIUS_DEFAULT = 7400;
                        break;
                    case 19:
                        RADIUS_DEFAULT = 7700;
                        break;
                    case 20:
                        RADIUS_DEFAULT = 8000;
                        break;
                }
                mCircle.setRadius(RADIUS_DEFAULT);
            }
        });
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            handleNewLocation(location);
            getAddress(mLatitude,mLongitude);
        }
    }

    public void getAddress(double lat, double lng) {

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

    private void handleNewLocation(Location location) {

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();



        mLatitude = currentLatitude;
        mLongitude = currentLongitude;

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        mLastLocation = location;
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        mMap.addMarker(options);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0f));

        mCircle = mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(RADIUS_DEFAULT)
                .strokeColor(Color.BLUE)
                .fillColor(Color.TRANSPARENT)
                .strokeWidth(5));

    }
    @Override
    public void onConnectionSuspended(int i) {}


    @Override
    public void onLocationChanged(Location location)
    {
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainZone.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }

                } else {

                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void showInfoAlert()
    {
        new AlertDialog.Builder(this)
                .setTitle("Señal GPS")
                .setMessage("Tu GPS no esta activado, debes activarlo para continuar")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel",null )
                .show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.fabmap:
                if (!this.checkGpsEnable())
                {
                    showInfoAlert();
                }
                break;
            case R.id.next:
                /*Intent myIntent = new Intent(MainZone.this, MainActivity.class);
                Bundle b = new Bundle();

                Gson gson = new Gson();
                String Ubicacion = gson.toJson(ubicacion);
                myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);

                myIntent.putExtra("idUser", mIdUser);
                myIntent.putExtra("Ubicacion", Ubicacion);

                MainZone.this.startActivity(myIntent);
                MainZone.this.finish();
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);*/
                new SaveUbicationTask().execute();
                break;
        }
    }

    public class SaveUbicationTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainZone.this);
            pDialog.setMessage("Guardando ubicación...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean flag = false;
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
            nameValuePairs.add(new BasicNameValuePair("IdUser", Integer.toString(mIdUser)));
            nameValuePairs.add(new BasicNameValuePair("Radius", Integer.toString(RADIUS_DEFAULT)));
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
                new PreferenceManager(MainZone.this).clearPreference();
                Intent myIntent = new Intent(MainZone.this, MainActivity.class);
                myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                myIntent.putExtra("idUser", mIdUser); //Optional parameters
                String Ubicacion = gson.toJson(ubicacion);
                myIntent.putExtra("Ubicacion", Ubicacion);
                MainZone.this.startActivity(myIntent);
            } else {
                Toast.makeText(MainZone.this,"Hubo un problema al guardar la ubicación",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            pDialog.dismiss();

            Toast.makeText(MainZone.this,"Sin conexión",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            Intent myIntent = new Intent(MainZone.this, PostUploadPhotoActivity.class);
            myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            myIntent.putExtra("Detail", mAuto);
            myIntent.putExtra("Type", mType);
            myIntent.putExtra("idUser", mIdUser);
            startActivity(myIntent);
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(MainZone.this, MainActivity.class);
        myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        myIntent.putExtra("idUser", mIdUser);
        MainZone.this.startActivity(myIntent);
        finish();
    }
}
