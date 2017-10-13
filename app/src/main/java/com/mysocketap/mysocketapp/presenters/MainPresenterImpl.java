package com.mysocketap.mysocketapp.presenters;

import android.widget.Toast;
import com.mysocketap.mysocketapp.views.MainView;

public class MainPresenterImpl implements MainPresenter {

    private MainView mainView;

    public MainPresenterImpl(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void onResume() {
        mainView.showToast("Welcome to my socket app", Toast.LENGTH_SHORT);
    }

    @Override
    public void onDestroy() {
        mainView = null;
    }

    public MainView getMainView() {
        return mainView;
    }
}
