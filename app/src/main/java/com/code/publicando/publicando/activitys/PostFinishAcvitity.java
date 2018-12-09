package com.code.publicando.publicando.activitys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.code.publicando.publicando.R;
import com.code.publicando.publicando.clases.BitmapHelper;
import com.code.publicando.publicando.clases.JSONParser;
import com.code.publicando.publicando.clases.Url;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;

public class PostFinishAcvitity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout Dots_Layout;
    private ImageView[] dots;

    private String mType;
    private String mAuto;
    private Bitmap mBitmap;
    private Integer mRadius;
    private Double mLatitude;
    private Double mLongitude;
    private String mCelular;
    private String mPhone;
    private String mDescription;;
    private Integer mIdUser;

    //private String url = "http://10.0.2.2/api/login/";
    //private String url = "http://192.168.1.149/api/login/";
    JSONParser jParser = new JSONParser();
    private ProgressDialog pDialog;
    private EditText mEmailView;
    private EditText mNameView;
    private Integer IDuser;
    private static final String TAG_SUCCESS = "StatusCode";
    private static final String TAG_USER = "UserName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_finish_acvitity);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("PUBLICAR ANUNCIO");
        }
        catch (Exception e )
        {
            Log.e(e.getMessage(),"TEST");
        }

        Bundle b = getIntent().getExtras();
        int value = -1; // or other values
        if(b != null){
            mType = b.getString("Type");
            mAuto = b.getString("Detail");
            mBitmap = BitmapHelper.getInstance().getBitmap();
            mRadius = b.getInt("Radius");
            mLatitude = b.getDouble("Latitude");
            mLongitude = b.getDouble("Longitude");
            mCelular = b.getString("Celular");
            mPhone = b.getString("Phone");
            mDescription = b.getString("Description");
            mIdUser = b.getInt("idUser");
        }

        ImageView imagenPost = findViewById(R.id.imagenPost);
        imagenPost.setImageBitmap(mBitmap);

        TextView detalleServicio = findViewById(R.id.detalleServicio);
        detalleServicio.setText(mAuto);

        TextView celular = findViewById(R.id.celular);
        celular.setText(mCelular);

        TextView telefono = findViewById(R.id.telefono);
        telefono.setText(mPhone);

        TextView descripcion = findViewById(R.id.description);
        descripcion.setText(mDescription);



        Dots_Layout = (LinearLayout) findViewById(R.id.dotsLayout);
        createDots(5);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            Intent myIntent = new Intent(PostFinishAcvitity.this, PostFormActivity.class);
            myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            startActivity(myIntent);
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void showInfoAlert()
    {
        new AlertDialog.Builder(this)
                .setTitle("Publicación realizada!")
                .setMessage("¡Tu publicidad fue cargada con éxito!\n\n Vamos a verificar los " +
                        "datos ingresados y en breve te avisaremos cuando este publicado tu anuncio." +
                        "\n\n ¡Muchas gracias!")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent mainIntent = new Intent(PostFinishAcvitity.this,
                                MainActivity.class);
                        mainIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                        mainIntent.putExtra("idUser", IDuser); //Optional parameters
                        startActivity(mainIntent);
                        PostFinishAcvitity.this.finish();
                        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                    }
                })
                //.setNegativeButton("Cancel",null )
                .show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id)
        {
            case R.id.btnPublicar:
                new RegisterPost().execute();

                break;
        }
    }

    class RegisterPost extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PostFinishAcvitity.this);
            pDialog.setMessage("Realizando Publicación...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean flag = false;
            
            ByteArrayOutputStream bao = new ByteArrayOutputStream();

            //Resize the image
            double width = mBitmap.getWidth();
            double height = mBitmap.getHeight();
            double ratio = 400/width;
            int newheight = (int)(ratio * height);

            mBitmap = Bitmap.createScaledBitmap(mBitmap, 400, newheight, true);

            //Here you can define .PNG as well
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 95, bao);
            byte[] ba = bao.toByteArray();
            String base64 = Base64.encodeToString(ba, Base64.DEFAULT);
            
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("TypeWork", mType));
            nameValuePairs.add(new BasicNameValuePair("WorkDetail", mAuto));
            nameValuePairs.add(new BasicNameValuePair("Image64", base64));
            nameValuePairs.add(new BasicNameValuePair("Radius", mRadius.toString()));
            nameValuePairs.add(new BasicNameValuePair("Latitude", mLatitude.toString()));
            nameValuePairs.add(new BasicNameValuePair("Longitude", mLongitude.toString()));
            nameValuePairs.add(new BasicNameValuePair("Celular", mCelular));
            nameValuePairs.add(new BasicNameValuePair("Phone", mPhone));
            nameValuePairs.add(new BasicNameValuePair("Description", mDescription));
            nameValuePairs.add(new BasicNameValuePair("idUser", mIdUser.toString()));


            String Resultado="";
            Url url = new Url();
            JSONObject json = jParser.makeHttpRequest(url.getDireccion() + "/api/master/RegisterPost", "POST", nameValuePairs);

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
            if (success) {
/*                Intent myIntent = new Intent(CreateAccountActivity.this, ChooseZoneActivity.class);
                myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                myIntent.putExtra("key", IDuser); //Optional parameters
                CreateAccountActivity.this.startActivity(myIntent);*/
                showInfoAlert();

            } else {
                mNameView.setError(getString(R.string.error_incorrect_password));
                mNameView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            pDialog.dismiss();
            //mAuthTask = null;
            Toast.makeText(PostFinishAcvitity.this,"Sin conexión",Toast.LENGTH_LONG).show();
        }
    }
}
