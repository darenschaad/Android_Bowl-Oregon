package com.epicodus.bowloregon.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.epicodus.bowloregon.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.buttonScores) Button mButtonScores;
    @Bind(R.id.buttonStats) Button mButtonStats;
    @Bind(R.id.buttonAlleys) Button mButtonAlleys;
    @Bind(R.id.editTextBowlLocation) EditText mEditTextBowlLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mButtonScores.setOnClickListener(this);
        mButtonStats.setOnClickListener(this);
        mButtonAlleys.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStats:
                Intent intent = new Intent(MainActivity.this, StatsActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonScores:
                Intent intent1 = new Intent(MainActivity.this, ScoresActivity.class);
                startActivity(intent1);
                break;
            case R.id.buttonAlleys:
                String location = mEditTextBowlLocation.getText().toString();
                Intent intent2 = new Intent(MainActivity.this, MapsActivity.class);
                intent2.putExtra("location", location);
                startActivity(intent2);
            default:
                break;
        }
    }

}
