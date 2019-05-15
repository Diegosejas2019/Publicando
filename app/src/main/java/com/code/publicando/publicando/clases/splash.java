package com.code.publicando.publicando.clases;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.code.publicando.publicando.activitys.LoginActivity;
import com.code.publicando.publicando.R;
import com.code.publicando.publicando.activitys.MainActivity;

public class splash extends AppCompatActivity {
    private static final long SPLASH_SCREEN_DELAY = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

/*        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // Start the next activity
                Intent mainIntent = new Intent().setClass(
                        splash.this, LoginActivity.class);
                startActivity(mainIntent, ActivityOptions.makeSceneTransitionAnimation(splash.this).toBundle());

                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();


            }
        };*/

        // Simulate a long loading process on application startup.
/*        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);*/
        new Handler().postDelayed(new Runnable() {
            public void run() {

                /* Create an intent that will start the main activity. */
                Intent mainIntent = new Intent(splash.this,
                        MainActivity.class);
                //mainIntent.putExtra("id", "1");

                //SplashScreen.this.startActivity(mainIntent);
                startActivity(mainIntent);
                /* Finish splash activity so user cant go back to it. */
                splash.this.finish();

                     /* Apply our splash exit (fade out) and main
                        entry (fade in) animation transitions. */
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            }
        }, SPLASH_SCREEN_DELAY);
    }
}
