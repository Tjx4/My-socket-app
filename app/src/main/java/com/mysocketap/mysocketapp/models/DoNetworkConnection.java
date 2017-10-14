package com.mysocketap.mysocketapp.models;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import com.mysocketap.mysocketapp.R;
import com.mysocketap.mysocketapp.presenters.MainPresenterImpl;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class DoNetworkConnection extends AsyncTask<String, Integer, String> {

    private final String LOGSTRING = "log_string";
    private String ip = "10.0.2.2"; //"196.37.22.179";
    private int port = 5000; //9011;
    private boolean isSuccessful;
    private Socket socket;

    private MainPresenterImpl presenter;

    public DoNetworkConnection(MainPresenterImpl presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        presenter.setBusyState(presenter.connectButton);
    }

    @Override
    protected String doInBackground(String... params) {

        String response = "";

        presenter.isbusy = true;

        try {

            socket = new Socket(ip, port); // I instanciate the socket
            socket.setSoTimeout(10000); // I set the connection timeout

            String encoding = "UTF-8"; // set the encoding
            OutputStream outputStream = socket.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, encoding));  // write to socket using buffered writer

            bufferedWriter.write(params[0]);
            bufferedWriter.flush();

            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, encoding));

            response = bufferedReader.readLine(); // read the server response

            bufferedWriter.close(); // Close bufferedWriter
            outputStream.close(); // Close output stream
            bufferedReader.close(); // Close bufferedReader
            inputStream.close(); // Close inputStream
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
            presenter.writeToReadmetxtAndShowResults(s);
            presenter.showSuccessMessage(presenter.activity.getString(R.string.request_success));
        }
        else
        {
            presenter.showDisplay(presenter.activity.getResources().getString(R.string.not_connected_to_server));
            presenter.showErrorMessage(presenter.activity.getString(R.string.request_error));
        }

        presenter.setReadyState(presenter.connectButton);
    }
}