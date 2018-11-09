package com.code.publicando.publicando;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;

public class PostSetDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout Dots_Layout;
    private ImageView[] dots;
    private String mType;
    private AutoCompleteTextView mAuto;
    private Integer mIdUser;

    String[] elementos = {"Mecanico","Electricista","Pintor","Albañil", "Abogado"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_set_detail);
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
            mIdUser = b.getInt("idUser");
        }

        Dots_Layout = (LinearLayout) findViewById(R.id.dotsLayout);
        createDots(1);

        mAuto = (AutoCompleteTextView) findViewById(R.id.auto);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,elementos);
        mAuto.setThreshold(3);
        mAuto.setAdapter(adapter);

        Button btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
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
            Intent myIntent = new Intent(PostSetDetailActivity.this, CreatePostActivity.class);
            myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
            startActivity(myIntent);
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(PostSetDetailActivity.this, CreatePostActivity .class);
        myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        //myIntent.putExtra("key", IDuser); //Optional parameters
        PostSetDetailActivity.this.startActivity(myIntent);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.btnNext:
                mAuto.setError(null);

                mAuto = findViewById(R.id.auto);

                String detail = mAuto.getText().toString();

                boolean cancel = false;
                View focusView = null;

                if (TextUtils.isEmpty(detail)) {
                    mAuto.setError(getString(R.string.error_field_required));
                    focusView = mAuto;
                    cancel = true;
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {
                    Intent myIntent = new Intent(PostSetDetailActivity.this, PostUploadPhotoActivity .class);
                    myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                    myIntent.putExtra("Detail", mAuto.getText().toString());
                    myIntent.putExtra("Type", mType);
                    myIntent.putExtra("idUser", mIdUser);
                    PostSetDetailActivity.this.startActivity(myIntent);
                    PostSetDetailActivity.this.finish();
                    overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                }
                break;
        }
    }
}
