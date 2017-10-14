package com.mysocketap.mysocketapp.presenters;

import android.view.View;
import android.widget.Button;

import java.io.IOException;

public interface MainPresenter {
    void onResume();
    void onDestroy();

    void startConnection(View view);
    void setBusyState(Button connectButton);
    void setReadyState(Button connectButton);
    void disableButton(Button connectButton);
    void enableButton(Button connectButton);
    void showErrorMessage(String message);
    void showSuccessMessage(String message);
    void showMessageDialog(String title, String message, int icon);
    void showToast(String message, int length);
    void writeToReadmetxtAndShowResults(String s);
    void showDisplay(String s);
    void writeToReadmetxft(String s) throws IOException;

}
