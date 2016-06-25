package com.epicodus.bowloregon.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.bowloregon.Constants;
import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.models.Game;
import com.epicodus.bowloregon.models.User;
import com.epicodus.bowloregon.util.FlingListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//    public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.buttonScores) Button mButtonScores;
    @Bind(R.id.buttonStats) Button mButtonStats;
//    @Bind(R.id.buttonAlleys) Button mButtonAlleys;
    @Bind(R.id.buttonYelp) Button mButtonYelp;
    @Bind(R.id.editTextLocation) EditText mLocationEditText;
    @Bind(R.id.welcomeTextView) TextView mWelcomeTextView;
    @Bind(R.id.ballImageView) ImageView mBallImageView;
    @Bind(R.id.pinImageView) ImageView mPinImageView;
    @Bind(R.id.currentAverageTextView) TextView mCurrentAverageTextView;

    private ValueEventListener mUserRefListener;
    private Firebase mFirebaseRef;
    private Firebase mUserRef;
    private String mUid;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;
    Context mContext;
//    private SensorManager mSensorManager;
//    private Sensor mSensor;
//    private long lastUpdate = 0;
//    private float last_x, last_y, last_z;
//    private static final int SHAKE_THRESHOLD = 1000;
//    private static final String DEBUG_TAG = "Gestures";
//    public Animation pinFallAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        pinFallAnimation = AnimationUtils.loadAnimation(mContext, R.anim.pin_animation);
//        flingAnimation = AnimationUtils.loadAnimation(mContext, R.anim.ball_animation);
//        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        mSensorManager.registerListener(this, mSensor, mSensorManager.SENSOR_DELAY_NORMAL);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferencesEditor = mSharedPreferences.edit();
        mUid = mSharedPreferences.getString(Constants.KEY_UID, null);
        mUserRef = new Firebase(Constants.FIREBASE_URL_USERS).child(mUid);
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        mButtonScores.setOnClickListener(this);
        mButtonStats.setOnClickListener(this);
//        mButtonAlleys.setOnClickListener(this);
        mButtonYelp.setOnClickListener(this);

        final GestureDetector detector = new GestureDetector(this, new FlingListener(mBallImageView, this));
        mBallImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (detector.onTouchEvent(motionEvent)) {
                    Log.d("roll", "ball should now roll");
//                    mPinImageView.startAnimation(pinFallAnimation);
                    return true;
                }
                return false;
            }
        });

       Picasso.with(this)
               .load(R.drawable.ball)
               .into(mBallImageView);

        mUserRefListener = mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mWelcomeTextView.setText("Welcome, " + user.getName());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        final Query returnAllChildNodes = new Firebase(Constants.FIREBASE_URL_GAMES).child(mUid);
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
                double averagePins = round(total/numberOfGamesPlayed, 2);
                mCurrentAverageTextView.setText("Current Average: " + averagePins + "");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_search, menu);
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void logout() {
        mFirebaseRef.unauth();
        takeUserToLoginScreenOnUnAuth();
    }

    private void takeUserToLoginScreenOnUnAuth() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStats:
                Log.d("Stats", "clicked");
                Intent intent = new Intent(MainActivity.this, StatsActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonScores:
                Intent intent1 = new Intent(MainActivity.this, ScoresActivity.class);
                startActivity(intent1);
                break;
            case R.id.buttonYelp:
                String location = mLocationEditText.getText().toString();
                if (!(location.equals(""))) {
                    mSharedPreferencesEditor.putString(Constants.PREFERENCES_LOCATION_KEY, location).apply();
                }
                Intent intent3 = new Intent(MainActivity.this, YelpActivity.class);
                intent3.putExtra("location", location);
                startActivity(intent3);
            default:
                break;
        }
    }

}
