package com.code.publicando.publicando;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.BufferedReader;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;

public class ChooseZoneActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout Dots_Layout;
    private ImageView[] dots;
    private Button chooseButton;
    private Button myUbication;
    private Integer mIdUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_zone);

        Bundle b = getIntent().getExtras();
        if(b != null){
            mIdUser = b.getInt("idUser");
        }

        Dots_Layout = (LinearLayout) findViewById(R.id.dotsLayout);
        createDots(1);

        chooseButton = findViewById(R.id.choose);
        chooseButton.setOnClickListener(this);

        myUbication = findViewById(R.id.myUbication);
        myUbication.setOnClickListener(this);
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
    public void onClick(View view)
    {
        Intent myIntent;
        switch (view.getId())
        {
            case R.id.myUbication:
                myIntent = new Intent(ChooseZoneActivity.this, LocationActivity.class);
                myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                ChooseZoneActivity.this.startActivity(myIntent);
                myIntent.putExtra("idUser", mIdUser); //Optional parameters
                break;
            case R.id.choose:
                myIntent = new Intent(ChooseZoneActivity.this,
                        SetZoneActivity.class);
                myIntent.putExtra("idUser", mIdUser); //Optional parameters
                startActivity(myIntent);
                ChooseZoneActivity.this.finish();
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(ChooseZoneActivity.this, CreateAccountActivity.class);
        myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        ChooseZoneActivity.this.startActivity(myIntent);
    }
}
