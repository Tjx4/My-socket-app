package com.mysocketap.mysocketapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private String message;
    private boolean isbusy;

    private Button connectButton;

    private EditText displayTxt;
    private ProgressBar loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        connectButton = (Button) findViewById(R.id.btnConnect);
        displayTxt = (EditText)findViewById(R.id.txtDisplay);
        loadingSpinner = (ProgressBar)findViewById(R.id.progressBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.quit:
                    finish();
            break;
            case R.id.connect:
                startConnection(null);
            break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onConnectButtonClicked(View view) {
        startConnection(view);
    }

    public void startConnection(View view) {

        if(isbusy)
            return;

        message = "Hello server";
        DoNetworkConnection doNetworkConnection = new DoNetworkConnection();
        doNetworkConnection.execute(message);
    }

    private void setBusyState(Button connectButton) {
        displayTxt.setText(R.string.connecting);
        disableButton(connectButton);
        loadingSpinner.setVisibility(View.VISIBLE);
    }

    private void setReadyState(Button connectButton) {
        isbusy = false;
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

    private void writeToReadmetxt(String s) {
        Toast.makeText(context, "Result = "+s,Toast.LENGTH_LONG).show();
        displayTxt.setText(s);
    }

     class DoNetworkConnection extends AsyncTask<String, Integer, String> {

         private final String LOGSTRING = "log_string";
         private String ip = "192.168.0.101";  // 196.37.22.179;
         private int port = 5000; //   9011
         private boolean isSuccessful;
         private  Socket socket;

         public DoNetworkConnection() {}

         @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setBusyState(connectButton);
        }

        @Override
        protected String doInBackground(String... params) {

            String response = "";

            isbusy = true;

            try {

                socket = new Socket(ip, port);
                socket.setSoTimeout(10000);

                String encoding = "UTF-8";
                OutputStream outputStream = socket.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, encoding));
                bufferedWriter.write(params[0]);
                bufferedWriter.flush();

                InputStream inputStream = socket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String str;

                while ( (str = bufferedReader.readLine()) != null) {
                    response += str;
                }

                bufferedWriter.close();
                outputStream.close();

                bufferedReader.close();
                inputStream.close();
                socket.close();

                isSuccessful = true;

            }
            catch (UnknownHostException e) {
                Log.e(LOGSTRING, "UnknownHostException: "+e);
            }
            catch (IOException e){
                Log.e(LOGSTRING, "IOException: "+e);
            }
            catch (Resources.NotFoundException e){
                Log.e(LOGSTRING, "NotFoundException: "+e);
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(isSuccessful) {
                writeToReadmetxt(s);
                showSuccessMessage("Data sent successfully");
            }
            else
            {
               displayTxt.setText(R.string.not_connected_to_server);
               showErrorMessage("Error sending data");
            }

            setReadyState(connectButton);
        }
     }

}