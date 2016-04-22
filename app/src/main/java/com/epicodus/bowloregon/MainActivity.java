package com.epicodus.bowloregon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.buttonScores) Button mButtonScores;
    @Bind(R.id.buttonStats) Button mButtonStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mButtonScores.setOnClickListener(this);
        mButtonStats.setOnClickListener(this);
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
            default:
                break;
        }
    }

}
