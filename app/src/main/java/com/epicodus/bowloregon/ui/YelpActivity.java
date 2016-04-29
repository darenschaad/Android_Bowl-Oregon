package com.epicodus.bowloregon.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.services.YelpService;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class YelpActivity extends AppCompatActivity {
    public static final String TAG = YelpActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yelp);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");
        getAlleys(location);
    }

    private void getAlleys(String location) {
        YelpService.findAlleys(location, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    if (response.isSuccessful()) {
                        Log.v("hello", jsonData);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
