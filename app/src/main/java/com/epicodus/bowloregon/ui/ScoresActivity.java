package com.epicodus.bowloregon.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.bowloregon.Constants;
import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.adapters.FirebaseAlleyArrayAdapter;
import com.epicodus.bowloregon.adapters.SpinnerAdapter;
import com.epicodus.bowloregon.models.Alley;
import com.epicodus.bowloregon.models.Game;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ScoresActivity extends AppCompatActivity implements View.OnClickListener, DatePickerFragment.DatePickDialogListener {
    public static final String TAG = ScoresActivity.class.getSimpleName();
    @Bind(R.id.buttonEnterScores) Button mButtonEnterScores;
    @Bind(R.id.buttonViewStats) Button mButtonViewStats;
    @Bind(R.id.addAlleyButton) Button mAddAlleyButton;
    @Bind(R.id.editTextScore) EditText mEditTextScore;
    @Bind(R.id.dateEditText) TextView mDateEditText;
    @Bind(R.id.locationSpinner) Spinner mLocationSpinner;
    int year_x, month_x, day_x;
    private SharedPreferences mSharedPreferences;
    private Firebase mFirebaseUserAlleysRef;
    private ArrayList<Alley> mUserAlleys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mButtonEnterScores.setOnClickListener(this);
        mButtonViewStats.setOnClickListener(this);
        mAddAlleyButton.setOnClickListener(this);
        String userUid = mSharedPreferences.getString(Constants.KEY_UID, null);
        mFirebaseUserAlleysRef = new Firebase(Constants.FIREBASE_URL_USER_ALLEYS).child(userUid);
        populateAlleySpinner();

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = DatePickerFragment.newInstance(this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void populateAlleySpinner() {
        mFirebaseUserAlleysRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUserAlleys.clear();
                for(DataSnapshot alleySnapshot: dataSnapshot.getChildren()) {
                    Log.d("AlleySnapshot", alleySnapshot.getValue() + "");
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) alleySnapshot.getValue();
                    Alley alley = new Alley(hashMap);
                    Log.d("alleyObject", alley.toString());
                    mUserAlleys.add(alley);

//                    Alley alley = (Alley) alleySnapshot.getValue();
//                    mUserAlleys.add(alleySnapshot.getValue().toString());
//                    mUserAlleys.add(alley.getName() + " - " + alley.getCity());
                }
                SpinnerAdapter adapter = new SpinnerAdapter(ScoresActivity.this, R.layout.spinner_alley, mUserAlleys);
                adapter.setDropDownViewResource(R.layout.spinner_alley);
                mLocationSpinner.setAdapter(adapter);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    private void createGameInFirebaseHelper(final double score, final Date date, final Alley alley) {
        String userUid = mSharedPreferences.getString(Constants.KEY_UID, null);
//        Log.d("test", userUid +"");
//        Log.d("test", alley.getName());
        final Firebase gameLocation = new Firebase(Constants.FIREBASE_URL_GAMES).child(userUid).child(alley.getId().replaceAll("\\s+", ""));

        Game newGame = new Game(score, date, alley.getName(), alley.getId());
        Firebase gameRef = gameLocation.push();
        String pushId = gameRef.getKey();
        newGame.setPushId(pushId);
        gameRef.setValue(newGame);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonEnterScores:
                String scoreString = mEditTextScore.getText().toString();
                String dateString = mDateEditText.getText().toString();

                if (!scoreString.equals("")) {
                    int scoreInt = Integer.parseInt(scoreString);
                    if (scoreInt > 300) {
                        Log.d("scoreInt", scoreInt +"");
                        Toast.makeText(this, "Game could not be saved, you can not score above 300", Toast.LENGTH_SHORT).show();
                    }else if (dateString.equals("Select Date")){
                        Toast.makeText(this, "Game could not be saved, please select a date", Toast.LENGTH_SHORT).show();
                    }else if (mLocationSpinner.getSelectedItem() == null) {
                        Toast.makeText(this, "Game could not be saved, please select a bowling alley", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        double enteredScore = Double.parseDouble(scoreString);
                        Date enteredDate = parseDate(year_x + "-" + month_x + "-" + day_x);
                        Alley alley = (Alley) mLocationSpinner.getSelectedItem();
                        createGameInFirebaseHelper(enteredScore, enteredDate, alley);
                        Toast.makeText(this, "Game Saved!", Toast.LENGTH_SHORT).show();
                        mEditTextScore.setText("");
                    }
                }else {
                    Toast.makeText(this, "Game could not be saved, please enter a valid score", Toast.LENGTH_SHORT).show();
                }
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

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public void onFinishEditDialog(int year, int month, int day) {
        year_x = year;
        month_x = month + 1;
        day_x = day;
        parseDate(year + "-" + month + "-" + day);
        Date enteredDate = parseDate(year_x + "-" + month_x + "-" + day_x);
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, yyyy");
        mDateEditText.setText(formatter.format(enteredDate).toString());
    }
}
