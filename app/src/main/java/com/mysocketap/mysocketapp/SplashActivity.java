package com.mysocketap.mysocketapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.mysocketap.mysocketapp.views.SocketConnectionActivity;

public class SplashActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delaySplashScreen();
    }

    private void goToConnectionActivity() {
        Intent i = new Intent(this, SocketConnectionActivity.class);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        startActivity(i);
        finish();
    }

    private void delaySplashScreen() {

        Thread lTimer = new Thread() {

            public void run() {

                try {
                    Thread.sleep(3000);
                    goToConnectionActivity();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        lTimer.start();
    }

}