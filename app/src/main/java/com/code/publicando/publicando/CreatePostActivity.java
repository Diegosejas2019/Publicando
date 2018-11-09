package com.code.publicando.publicando;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;

public class CreatePostActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout Dots_Layout;
    private ImageView[] dots;
    private Integer mIdUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
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
        if(b != null){
            mIdUser = b.getInt("idUser");
        }

        Dots_Layout = (LinearLayout) findViewById(R.id.dotsLayout);
        createDots(0);
        Button btnProducto = findViewById(R.id.btnProducto);
        btnProducto.setOnClickListener(this);
        Button btnServicio = findViewById(R.id.btnServicio);
        btnServicio.setOnClickListener(this);
        Button btnComercio = findViewById(R.id.btnComercio);
        btnComercio.setOnClickListener(this);
        Button btnPromocion = findViewById(R.id.btnPromocion);
        btnPromocion.setOnClickListener(this);
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
            Intent myIntent = new Intent(CreatePostActivity.this, MainActivity.class);
            myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            startActivity(myIntent);
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(CreatePostActivity.this, MainActivity .class);
        myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        //myIntent.putExtra("key", IDuser); //Optional parameters
        CreatePostActivity.this.startActivity(myIntent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        Intent myIntent = new Intent(CreatePostActivity.this, PostSetDetailActivity .class);
        myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);

        switch (id)
        {
            case R.id.btnProducto:
                myIntent.putExtra("Type", "Producto"); //Optional parameters
                break;
            case R.id.btnServicio:
                myIntent.putExtra("Type", "Servicio"); //Optional parameters
                break;
            case R.id.btnComercio:
                myIntent.putExtra("Type", "Comercio"); //Optional parameters
                break;
            case R.id.btnPromocion:
                myIntent.putExtra("Type", "Promocion"); //Optional parameters
                break;
        }

        myIntent.putExtra("idUser", mIdUser); //Optional parameters
        CreatePostActivity.this.startActivity(myIntent);
        CreatePostActivity.this.finish();;
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
    }
}
