package com.epicodus.bowloregon;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.epicodus.bowloregon.models.Game;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

/**
 * Created by Daren on 9/20/2016.
 */
public class BaseActivity extends AppCompatActivity {

    public String mUid;
    public String mRecentAddress;
    public SharedPreferences mSharedPreferences;
    public String mActivityTitle;
    public Context mContext;
    public Firebase mFirebaseRef;
    public Firebase mUserRef;
    public SharedPreferences.Editor mSharedPreferencesEditor;
    public double mAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferencesEditor = mSharedPreferences.edit();
        mUid = mSharedPreferences.getString(Constants.KEY_UID, null);
        mUserRef = new Firebase(Constants.FIREBASE_URL_USERS).child(mUid);
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);


    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }





}
