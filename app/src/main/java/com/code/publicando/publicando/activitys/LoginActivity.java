package com.code.publicando.publicando.activitys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.code.publicando.publicando.R;
import com.code.publicando.publicando.clases.JSONParser;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    private static final String TAG = "";
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    private View mProgressView;
    private View mLoginFormView;
    private LinearLayout Dots_Layout;
    private ImageView[] dots;

    //private String url = "http://10.0.2.2/api/login/";
    private String url = "http://192.168.1.149/api/login/";
    JSONParser jParser = new JSONParser();
    private ProgressDialog pDialog;
    private EditText mEmailView;
    private EditText mNameView;
    private Integer IDuser;
    private static final String TAG_SUCCESS = "StatusCode";
    private static final String TAG_USER = "UserName";
    //Google Login
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    static final int RC_SIGN_IN = 1;
    private int SIGN_IN = 30;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    //FaceBoook
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Button fbbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //printHashKey(LoginActivity.this);
/*        SharedPreferences spreferences =  getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor spreferencesEditor = spreferences.edit();
        spreferencesEditor.clear();
        spreferencesEditor.commit();*/

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        Integer IDuser = prefs.getInt("idUser", 0);
        if (IDuser != 0) {
            String mLongitud = prefs.getString("Longitud", null);
            String mLatitude = prefs.getString("Latitude", null);
            String mRadius = prefs.getString("Radius", null);
            Integer Guide = prefs.getInt("Guide", 0);
            if (mLongitud != null)
            {
                if (Guide != 0)
                {
                    Intent mainIntent = new Intent(LoginActivity.this,
                            MainActivity.class);
                    mainIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    mainIntent.putExtra("idUser", IDuser); //Optional parameters
                    startActivity(mainIntent);
                    LoginActivity.this.finish();
                    overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                }
                else{
                    Intent mainIntent = new Intent(LoginActivity.this,
                            ChooseZoneActivity.class);
                    mainIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    mainIntent.putExtra("idUser", IDuser); //Optional parameters
                    startActivity(mainIntent);
                    LoginActivity.this.finish();
                    overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                }
            }
            else{
                Intent mainIntent = new Intent(LoginActivity.this,
                        ChooseZoneActivity.class);
                mainIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                mainIntent.putExtra("idUser", IDuser); //Optional parameters
                startActivity(mainIntent);
                LoginActivity.this.finish();
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            }
        }

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(this);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        Button btnIngreseMail = findViewById(R.id.ingresarEmail);
        btnIngreseMail.setOnClickListener(this);

        Dots_Layout = (LinearLayout) findViewById(R.id.dotsLayout);
        createDots(0);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        @SuppressLint("WrongViewCast") SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();
        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText("INGRESA CON GOOGLE");
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(R.drawable.bordesgoogle);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, SIGN_IN);
            }
        });

        //FaceBook Login
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        fbbutton = (Button) findViewById(R.id.login_button);
        //bbutton.setBackgroundResource(0);
        fbbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onFblogin();
            }
        });


    }


    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    private void onFblogin() {
        callbackManager = CallbackManager.Factory.create();
        //loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    public static final String TAG_ERROR = "Error";
                    public static final String TAG_CANCEL = "a";

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject json, GraphResponse response) {
                                // Application code
                                if (response.getError() != null) {
                                    System.out.println("ERROR");
                                } else {
                                    System.out.println("Success");
                                    String jsonresult = String.valueOf(json);
                                    System.out.println("JSON Result" + jsonresult);

                                    String fbUserId = json.optString("id");
                                    String fbUserFirstName = json.optString("name");
                                    String fbUserEmail = json.optString("email");
                                    String fbUserProfilePics = "http://graph.facebook.com/" + fbUserId + "/picture?type=large";
                                    new UserLoginTask(fbUserEmail, fbUserFirstName).execute();
                                    Toast.makeText(LoginActivity.this, fbUserEmail, Toast.LENGTH_LONG).show();
                                }
                                Log.v("FaceBook Response :", response.toString());
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }
                    @Override
                    public void onCancel() {
                        Log.d(TAG_CANCEL,"On cancel");
                        Toast.makeText(LoginActivity.this,"On cancel: ",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG_ERROR,error.toString());
                        Toast.makeText(LoginActivity.this,"Error: " + error.toString(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent mainIntent;
        switch (id)
        {
            case R.id.ingresarEmail:
                mainIntent = new Intent(LoginActivity.this,
                        CreateAccountActivity.class);
                startActivity(mainIntent);
                LoginActivity.this.finish();
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                break;
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.email_sign_in_button:
                mainIntent = new Intent(LoginActivity.this,
                        SignInActivity.class);
                startActivity(mainIntent);
                LoginActivity.this.finish();
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            //get user's email
            String mEmail = acct.getEmail();

            //get user's full name
            String mFullName = acct.getDisplayName();

            String gPlusID = acct.getId();

            new UserLoginTask(mEmail, mFullName).execute();
            Toast.makeText(this, mEmail, Toast.LENGTH_LONG).show();
        }
    }


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Registrando cuenta...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        private final String mEmail;
        private final String mName;

        UserLoginTask(String email, String name) {
            mEmail = email;
            mName = name;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean flag = false;
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("Email", mEmail));
            nameValuePairs.add(new BasicNameValuePair("UserName", mName));

            String Resultado="";
            JSONObject json = jParser.makeHttpRequest(url + "RegisterUser", "POST", nameValuePairs);

            try {
                if (json != null){
                    int success = json.getInt(TAG_SUCCESS);
                    if (success == 200){
                        IDuser = json.getInt("IdUser");
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
            mAuthTask = null;
            if (success) {
/*                Intent myIntent = new Intent(CreateAccountActivity.this, ChooseZoneActivity.class);
                myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                myIntent.putExtra("key", IDuser); //Optional parameters
                CreateAccountActivity.this.startActivity(myIntent);*/
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("idUser", IDuser.toString());
                editor.apply();
                Intent mainIntent = new Intent(LoginActivity.this,
                        ChooseZoneActivity.class);
                mainIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                mainIntent.putExtra("idUser", IDuser); //Optional parameters
                startActivity(mainIntent);
                LoginActivity.this.finish();
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            } else {
                mNameView.setError(getString(R.string.error_incorrect_password));
                mNameView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            pDialog.dismiss();
            //mAuthTask = null;
            Toast.makeText(LoginActivity.this,"Sin conexión",Toast.LENGTH_LONG).show();
        }
    }
}

