package com.epicodus.bowloregon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import butterknife.Bind;

public class ScoresActivity extends AppCompatActivity {
    public static final String TAG = ScoresActivity.class.getSimpleName();

    @Bind(R.id.editTextScore) EditText mEditTextScore;
    @Bind(R.id.editTextDate) EditText mEditTextDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
    }
}
