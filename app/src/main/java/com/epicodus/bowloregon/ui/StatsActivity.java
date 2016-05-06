package com.epicodus.bowloregon.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.epicodus.bowloregon.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StatsActivity extends AppCompatActivity {

    @Bind(R.id.listViewScores) ListView mListViewScores;
    @Bind(R.id.listViewDates) ListView mListViewDates;
    @Bind(R.id.listViewLocations) ListView mListViewLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        ButterKnife.bind(this);

        Intent intent = getIntent();



    }
}
