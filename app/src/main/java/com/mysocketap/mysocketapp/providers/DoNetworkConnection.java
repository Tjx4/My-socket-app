package com.mysocketap.mysocketapp.providers;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import com.mysocketap.mysocketapp.R;
import com.mysocketap.mysocketapp.presenters.SocketConnectionPresenter;
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
    private String ip = "196.37.22.179";
    private int port = 9011;
    private boolean isSuccessful;
    private Socket socket;
    private SocketConnectionPresenter presenter;

    public DoNetworkConnection(SocketConnectionPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        presenter.setBusyState(presenter.connectButton); // Set the app state to busy
    }

    @Override
    protected String doInBackground(String... params) {
        String response = "";

        try {
            socket = new Socket(ip, port); // Instantiate the socket
            socket.setSoTimeout(10000); // Set the connection timeout
            String encoding = "UTF-8"; // Set the encoding

            // Send the request to the server
            OutputStream outputStream = socket.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, encoding));
            bufferedWriter.write(params[0]);
            bufferedWriter.flush();

            delayProcess(); // Just for this demo, this is just a method to delay in-order to show the progress bar

            // Read the server's response
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, encoding));
            response = bufferedReader.readLine();

            // Close socket and streams
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
            // Write the server response to readme.txt and  also show it on the display plus notify the user that the request was successfull
            presenter.writeToReadmetxtAndShowResults(s);
            presenter.showSuccessMessage(presenter.activity.getString(R.string.request_success));
        }
        else
        {
            // Show not connected text on the display and notify the user that the request failed
            presenter.showDisplay(presenter.activity.getResources().getString(R.string.not_connected_to_server));
            presenter.showErrorMessage(presenter.activity.getString(R.string.request_error));
        }

        presenter.setReadyState(presenter.connectButton); // Set the app state back to ready
    }

    private void delayProcess() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}