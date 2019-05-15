package com.code.publicando.publicando.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.code.publicando.publicando.R;
import com.code.publicando.publicando.clases.JSONParser;
import com.code.publicando.publicando.clases.Url;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout Dots_Layout;
    private ImageView[] dots;
    private Button buttonLogin;
    JSONParser jParser = new JSONParser();
    private ProgressDialog pDialog;
    private EditText mEmailView;
    private EditText mPassword;
    private EditText mRePassword;
    private Integer IDuser;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private static final String TAG_SUCCESS = "StatusCode";
    private static final String TAG_USER = "UserName";
    private UserLoginTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        buttonLogin = findViewById(R.id.next);
        buttonLogin.setOnClickListener(this);
        Dots_Layout = (LinearLayout) findViewById(R.id.dotsLayout);
        createDots(0);

        mEmailView = findViewById(R.id.createEmail);
        mPassword = findViewById(R.id.createPassword);
        mRePassword = findViewById(R.id.confirmarContrasena);

        /*Button btn = findViewById(R.id.next);UserLoginTask
        btn.setOnClickListener(this);*/
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
/*        Intent myIntent = new Intent(CreateAccountActivity.this, ChooseZoneActivity.class);
        myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        //myIntent.putExtra("key", IDuser); //Optional parameters
        CreateAccountActivity.this.startActivity(myIntent);*/
        int id = view.getId();
        switch (id)
        {
            case R.id.next:
                crearCuenta();
                break;
        }

    }

    private void crearCuenta() {
        mEmailView.setError(null);
        mPassword.setError(null);
        mRePassword.setError(null);

        mEmailView = findViewById(R.id.createEmail);
        mPassword = findViewById(R.id.createPassword);
        mRePassword = findViewById(R.id.confirmarContrasena);

        String email = mEmailView.getText().toString();
        String contrasena = mPassword.getText().toString();
        String recontrasena = mPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(contrasena)) {
            mPassword.setError("Compo obligatorio");
            focusView = mPassword;
            cancel = true;
        }

        if (!recontrasena.equals(contrasena))
        {
            mRePassword.setError("Las contraseñas deben coincidir");
            focusView = mRePassword;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Compo obligatorio");
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError("Correo invalido");
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("Email", email);
            editor.putString("Name", contrasena);
            editor.apply();
            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
            new UserLoginTask(email, contrasena).execute();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isNameValid(String name) {
        return name.length() > 4;
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateAccountActivity.this);
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
            Url url = new Url();
            JSONObject json = jParser.makeHttpRequest(url.getDireccion() + "/api/master/RegisterUser", "POST", nameValuePairs);

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
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putInt("idUser", IDuser);
                editor.apply();
                Intent mainIntent = new Intent(CreateAccountActivity.this,
                        ChooseZoneActivity.class);
                mainIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                mainIntent.putExtra("idUser", IDuser); //Optional parameters
                startActivity(mainIntent);
                CreateAccountActivity.this.finish();
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            } else {
                mPassword.setError("Contraseña incorrecta");
                mPassword.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            pDialog.dismiss();
            //mAuthTask = null;
            Toast.makeText(CreateAccountActivity.this,"Sin conexión",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
      Intent myIntent = new Intent(CreateAccountActivity.this, NewLogin.class);
        myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        //myIntent.putExtra("key", IDuser); //Optional parameterse
        CreateAccountActivity.this.startActivity(myIntent);
    }
}
