package com.mysocketap.mysocketapp.views;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public interface ISocetConnectionView {
    /*void onResume();
    void onDestroy(); */
    boolean onCreateOptionsMenu(Menu menu);
    boolean onOptionsItemSelected(MenuItem item);
    void onConnectButtonClicked(View view);
}
