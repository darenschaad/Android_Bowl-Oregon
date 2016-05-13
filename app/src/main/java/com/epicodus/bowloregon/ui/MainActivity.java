package com.epicodus.bowloregon.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.epicodus.bowloregon.Constants;
import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.models.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.buttonScores) Button mButtonScores;
    @Bind(R.id.buttonStats) Button mButtonStats;
//    @Bind(R.id.buttonAlleys) Button mButtonAlleys;
    @Bind(R.id.buttonYelp) Button mButtonYelp;
    @Bind(R.id.editTextLocation) EditText mLocationEditText;
    @Bind(R.id.welcomeTextView) TextView mWelcomeTextView;
//    @Bind(R.id.editTextBowlLocation) EditText mEditTextBowlLocation;

    private ValueEventListener mUserRefListener;
    private Firebase mFirebaseRef;
    private Firebase mUserRef;
    private String mUid;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferencesEditor = mSharedPreferences.edit();
        mUid = mSharedPreferences.getString(Constants.KEY_UID, null);
        mUserRef = new Firebase(Constants.FIREBASE_URL_USERS).child(mUid);
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        mButtonScores.setOnClickListener(this);
        mButtonStats.setOnClickListener(this);
//        mButtonAlleys.setOnClickListener(this);
        mButtonYelp.setOnClickListener(this);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
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
                Intent intent = new Intent(MainActivity.this, StatsActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonScores:
                Intent intent1 = new Intent(MainActivity.this, ScoresActivity.class);
                startActivity(intent1);
                break;
//            case R.id.buttonAlleys:
////                String location = mEditTextBowlLocation.getText().toString();
//                Intent intent2 = new Intent(MainActivity.this, MapsActivity.class);
////                intent2.putExtra("location", loc97232ation);
//                startActivity(intent2);
//                break;
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
