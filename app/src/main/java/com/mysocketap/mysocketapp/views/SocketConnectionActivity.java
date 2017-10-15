package com.mysocketap.mysocketapp.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.mysocketap.mysocketapp.R;
import com.mysocketap.mysocketapp.presenters.MainPresenter;
import com.mysocketap.mysocketapp.presenters.MainPresenterImpl;

public class SocketConnectionActivity extends AppCompatActivity implements SocetConnectionView {

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_connection);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        presenter = new MainPresenterImpl(this);
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // the first item in the menu is of id @id/quit and its purpose is to close the app, the second item is of @id/connect and it just calls the startConnection method

        switch (item.getItemId()) {
            case R.id.quit:
                finish();
                break;
            case R.id.connect:
                presenter.startConnection(null);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);

    }

    public void onConnectButtonClicked(View view) {
        // this method handles the connect button's click event and then calls the startConnection method
        presenter.startConnection(view);
    }

}