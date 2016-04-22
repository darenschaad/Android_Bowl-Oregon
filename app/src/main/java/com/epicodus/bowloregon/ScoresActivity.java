package com.epicodus.bowloregon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ScoresActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = ScoresActivity.class.getSimpleName();
    @Bind(R.id.buttonEnterScores) Button mButtonEnterScores;
    @Bind(R.id.buttonViewStats) Button mButtonViewStats;

    @Bind(R.id.editTextScore) EditText mEditTextScore;
    @Bind(R.id.editTextDate) EditText mEditTextDate;
    @Bind(R.id.editTextLocation)EditText mEditTextLocation;
    ArrayList<String> enteredScoresArray = new ArrayList<String>();
    ArrayList<String> enteredDatesArray = new ArrayList<String>();
    ArrayList<String> enteredLocationsArray = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        ButterKnife.bind(this);

        mButtonEnterScores.setOnClickListener(this);
        mButtonViewStats.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonEnterScores:
                String enteredScore = mEditTextScore.getText().toString();
                enteredScoresArray.add(enteredScore);
                String enteredDate = mEditTextDate.getText().toString();
                enteredDatesArray.add(enteredDate);
                String enteredLocation = mEditTextLocation.getText().toString();
                enteredLocationsArray.add(enteredLocation);

        }
    }
}
