package com.epicodus.bowloregon.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.epicodus.bowloregon.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapsActivity extends AppCompatActivity {
    public static final String TAG = MapsActivity.class.getSimpleName();
    @Bind(R.id.textViewLocation) TextView mTextViewLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        Intent intent2 = getIntent();
        String location = intent2.getStringExtra("location");
        mTextViewLocation.setText("Here are all of the bowling alleys near: " + location);
    }
}
