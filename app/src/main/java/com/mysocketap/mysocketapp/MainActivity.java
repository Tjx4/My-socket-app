package com.mysocketap.mysocketapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private final String LOGSTRING = "log_string";
    private String ip = "http://emilygracetechnologies.com/";  //"196.37.22.179";
    private String message;
    private int port =  5000;//9011;

    private static Socket socket;
    private static ServerSocket serverSocket;
    private static PrintWriter printWriter;

    private EditText displayTxt;
    private ProgressBar loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        displayTxt = (EditText)findViewById(R.id.txtDisplay);
        loadingSpinner = (ProgressBar)findViewById(R.id.progressBar);
    }


    public void onConnectButtonClicked(View view) {
        Button connectButton = (Button)view;
        message = "Hello";
        DoNetworkConnection doNetworkConnection = new DoNetworkConnection(connectButton);
        doNetworkConnection.execute();
    }

    private void setBusyState(Button connectButton) {
        displayTxt.setText(R.string.connecting);
        disableButton(connectButton);
        loadingSpinner.setVisibility(View.VISIBLE);
    }

    private void setReadyState(Button connectButton) {
        loadingSpinner.setVisibility(View.INVISIBLE);
        enableButton(connectButton);
    }

  private void disableButton(Button connectButton) {
        connectButton.setEnabled(false);
        connectButton.setText("");
    }

    private void enableButton(Button connectButton) {
        connectButton.setEnabled(true);
        connectButton.setText(getResources().getString(R.string.connect_to_server));
    }

    private void showErrorMessage(String message) {
        showMessageDialog("Error", message, R.mipmap.error_icon);
    }

    private void showSuccessMessage(String message) {
        showMessageDialog("Success", message, R.mipmap.success_icon);
    }

    private void showMessageDialog(String title, String message, int icon) {

        AlertDialog.Builder ab = new AlertDialog.Builder(context);
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


     class DoNetworkConnection extends AsyncTask<String, Integer, String> {

         private Button connectButton;
         private boolean isSuccessful;

         public DoNetworkConnection(Button connectButton) {
             this.connectButton = connectButton;
         }

         @Override
        protected void onPreExecute() {
            super.onPreExecute();

            setBusyState(connectButton);
        }

        @Override
        protected String doInBackground(String... strings) {

            String results;

            try {
                Looper.prepare();

                socket = new Socket(ip,port);

                socket.setSoTimeout(5000);

                printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.write(message);
                printWriter.flush();
                printWriter.close();

                results = "Ok";
                isSuccessful = true;

            }
            catch (IOException e){
                Log.e(LOGSTRING, "Error: "+e);
                results = "bad";
            }

            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(isSuccessful) {
                showSuccessMessage("Data sent successfully");
            }
            else
            {
               showErrorMessage("Error sending data");
            }

            setReadyState(connectButton);
        }
     }

}