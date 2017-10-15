package com.mysocketap.mysocketapp.presenters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mysocketap.mysocketapp.R;
import com.mysocketap.mysocketapp.constants.Constants;
import com.mysocketap.mysocketapp.models.DoNetworkConnection;
import com.mysocketap.mysocketapp.views.ISocketConnectionView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SocketConnectionPresenter implements ISocketConnectionPresenter {

    public Activity activity;
    public String request;
    public boolean isbusy;
    public Button connectButton;
    public EditText displayTxt;
    public ProgressBar loadingSpinner;
    public ISocketConnectionView ISocketConnectionView;

    public SocketConnectionPresenter(ISocketConnectionView ISocketConnectionView) {
        this.ISocketConnectionView = ISocketConnectionView;
        activity = (Activity) ISocketConnectionView;
        connectButton = (Button) activity.findViewById(R.id.btnConnect);
        displayTxt = (EditText)activity.findViewById(R.id.txtDisplay);
        loadingSpinner = (ProgressBar)activity.findViewById(R.id.progressBar);
    }

    @Override
    public void onResume() {
        String welcomeMessage = ((Activity) ISocketConnectionView).getResources().getString(R.string.welcome_message);
        showToast(welcomeMessage, Toast.LENGTH_SHORT);
    }

    @Override
    public void onDestroy() {
        ISocketConnectionView = null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // the first item in the menu is of id @id/quit and its purpose is to close the app, the second item is of @id/connect and it just calls the startConnection method

        switch (item.getItemId()) {
            case R.id.quit:
                activity.finish();
                break;
            case R.id.connect:
                startConnection(null);
                break;

            default:
                return activity.onOptionsItemSelected(item);
        }
        return activity.onOptionsItemSelected(item);

    }

    public void onConnectButtonClicked(View view) {
        // this method handles the connect button's click event and then calls the startConnection method
        startConnection(view);
    }

    public ISocketConnectionView getISocketConnectionView() {
        return ISocketConnectionView;
    }

    @Override
    public void startConnection(View view) {
        // This method is responsible for creating the connection between the client and the server
        if(isbusy)
            return;

        request = Constants.REQUESTXML;
        DoNetworkConnection doNetworkConnection = new DoNetworkConnection(this);
        doNetworkConnection.execute(request);
    }

    @Override
    public void setBusyState(Button connectButton) {
        // This method sets the state of the app as busy, it changes the views to reflect that their busy processing
        displayTxt.setText(R.string.connecting);
        disableButton(connectButton);
        loadingSpinner.setVisibility(View.VISIBLE);
        isbusy = true;
    }

    @Override
    public void setReadyState(Button connectButton) {
        // this method sets the state of the app as ready to perform actions, meaning there are no processes running and it also changes the views to reflect their ready status
        isbusy = false;
        loadingSpinner.setVisibility(View.INVISIBLE);
        enableButton(connectButton);
    }

    @Override
    public void disableButton(Button connectButton) {
        // This method disables the button when the app is busy in the background
        connectButton.setEnabled(false);
        connectButton.setText("");
    }

    @Override
    public void enableButton(Button connectButton) {
        // This method enables the button when the call is finished
        connectButton.setEnabled(true);
        connectButton.setText(activity.getResources().getString(R.string.connect_to_server));
    }

    @Override
    public void showErrorMessage(String message) {
        //This method formats the showMessage method to be an error type
        showMessageDialog("Error", message, R.mipmap.error_icon);
    }

    @Override
    public void showSuccessMessage(String message) {
        //This method formats the showMessage method to be a success type
        showMessageDialog("Success", message, R.mipmap.success_icon);
    }

    @Override
    public void showMessageDialog(String title, String message, int icon) {
        // This method shows a dilog message
        AlertDialog.Builder ab = new AlertDialog.Builder(activity);
        ab.setTitle(title).setMessage(message).setIcon(icon).
                setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog a = ab.create();
        a.show();
    }

    @Override
    public void showToast(String message, int length) {
        // This method just shows a toast message
        Toast.makeText(activity, message, length).show();
    }

    @Override
    public void writeToReadmetxtAndShowResults(String s) {
        // This method displays the results and also writes the results to the readme.txt file
        showDisplay(s);
        try {
            writeToReadmetxft(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void showDisplay(String s) {
        //This method displays what ever string is passed through in the display EditText
        displayTxt.setText(s);
    }

    @Override
    public void writeToReadmetxft(String s) throws IOException {

        // For the purpose of this demo I'm putting the readme.txt file in the sd card in a folder called notes

        // this will get the sd card then create a folder called notes
        File root = new File(Environment.getExternalStorageDirectory(), "Notes");

        // if it doesnt exist then create the directory
        if (!root.exists())
            root.mkdirs(); // this will create folder.

        File filepath = new File(root, "readme.txt");  // file path to save
        FileWriter writer = new FileWriter(filepath);
        writer.append(s);
        writer.flush();
        writer.close();
    }
}
