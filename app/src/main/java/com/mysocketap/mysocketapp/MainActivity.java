package com.mysocketap.mysocketapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Environment;
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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private String request;
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

        request = new Constants().XMLMESSAGE;
        DoNetworkConnection doNetworkConnection = new DoNetworkConnection();
        doNetworkConnection.execute(request);
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

    private void writeToReadmetxtAndShowResults(String s) {
        displayTxt.setText(s);
        try {
            writeToReadmetxft(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToReadmetxft(String s) throws IOException {

        // this will create a new name everytime and unique
        File root = new File(Environment.getExternalStorageDirectory(), "Notes");

        // if external memory exists and folder with name Notes

        if (!root.exists())
            root.mkdirs(); // this will create folder.

        File filepath = new File(root, "readme.txt");  // file path to save
        FileWriter writer = new FileWriter(filepath);
        writer.append(s);
        writer.flush();
        writer.close();
    }

     class DoNetworkConnection extends AsyncTask<String, Integer, String> {

         private final String LOGSTRING = "log_string";
         private String ip = "196.37.22.179";
         private int port = 9011;
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

                boolean isconnected = ip.equals(InetAddress.getLocalHost().getHostAddress().toString());

                socket = new Socket(ip, port);
                socket.setSoTimeout(10000);

                String encoding = "UTF-8";
                OutputStream outputStream = socket.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, encoding));
                bufferedWriter.write(params[0]);
                bufferedWriter.flush();

                InputStream inputStream = socket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"));

                response = bufferedReader.readLine();

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
                writeToReadmetxtAndShowResults(s);
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