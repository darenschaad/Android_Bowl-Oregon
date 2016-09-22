package com.epicodus.bowloregon.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.bowloregon.BaseActivity;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener {
    // implement these too for location services maybe GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
//    public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {
    public static final String TAG = MainActivity.class.getSimpleName();
//    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    @Bind(R.id.buttonScores) Button mButtonScores;
    @Bind(R.id.buttonStats) Button mButtonStats;
    @Bind(R.id.buttonYelp) Button mButtonYelp;
    @Bind(R.id.editTextLocation) EditText mLocationEditText;
    @Bind(R.id.welcomeTextView) TextView mWelcomeTextView;
    @Bind(R.id.ballImageView) ImageView mBallImageView;
    @Bind(R.id.pinImageView) ImageView mPinImageView;
    @Bind(R.id.currentAverageTextView) TextView mCurrentAverageTextView;


    private ValueEventListener mUserRefListener;

    public Animation pinFallAnimation;
//
//    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
//    private Location mLastLocation;
//    private GoogleApiClient mGoogleApiClient;
//    private boolean mRequestLocationUpdates = false;
//    private LocationRequest mLocationRequest;
//    private static int UPDATE_INTERVAL = 10000;
//    private static int FASTEST_INTERVAL = 5000;
//    private static int DISPLACEMENT = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mLocationEditText.setOnEditorActionListener(this);

        pinFallAnimation = AnimationUtils.loadAnimation(this, R.anim.pin_animation);

        mButtonScores.setOnClickListener(this);
        mButtonStats.setOnClickListener(this);
        mButtonYelp.setOnClickListener(this);

//        mLocationEditText.setImeActionLabel("SEARCH", KeyEvent.KEYCODE_ENTER);

        mLocationEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.d("Search", "Search button detected");
                    yelpApiFunction();
                    handled = true;
                }
                return handled;
            }
        });

        final GestureDetector detector = new GestureDetector(this, new FlingListener(mBallImageView, this));
        mBallImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (detector.onTouchEvent(motionEvent)) {
                    Log.d("roll", "ball should now roll");
                    mPinImageView.startAnimation(pinFallAnimation);
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



//        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                // Show an expanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {
//
//                // No explanation needed, we can request the permission.
//
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        }
//
//        int permissionCheck = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION);
//
//        if (checkPlayServices()) {
//            buildGoogleApiClient();
//            createLocationRequest();
//        }

//        private void showPermissionDialog() {
//            if (!LocationController.checkPermission(this)) {
//                ActivityCompat.requestPermissions(
//                        this,
//                        new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},
//                        PERMISSION_LOCATION_REQUEST_CODE);
//            }
//        }
    }


    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
              Log.d("Listener", "Listener Fired");
            return true;
        }
        return false;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }
//
//    public static boolean checkPermission(final Context context) {
//        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(mGoogleApiClient != null) {
//            mGoogleApiClient.connect();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        checkPlayServices();
//        if (mGoogleApiClient.isConnected() && mRequestLocationUpdates) {
//            startLocationUpdates();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        stopLocationUpdates();
//    }
//
//    private void displayLocation() {
//        mLastLocation= LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        Log.d("last location", mLastLocation +"");
//        if (mLastLocation != null) {
//            double latitude = mLastLocation.getLatitude();
//            double longitude = mLastLocation.getLongitude();
//
//            mTempTextView.setText(latitude + "," + longitude);
//        }
//        else {
//            mTempTextView.setText("Could not find your location");
//        }
//    }
//
////    private void togglePeriodLocationUpdates() {
////        if (!mRequestLocationUpdates){
////
////        }
////
////    }
//
//    protected synchronized void buildGoogleApiClient(){
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API).build();
//    }
//
//    protected void createLocationRequest(){
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(UPDATE_INTERVAL);
//        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
//    }
//
//    private boolean checkPlayServices() {
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS){
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            }else {
//                Toast.makeText(this,"This Device is not Supported", Toast.LENGTH_LONG).show();
//                finish();
//            }
//            return false;
//        }
//        return true;
//    }
//
//    protected void startLocationUpdates() {
//
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//    }
//
//    protected void stopLocationUpdates() {
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//        displayLocation();
//        if (mRequestLocationUpdates){
//            startLocationUpdates();
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        mLastLocation = location;
//
//        Toast.makeText(getApplicationContext(), "Location Changed!", Toast.LENGTH_SHORT).show();
//
//        displayLocation();
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        Log.i(TAG, "Connection failed: " + connectionResult.getErrorCode());
//    }
//
////    @Override
////    public void enableMyLocation() {
////        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
////                != PackageManager.PERMISSION_GRANTED) {
////            PermissionUtils.requestPermission((FragmentActivity) mContext, LOCATION_PERMISSION_REQUEST_CODE,
////                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
//////        } else if (mMap != null) {
//////            mMap.setMyLocationEnabled(true);
//////        }
////    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
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

    private void yelpApiFunction(){
        String location = mLocationEditText.getText().toString();
        if (!(location.equals(""))) {
            mSharedPreferencesEditor.putString(Constants.PREFERENCES_LOCATION_KEY, location).apply();
        }
        Intent intent3 = new Intent(MainActivity.this, YelpActivity.class);
        intent3.putExtra("location", location);
        startActivity(intent3);
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
                mRecentAddress = mSharedPreferences.getString(Constants.PREFERENCES_LOCATION_KEY, null);
                if (location.equals("") && mRecentAddress == null){
                    Toast.makeText(this, "Please enter zip code or location to search for surrounding bowling alleys", Toast.LENGTH_SHORT).show();
//                    mRequestLocationUpdates = true;

//                    startLocationUpdates();
//                    displayLocation();
                }
                else {
                    yelpApiFunction();
                }

            default:
                break;
        }
    }


}
