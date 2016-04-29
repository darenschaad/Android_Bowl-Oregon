package com.epicodus.bowloregon.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;

import android.widget.ListView;

import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.adapters.AlleyListAdapter;
import com.epicodus.bowloregon.models.Alley;
import com.epicodus.bowloregon.services.YelpService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class YelpActivity extends AppCompatActivity {
    public static final String TAG = YelpActivity.class.getSimpleName();
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    private AlleyListAdapter mAdapter;

    public ArrayList<Alley> mAlleys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yelp);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");

        getAlleys(location);
    }

    private void getAlleys(String location) {
        final YelpService yelpService = new YelpService();

        YelpService.findAlleys(location, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
               mAlleys = yelpService.processResults(response);
                    Log.d("size", String.valueOf(mAlleys.size()));
                YelpActivity.this.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        mAdapter = new AlleyListAdapter(getApplicationContext(), mAlleys);
                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager =
                                new LinearLayoutManager(YelpActivity.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);
                    }
                });
            }
        });
    }


}
