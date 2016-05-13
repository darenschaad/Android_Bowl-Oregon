package com.epicodus.bowloregon.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.epicodus.bowloregon.Constants;
import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.adapters.FirebaseGameListAdapter;
import com.epicodus.bowloregon.models.Game;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StatsActivity extends AppCompatActivity {
    private Query mQuery;
    private Firebase mFirebaseGamesRef;
    private FirebaseGameListAdapter mAdapter;
    private SharedPreferences mSharedPreferences;
    @Bind(R.id.recyclerView) RecyclerView mRecylerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mFirebaseGamesRef = new Firebase(Constants.FIREBASE_URL_GAMES);

        setUpFirebaseQuery();
        setUpRecyclerView();

    }

    private void setUpFirebaseQuery() {
        String userUid = mSharedPreferences.getString(Constants.KEY_UID, null);
//        String location = mFirebaseGamesRef.child(userUid).toString();
        String location = mFirebaseGamesRef.toString();
        Log.d("location", location);
        mQuery = new Firebase(location);
    }

    private void setUpRecyclerView() {
        mAdapter = new FirebaseGameListAdapter(mQuery, Game.class);
        mRecylerView.setLayoutManager(new LinearLayoutManager(this));
        mRecylerView.setAdapter(mAdapter);
    }
}
