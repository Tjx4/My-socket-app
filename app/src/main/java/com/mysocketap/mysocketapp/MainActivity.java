package com.mysocketap.mysocketapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.mysocketap.mysocketapp.views.SocetConnectionView;
import com.mysocketap.mysocketapp.views.SocketConnectionActivity;

public class MainActivity extends AppCompatActivity implements SocetConnectionView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(this, SocketConnectionActivity.class);
        startActivity(i);
        finish();
    }

}