package com.example.rapidscholar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

public class SplashScreen extends Activity{
 
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1500;
    TextView txtView;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        txtView = (TextView) findViewById(R.id.subheading);
        
        final AlphaAnimation fadeIn = new AlphaAnimation(1.0f , 0.0f ) ; 
    	final AlphaAnimation fadeOut = new AlphaAnimation( 0.0f , 1.0f ) ; 

	    	txtView.startAnimation(fadeIn);
	    	txtView.startAnimation(fadeOut);
    	
	    	fadeIn.setDuration(1200);
	    	fadeIn.setFillAfter(false);
	    	fadeOut.setDuration(1200);
	    	fadeOut.setFillAfter(true);

    	
 
        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
            	
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
 
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
 
}