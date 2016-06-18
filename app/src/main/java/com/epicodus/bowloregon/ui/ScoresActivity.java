package com.epicodus.bowloregon.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.epicodus.bowloregon.Constants;
import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.adapters.FirebaseAlleyArrayAdapter;
import com.epicodus.bowloregon.models.Alley;
import com.epicodus.bowloregon.models.Game;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ScoresActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = ScoresActivity.class.getSimpleName();
    @Bind(R.id.buttonEnterScores) Button mButtonEnterScores;
    @Bind(R.id.buttonViewStats) Button mButtonViewStats;
    @Bind(R.id.addAlleyButton) Button mAddAlleyButton;
    @Bind(R.id.editTextScore) EditText mEditTextScore;
    @Bind(R.id.editTextDate) EditText mEditTextDate;
    @Bind(R.id.locationEditText) EditText mLocationEditText;

    private SharedPreferences mSharedPreferences;
    private Query mQuery;
    private Firebase mFirebaseAlleysRef;
    private FirebaseAlleyArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mButtonEnterScores.setOnClickListener(this);
        mButtonViewStats.setOnClickListener(this);
        mAddAlleyButton.setOnClickListener(this);
        mFirebaseAlleysRef = new Firebase(Constants.FIREBASE_URL_ALLEYS);
        setUpFirebaseQuery();
//        populateAlleySpinner();
//        addItemsOnSpinner();
    }

    private void setUpFirebaseQuery() {
        String userUid = mSharedPreferences.getString(Constants.KEY_UID, null);
        String location = mFirebaseAlleysRef.child(userUid).toString();
        mQuery = new Firebase(location);
    }

//    public void addItemsOnSpinner() {
////        mAdapter = new FirebaseAlleyArrayAdapter(mQuery, Alley.class);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.alleys_array, android.R.layout.simple_spinner_item);
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        mLocationSpinner.setAdapter(adapter);

//    }

//    private void populateAlleySpinner() {
//        mAdapter = new FirebaseAlleyArrayAdapter(mQuery, Alley.class);
//        ArrayAdapter<CharSequence> adapter = ArrrayAdapter.createFromResource(this, R.array.alleys_array, android.R.layout.simple_spinner_item);
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        mLocationSpinner.setAdapter(adapter);
//    }

    private void createGameInFirebaseHelper(final double score, final String date, final String alleyName) {
        String userUid = mSharedPreferences.getString(Constants.KEY_UID, null);
        final Firebase gameLocation = new Firebase(Constants.FIREBASE_URL_GAMES).child(userUid);
        Game newGame = new Game(score, date, alleyName);
        Firebase gameRef = gameLocation.push();
        String pushId = gameRef.getKey();
        newGame.setPushId(pushId);
        gameRef.setValue(newGame);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonEnterScores:
                double enteredScore = Double.parseDouble(mEditTextScore.getText().toString());
                String enteredDate = mEditTextDate.getText().toString();
                String enteredLocation = mLocationEditText.getText().toString();

//                Game game = new Game(enteredScore, enteredDate, enteredLocation);
//                Firebase userGamesFirebaseRef = new Firebase(Constants.FIREBASE_URL_ALLEYS).child(userUid);
//                Firebase pushRef = userGamesFirebaseRef.push();
//                String alleyPushId = pushRef.getKey();
//                pushRef.setValue(game);
                createGameInFirebaseHelper(enteredScore, enteredDate, enteredLocation);
                Toast.makeText(this, "Game Saved!", Toast.LENGTH_SHORT).show();
                mEditTextScore.setText("");

                break;
            case R.id.buttonViewStats:
                Intent intent1 = new Intent(ScoresActivity.this, StatsActivity.class);
                startActivity(intent1);
                break;
            case R.id.addAlleyButton:
                Intent intent2 = new Intent(ScoresActivity.this, AlleyAddActivity.class);
                startActivity(intent2);
                break;
            default:
                break;

        }
    }
}
