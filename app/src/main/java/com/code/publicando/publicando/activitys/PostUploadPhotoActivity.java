package com.code.publicando.publicando.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.code.publicando.publicando.R;
import com.code.publicando.publicando.clases.BitmapHelper;

import java.io.FileNotFoundException;
import java.io.IOException;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;

public class PostUploadPhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout Dots_Layout;
    private ImageView[] dots;
    public static final int GET_FROM_GALLERY = 3;
    private AutoCompleteTextView auto;
    private Button uploadButton;
    String[] elementos = {"Mecanico","Electricista","Pintor","Albañil", "Abogado"};

    private String mType;
    private String mAuto;
    private Bitmap mBitmap;
    private Integer mIdUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload_photo);
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
            mIdUser = b.getInt("idUser");
        }


        Dots_Layout = (LinearLayout) findViewById(R.id.dotsLayout);
        createDots(2);
        uploadButton =(Button) findViewById(R.id.uploadPhoto);
        uploadButton.setOnClickListener(this);
        Button uploadPhotoNext = findViewById(R.id.uploadPhotoNext);
        uploadPhotoNext.setOnClickListener(this);
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
            Intent myIntent = new Intent(PostUploadPhotoActivity.this, PostSetDetailActivity.class);
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
            case R.id.uploadPhoto:
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                break;
            case R.id.uploadPhotoNext:
                Intent myIntent = new Intent(PostUploadPhotoActivity.this, PostSetUbicationActivity.class);
                myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                BitmapHelper.getInstance().setBitmap(mBitmap);

                myIntent.putExtra("Detail", mAuto);
                myIntent.putExtra("Type", mType);
                myIntent.putExtra("idUser", mIdUser);
                startActivity(myIntent);
                PostUploadPhotoActivity.this.finish();
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            mBitmap = null;
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ImageView img = findViewById(R.id.imageUpload);
                img.setImageBitmap(mBitmap);

//Saves the  image
               // MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, timestamp, timestamp);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
