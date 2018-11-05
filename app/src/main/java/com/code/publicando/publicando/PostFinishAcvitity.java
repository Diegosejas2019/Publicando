package com.code.publicando.publicando;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
            mBitmap = b.getParcelable("Bitmap");
            mRadius = b.getInt("Radius");
            mLatitude = b.getDouble("Latitude");
            mLongitude = b.getDouble("Longitude");
            mCelular = b.getString("Celular");
            mPhone = b.getString("Phone");
            mDescription = b.getString("Description");
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
                        Intent myIntent = new Intent(PostFinishAcvitity.this, MainActivity.class);
                        myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                        startActivity(myIntent);
                        finish(); // close this activity and return to preview activity (if there is any)
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
                showInfoAlert();
                break;
        }
    }
}
