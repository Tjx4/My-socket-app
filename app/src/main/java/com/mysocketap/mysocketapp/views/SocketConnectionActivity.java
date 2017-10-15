package com.mysocketap.mysocketapp.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.mysocketap.mysocketapp.R;
import com.mysocketap.mysocketapp.presenters.ISocketConnectionPresenter;
import com.mysocketap.mysocketapp.presenters.SocketConnectionPresenter;

public class SocketConnectionActivity extends AppCompatActivity implements ISocetConnectionView {

    private ISocketConnectionPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_connection);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        presenter = new SocketConnectionPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Calls onCreateOptionsMenu in the presenter
        return presenter.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Calls onOptionsItemSelected in the presenter
        return presenter.onOptionsItemSelected(item);
    }

    public void onConnectButtonClicked(View view) {
        // this method handles the connect button's click event and then calls the startConnection method
        presenter.startConnection(view);
    }

}