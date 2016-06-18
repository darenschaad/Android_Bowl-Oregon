package com.epicodus.bowloregon.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.adapters.AlleyAddListAdapter;
import com.epicodus.bowloregon.models.Alley;
import com.epicodus.bowloregon.services.YelpService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AlleyAddActivity extends AppCompatActivity implements View.OnClickListener {
        @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
        @Bind(R.id.searchButton) Button mSearchButton;
        @Bind(R.id.searchEditText) EditText mSearchEditText;
        private AlleyAddListAdapter mAdapter;


        public ArrayList<Alley> mAlleys = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alley_add);
        ButterKnife.bind(this);
        mSearchButton.setOnClickListener(this);



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
                AlleyAddActivity.this.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        mAdapter = new AlleyAddListAdapter(getApplicationContext(), mAlleys);
                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager =
                                new LinearLayoutManager(AlleyAddActivity.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.searchButton:
                String location = mSearchEditText.getText().toString();
                getAlleys(location);
                break;
        }
    }
}
