package com.code.publicando.publicando;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;

public class PostFormActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout Dots_Layout;
    private ImageView[] dots;
    private String mType;
    private String mAuto;
    private Bitmap mBitmap;
    private Integer mRadius;
    private Double mLatitude;
    private Double mLongitude;
    private TextView mCelular;
    private TextView mPhone;
    private TextView mDescription;
    private Integer mIdUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_form);
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
            mBitmap =  (Bitmap) b.getParcelable("Photo");
            mRadius = b.getInt("Radius");
            mLatitude = b.getDouble("Latitude");
            mLongitude = b.getDouble("Longitude");
            mIdUser = b.getInt("idUser");
        }


        Dots_Layout = (LinearLayout) findViewById(R.id.dotsLayout);
        createDots(4);
        Button postformnext = findViewById(R.id.postformnext);
        postformnext.setOnClickListener(this);

        mCelular = findViewById(R.id.celular);
        mPhone = findViewById(R.id.telefono);
        mDescription = findViewById(R.id.description);
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
            Intent myIntent = new Intent(PostFormActivity.this, PostSetUbicationActivity.class);
            myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            startActivity(myIntent);
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.postformnext:
                mCelular.setError(null);
                mDescription.setError(null);

                String celular = mCelular.getText().toString();
                String description = mDescription.getText().toString();
                boolean cancel = false;
                View focusView = null;

                if (TextUtils.isEmpty(celular)) {
                    mCelular.setError(getString(R.string.error_field_required));
                    focusView = mCelular;
                    cancel = true;
                }

                if (TextUtils.isEmpty(description)) {
                    mDescription.setError(getString(R.string.error_field_required));
                    focusView = mDescription;
                    cancel = true;
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {
                    Intent myIntent = new Intent(PostFormActivity.this, PostFinishAcvitity.class);
                    myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    myIntent.putExtra("Type", mType);
                    myIntent.putExtra("Detail", mAuto);
                    myIntent.putExtra("idUser", mIdUser);
                    myIntent.putExtra("Radius", mRadius);
                    myIntent.putExtra("Latitude", mLatitude);
                    myIntent.putExtra("Longitude", mLongitude);
                    myIntent.putExtra("Celular", mCelular.getText().toString());
                    myIntent.putExtra("Phone", mPhone.getText().toString());
                    myIntent.putExtra("Description", mDescription.getText().toString());
                    PostFormActivity.this.startActivity(myIntent);
                    PostFormActivity.this.finish();
                    overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(PostFormActivity.this, PostSetUbicationActivity.class);
        myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        //myIntent.putExtra("key", IDuser); //Optional parameters
        PostFormActivity.this.startActivity(myIntent);
        finish();
    }
}
