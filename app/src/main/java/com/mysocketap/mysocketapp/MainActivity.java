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

public class MainActivity extends AppCompatActivity implements MainView{

    private Context context;
    private String request;
    private boolean isbusy;
    private Button connectButton;
    private EditText displayTxt;
    private ProgressBar loadingSpinner;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        presenter = new MainPresenterImpl(this);
        connectButton = (Button) findViewById(R.id.btnConnect);
        displayTxt = (EditText)findViewById(R.id.txtDisplay);
        loadingSpinner = (ProgressBar)findViewById(R.id.progressBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }
    @Override protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
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

    @Override
    public void onConnectButtonClicked(View view) {
        startConnection(view);
    }

    @Override
    public void startConnection(View view) {

        if(isbusy)
            return;

        request = new Constants().REQUESTXML;
        DoNetworkConnection doNetworkConnection = new DoNetworkConnection();
        doNetworkConnection.execute(request);
    }

    @Override
    public void setBusyState(Button connectButton) {
        displayTxt.setText(R.string.connecting);
        disableButton(connectButton);
        loadingSpinner.setVisibility(View.VISIBLE);
    }

    @Override
    public void setReadyState(Button connectButton) {
        isbusy = false;
        loadingSpinner.setVisibility(View.INVISIBLE);
        enableButton(connectButton);
    }

    @Override
    public void disableButton(Button connectButton) {
        connectButton.setEnabled(false);
        connectButton.setText("");
    }

    @Override
    public void enableButton(Button connectButton) {
        connectButton.setEnabled(true);
        connectButton.setText(getResources().getString(R.string.connect_to_server));
    }

    @Override
    public void showErrorMessage(String message) {
        showMessageDialog("Error", message, R.mipmap.error_icon);
    }

    @Override
    public void showSuccessMessage(String message) {
        showMessageDialog("Success", message, R.mipmap.success_icon);
    }

    @Override
    public void showMessageDialog(String title, String message, int icon) {

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

    @Override
    public void showToast(String message, int length) {
        Toast.makeText(context, message, length).show();
    }

    @Override
    public void writeToReadmetxtAndShowResults(String s) {
        showDisplay(s);
        try {
            writeToReadmetxft(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void showDisplay(String s) {
        displayTxt.setText(s);
    }

    @Override
    public void writeToReadmetxft(String s) throws IOException {

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

     class DoNetworkConnection extends AsyncTask<String, Integer, String> {

         private final String LOGSTRING = "log_string";
         private String ip = "10.0.2.2"; //"196.37.22.179";
         private int port = 5000; //9011;
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
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, encoding));

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
                showSuccessMessage(getString(R.string.request_success));
            }
            else
            {
               showDisplay(getResources().getString(R.string.not_connected_to_server));
               showErrorMessage(getString(R.string.request_error));
            }

            setReadyState(connectButton);
        }
     }

}