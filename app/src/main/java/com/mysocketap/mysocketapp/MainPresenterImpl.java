package com.mysocketap.mysocketapp;


import android.widget.Toast;

public class MainPresenterImpl implements MainPresenter{

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
