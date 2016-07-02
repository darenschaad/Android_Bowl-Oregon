package com.epicodus.bowloregon.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.epicodus.bowloregon.Constants;
import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.adapters.FirebaseGameListAdapter;
import com.epicodus.bowloregon.models.Game;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StatsActivity extends AppCompatActivity {
    private Query mQuery;
    private Firebase mFirebaseGamesRef;
    private ValueEventListener mGameRefListener;
    private String mUId;
    private FirebaseGameListAdapter mAdapter;
    private SharedPreferences mSharedPreferences;
    @Bind(R.id.recyclerView) RecyclerView mRecylerView;
    @Bind(R.id.averageTextView) TextView mAverageTextView;
    @Bind(R.id.averageAlleySpinner) Spinner mAverageAlleySpinner;
    @Bind(R.id.averageByAlleyTextView) TextView mAverageByAlleyTextView;

    private Firebase mFirebaseUserAlleysRef;
    private ArrayList<String> mUserAlleys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userUid = mSharedPreferences.getString(Constants.KEY_UID, null);
        mUId = mSharedPreferences.getString(Constants.KEY_UID, null);
        mFirebaseGamesRef = new Firebase(Constants.FIREBASE_URL_GAMES);
        mFirebaseUserAlleysRef = new Firebase(Constants.FIREBASE_URL_USER_ALLEYS).child(userUid);
        populateAlleySpinner();

        final Query returnAllChildNodes = new Firebase(Constants.FIREBASE_URL_GAMES).child(mUId);
        returnAllChildNodes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double numberOfGamesPlayed = 0;
                double total = 0;
                Iterable<DataSnapshot> alleysPlayedAt = dataSnapshot.getChildren();

                for (DataSnapshot data : alleysPlayedAt) {
                    Iterable<DataSnapshot> gamesPlayed = data.getChildren();
                    for (DataSnapshot gameData : gamesPlayed) {
                        Game game = gameData.getValue(Game.class);
                        double totalPins = game.getScore();
                        total += totalPins;
                        numberOfGamesPlayed ++;
                    }
                }
                double averagePins = round(total/numberOfGamesPlayed, 3);
                mAverageTextView.setText("Current Total Average: " + averagePins + "");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        mAverageAlleySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setUpFirebaseQuery();
                setUpRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    private void populateAlleySpinner() {
        mFirebaseUserAlleysRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUserAlleys.clear();
                for(DataSnapshot alleySnapshot: dataSnapshot.getChildren()) {
                    mUserAlleys.add(alleySnapshot.getValue().toString());
                }
                ArrayAdapter adapter = new ArrayAdapter(StatsActivity.this, android.R.layout.simple_spinner_item, mUserAlleys);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                mAverageAlleySpinner.setAdapter(adapter);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private void setUpFirebaseQuery() {
        String userUid = mSharedPreferences.getString(Constants.KEY_UID, null);
        String location = mFirebaseGamesRef.child(userUid).child(mAverageAlleySpinner.getSelectedItem().toString().replaceAll("\\s", "")).toString();
        mQuery = new Firebase(location);
        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double numberOfGamesPlayed = 0;
                Iterable<DataSnapshot> gamesPlayed = dataSnapshot.getChildren();
                double total = 0;

                for (DataSnapshot data : gamesPlayed) {
                    Game game = data.getValue(Game.class);
                    double totalPins = game.getScore();
                    total += totalPins;
                    numberOfGamesPlayed ++;
                }
                double averagePins = round(total/numberOfGamesPlayed, 2);
                mAverageByAlleyTextView.setText("Current Average at " + mAverageAlleySpinner.getSelectedItem().toString() + ": " + averagePins);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    private void setUpRecyclerView() {
        mAdapter = new FirebaseGameListAdapter(mQuery.orderByChild("date"), Game.class);
        mRecylerView.setLayoutManager(new LinearLayoutManager(this));
        mRecylerView.setAdapter(mAdapter);
    }
}
