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

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout Dots_Layout;
    private ImageView[] dots;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        buttonLogin = findViewById(R.id.email_sign_in_button);
        buttonLogin.setOnClickListener(this);
        Dots_Layout = (LinearLayout) findViewById(R.id.dotsLayout);
        createDots(0);

        Button btn = findViewById(R.id.next);
        btn.setOnClickListener(this);
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

        Intent mainIntent = new Intent(CreateAccountActivity.this,
                ChooseZoneActivity.class);
        startActivity(mainIntent);
        CreateAccountActivity.this.finish();
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
    }

    @Override
    public void onBackPressed() {
      Intent myIntent = new Intent(CreateAccountActivity.this, LoginActivity.class);
        myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        //myIntent.putExtra("key", IDuser); //Optional parameters
        CreateAccountActivity.this.startActivity(myIntent);
    }
}
