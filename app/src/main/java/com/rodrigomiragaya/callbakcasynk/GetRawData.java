package com.rodrigomiragaya.callbakcasynk;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus { IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK }


//Class that get the data from a website x

//AsyncTask<String(Param), Integer(Progress), String(Result)>
public class GetRawData extends AsyncTask<String, Integer, String> {
    private static final String TAG = "GetRawData";


    //create the variable CallBack with the name of the interface
    private final OnDownloadComplete mCallback;
    private DownloadStatus mDownloadStatus;

    //create an interface to apply to class to return
    interface OnDownloadComplete {
        void onDownloadCompleteMethod(String downloadedData, DownloadStatus mDownloadStatus);
    }

    public GetRawData(OnDownloadComplete callback) {
        this.mDownloadStatus = DownloadStatus.IDLE;
        this.mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute: call");
    }


    //metod to get all the info (html) from a website
    //can use to get json if the web is json aprove
    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: call");
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if(strings == null) {
            mDownloadStatus = DownloadStatus.NOT_INITIALISED;
            return null;
        }

        try {
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code was " + response);

            StringBuilder result = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

//            String line;
//            while(null != (line = reader.readLine())) {
            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                result.append(line).append("\n");
            }

            mDownloadStatus = DownloadStatus.OK;
            return result.toString();


        } catch(MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid URL " + e.getMessage() );
        } catch(IOException e) {
            Log.e(TAG, "doInBackground: IO Exception reading data: " + e.getMessage() );
        } catch(SecurityException e) {
            Log.e(TAG, "doInBackground: Security Exception. Needs permission? " + e.getMessage());
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch(IOException e) {
                    Log.e(TAG, "doInBackground: Error closing stream " + e.getMessage() );
                }
            }
        }

        mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        // to show the proggres of the task

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: call");

        if(mCallback != null) {
            mCallback.onDownloadCompleteMethod(s, this.mDownloadStatus);
        }
    }


}
