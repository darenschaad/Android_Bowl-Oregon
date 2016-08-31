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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.epicodus.bowloregon.Constants;
import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.adapters.FirebaseGameListAdapter;
import com.epicodus.bowloregon.adapters.SpinnerAdapter;
import com.epicodus.bowloregon.models.Alley;
import com.epicodus.bowloregon.models.Game;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StatsActivity extends AppCompatActivity {
    private Query mQuery;
    private Firebase mFirebaseGamesRef;
    private ValueEventListener mGameRefListener;
    private String mUId;
    private FirebaseGameListAdapter mAdapter;
    private Adapter mReverseAdapter;
    private SharedPreferences mSharedPreferences;
    private LinearLayoutManager mLayoutManager;
    @Bind(R.id.recyclerView) RecyclerView mRecylerView;
    @Bind(R.id.averageTextView) TextView mAverageTextView;
    @Bind(R.id.averageAlleySpinner) Spinner mAverageAlleySpinner;
    @Bind(R.id.averageByAlleyTextView) TextView mAverageByAlleyTextView;

    private Firebase mFirebaseUserAlleysRef;
    private ArrayList<Alley> mUserAlleys = new ArrayList<>();

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
                mAverageTextView.setText("Lifetime Average: " + averagePins + "");
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
//                    Log.d("AlleySnapshot", alleySnapshot.getValue() + "");
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) alleySnapshot.getValue();
                    Alley alley = new Alley(hashMap);
//                    Log.d("alleyObject", alley.toString());
                    mUserAlleys.add(alley);

//                    Alley alley = (Alley) alleySnapshot.getValue();
//                    mUserAlleys.add(alleySnapshot.getValue().toString());
//                    mUserAlleys.add(alley.getName() + " - " + alley.getCity());
                }
                SpinnerAdapter adapter = new SpinnerAdapter(StatsActivity.this, R.layout.spinner_alley, mUserAlleys);
                adapter.setDropDownViewResource(R.layout.spinner_alley);
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
        Alley alley = (Alley) mAverageAlleySpinner.getSelectedItem();
        String location = mFirebaseGamesRef.child(userUid).child(alley.getId().replaceAll("\\s", "")).toString();

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
                Log.d("Games Played", numberOfGamesPlayed +"");
                if (numberOfGamesPlayed == 0){
                    mAverageByAlleyTextView.setText("No Scores Recorded");
                }else{
                    double averagePins = round(total/numberOfGamesPlayed, 2);
                    Alley alley = (Alley) mAverageAlleySpinner.getSelectedItem();
                    mAverageByAlleyTextView.setText("Ally Average : " + averagePins);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    private void setUpRecyclerView() {
        mAdapter = new FirebaseGameListAdapter(mQuery.orderByChild("date"), Game.class);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecylerView.setLayoutManager(mLayoutManager);
        mRecylerView.setAdapter(mAdapter);
    }
}
