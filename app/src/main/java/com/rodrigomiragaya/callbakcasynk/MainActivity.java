package com.rodrigomiragaya.callbakcasynk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

//implements the interface created on the class that get the data
public class MainActivity extends AppCompatActivity implements GetRawData.OnDownloadComplete {
    private static final String TAG = "MainActivity";

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.text1);

        //execute the GetRawData Async and pass the callback
        GetRawData getRawData = new GetRawData(this);
        getRawData.execute("https://www.preciosclaros.gob.ar/#!/buscar-productos");
        Log.d(TAG, "onCreate: getRawData.execute");

    }

    @Override
    public void onDownloadCompleteMethod(String downloadedData, DownloadStatus mDownloadStatus) {
        Log.d(TAG, "onDownloadComplete: on Main call");
        if (mDownloadStatus == DownloadStatus.OK){
            System.out.println("Info descargada: " + downloadedData);
            text.setText(downloadedData);
        } else {
            //Download processing fail
            System.out.println("Download Fail");
        }
    }

    
}
