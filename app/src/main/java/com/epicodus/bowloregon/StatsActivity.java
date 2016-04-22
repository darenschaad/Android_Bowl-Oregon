package com.epicodus.bowloregon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        Intent intent1 = getIntent();
        ArrayList<String> scores = intent1.getStringArrayListExtra("enteredScoresArray");
        ArrayList<String> dates = intent1.getStringArrayListExtra("enteredDatesArray");
        ArrayList<String> locations = intent1.getStringArrayListExtra("enteredLocationsArray");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, scores);
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dates);
        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, locations);
        mListViewScores.setAdapter(adapter);
        mListViewDates.setAdapter(adapter1);
        mListViewLocations.setAdapter(adapter2);
    }
}
