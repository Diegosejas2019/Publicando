package com.code.publicando.publicando.activitys;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.code.publicando.publicando.R;
import com.code.publicando.publicando.clases.JSONParser;
import com.code.publicando.publicando.clases.PreferenceManager;
import com.code.publicando.publicando.clases.Url;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;
import static com.code.publicando.publicando.activitys.LoginActivity.MY_PREFS_NAME;


public class LocationActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener ,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

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
    private int RADIUS_DEFAULT = 2000;
    private int NEW_RADIUS = 0;
    private Circle mCircle;
    private Integer mIdUser;
    private double mLatitude;
    private double mLongitud;

    JSONParser jParser = new JSONParser();
    Url url = new Url();
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "StatusCode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Bundle b = getIntent().getExtras();
        if(b != null){
            mIdUser = b.getInt("idUser");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Dots_Layout = (LinearLayout) findViewById(R.id.dotsLayout);
        createDots(1);

        fab = findViewById(R.id.fabmap);
        fab.setOnClickListener(this);

        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SaveUbicationTask().execute();
            }
        });


    }

    public class SaveUbicationTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LocationActivity.this);
            pDialog.setMessage("Guardando ubicación...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean flag = false;
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("IdUser", mIdUser.toString()));
            nameValuePairs.add(new BasicNameValuePair("Radius", Integer.toString(RADIUS_DEFAULT)));
            nameValuePairs.add(new BasicNameValuePair("Latitude", Double.toString(mLatitude)));
            nameValuePairs.add(new BasicNameValuePair("Longitude", Double.toString(mLongitud)));

            String Resultado="";

            JSONObject json = jParser.makeHttpRequest(url.getDireccion() + "/api/master/SaveUbication", "POST", nameValuePairs);

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
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("Latitude", String.valueOf(mLatitude));
                editor.putString("Longitud", String.valueOf(mLongitud));
                editor.putString("Radius", String.valueOf(RADIUS_DEFAULT));
                editor.apply();
                new PreferenceManager(LocationActivity.this).clearPreference();
                Intent myIntent = new Intent(LocationActivity.this, GuideActivity.class);
                myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                myIntent.putExtra("idUser", mIdUser); //Optional parameters
                myIntent.putExtra("Latitude", mLatitude); //Optional parameters
                myIntent.putExtra("Longitud", mLongitud); //Optional parameters
                myIntent.putExtra("Radius", RADIUS_DEFAULT); //Optional parameters
                LocationActivity.this.startActivity(myIntent);
            } else {
                Toast.makeText(LocationActivity.this,"Hubo un problema al guardar la ubicación",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            pDialog.dismiss();

            Toast.makeText(LocationActivity.this,"Sin conexión",Toast.LENGTH_LONG).show();
        }
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

    private void createDots(int current_position) {
        if (Dots_Layout != null)
            Dots_Layout.removeAllViews();

        dots = new ImageView[3];

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            if (i == current_position) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots));
            } else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_dots));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.setMargins(4, 0, 4, 0);

            Dots_Layout.addView(dots[i], params);
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

/*        LatLng sevilla = new LatLng(-34.6969607,-58.5372425);
        mMap.addMarker(new MarkerOptions().position(sevilla).title("Hola desde Sevilla!").draggable(true));

        mLatitude = (int) sevilla.latitude;
        mLongitud = (int) sevilla.longitude;

        final CameraPosition camera = new CameraPosition.Builder()
                .target(sevilla)
                .zoom(11.0f)           // limit -> 21
                .bearing(0)         // 0 - 365º
                .tilt(30)           // limit -> 90
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));

        mCircle = mMap.addCircle(new CircleOptions()
                .center(sevilla)
                .radius(RADIUS_DEFAULT)
                .strokeColor(Color.BLUE)
                .fillColor(Color.TRANSPARENT)
                .strokeWidth(5));*/

        SeekBar bar = findViewById(R.id.seekBar);
        bar.setMax(20);
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
                // use progress set map zoom level
                // update map zoom level here
/*                if (progress > NEW_RADIUS) {
                    NEW_RADIUS = NEW_RADIUS + progress;
                    RADIUS_DEFAULT = RADIUS_DEFAULT + 100;
                }
                else{
                    NEW_RADIUS = NEW_RADIUS - progress;
                    RADIUS_DEFAULT = RADIUS_DEFAULT - 100;
                }*/
/*                mCircle = mMap.addCircle(new CircleOptions()
                        .center(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                        .radius(RADIUS_DEFAULT)
                        .strokeColor(Color.BLUE)
                        .fillColor(Color.TRANSPARENT)
                        .strokeWidth(5));*/
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
        }
    }

    private void handleNewLocation(Location location) {

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        mLatitude =  currentLatitude;
        mLongitud =  currentLongitude;

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        mLastLocation = location;
        //mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        mMap.addMarker(options);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11.0f));

        mCircle = mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(RADIUS_DEFAULT)
                .strokeColor(Color.RED)
                .fillColor(0x220000FF)
                .strokeWidth(5));

    }
    @Override
    public void onConnectionSuspended(int i) {}


    @Override
    public void onLocationChanged(Location location)
    {
        /* mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,13.0f));
       mMap.addCircle(new CircleOptions()
                .center(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                .radius(RADIUS_DEFAULT)
                .strokeColor(Color.RED)
                .strokeWidth(5)
                .fillColor(0x220000FF));*/
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(LocationActivity.this,
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

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
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

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
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
        if (!this.checkGpsEnable())
        {
            showInfoAlert();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(LocationActivity.this, ChooseZoneActivity.class);
        myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        //myIntent.putExtra("key", IDuser); //Optional parameters
        LocationActivity.this.startActivity(myIntent);
    }
}
