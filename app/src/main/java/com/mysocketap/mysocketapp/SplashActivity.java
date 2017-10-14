package com.mysocketap.mysocketapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.mysocketap.mysocketapp.views.SocetConnectionView;
import com.mysocketap.mysocketapp.views.SocketConnectionActivity;

public class SplashActivity extends AppCompatActivity implements SocetConnectionView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = new Intent(this, SocketConnectionActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }

}