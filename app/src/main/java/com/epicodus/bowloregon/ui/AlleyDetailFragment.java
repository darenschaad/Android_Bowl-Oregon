package com.epicodus.bowloregon.ui;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.bowloregon.Constants;
import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.models.Alley;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;



/**
 * A simple {@link Fragment} subclass.
 */
public class AlleyDetailFragment extends Fragment implements View.OnClickListener {
    private static final int MAX_WIDTH = 400;
    private static final int MAX_HEIGHT = 300;
    @Bind(R.id.alleyImageView) ImageView mImageLabel;
    @Bind(R.id.alleyNameTextView) TextView mNameLabel;
    @Bind(R.id.categoryTextView) TextView mCategoryLabel;
    @Bind(R.id.ratingTextView) TextView mRatingLabel;
    @Bind(R.id.phoneTextView) TextView mPhoneLabel;
    @Bind(R.id.addressTextView) TextView mAddressLabel;
    @Bind(R.id.saveAlleyButton) Button mSaveAlleyButton;
    @Bind(R.id.websiteTextView) TextView mWebsiteLabel;

    private Alley mAlley;
    private Context mContext;
    private Firebase mFirebaseUserAlleysRef;
    private SharedPreferences mSharedPreferences;
    private ArrayList<String> mUserAlleyIds = new ArrayList<>();

    public static AlleyDetailFragment newInstance(Alley alley) {
        AlleyDetailFragment alleyDetailFragment = new AlleyDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("alley", Parcels.wrap(alley));
        alleyDetailFragment.setArguments(args);
        return alleyDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlley = Parcels.unwrap(getArguments().getParcelable("alley"));
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alley_detail, container, false);
        ButterKnife.bind(this, view);

        Picasso.with(view.getContext())
                .load(mAlley.getImageUrl())
                .resize(MAX_WIDTH, MAX_HEIGHT)
                .centerCrop()
                .into(mImageLabel);
        mNameLabel.setText(mAlley.getName());
        mCategoryLabel.setText(android.text.TextUtils.join(", ", mAlley.getCategories()));
        mRatingLabel.setText(Double.toString(mAlley.getRating()) + "/5");
        mPhoneLabel.setText(mAlley.getPhone());
        mAddressLabel.setText(android.text.TextUtils.join(", ", mAlley.getAddress()));
        mAddressLabel.setOnClickListener(this);
        mPhoneLabel.setOnClickListener(this);
        mSaveAlleyButton.setOnClickListener(this);
        mWebsiteLabel.setOnClickListener(this);

        return view;
    }

    private void openDialog(final Alley alley) {
        final Context mContext = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Track games at " + alley.getName() + " ?");
        builder.setMessage("Saving alley allows you to track your scores.");
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("SAVE ALLEY", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userUid = mSharedPreferences.getString(Constants.KEY_UID, null);
                mFirebaseUserAlleysRef = new Firebase(Constants.FIREBASE_URL_USER_ALLEYS).child(userUid);
                mFirebaseUserAlleysRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mUserAlleyIds.clear();
                        for(DataSnapshot alleySnapshot: dataSnapshot.getChildren()) {
                            HashMap<String, Object> hashMap = (HashMap<String, Object>) alleySnapshot.getValue();
                            Alley alleyCheck = new Alley(hashMap);
                            String alleyId = alleyCheck.getId();
                            mUserAlleyIds.add(alleyId);

                        }
                        if (mUserAlleyIds.contains(alley.getId())){
                            Toast.makeText(mContext, "You already have " + alley.getName() + " saved", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            saveAlleyToFirebase(alley);
                            Toast.makeText(mContext, "You can now save scores to " + alley.getName(), Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, "Alley Was Not Saved", Toast.LENGTH_LONG).show();
            }
        });

        builder.show();
    }

    public void saveAlleyToFirebase(Alley alley) {
        String userUid = mSharedPreferences.getString(Constants.KEY_UID, null);
        final Firebase userAlleyLocation = new Firebase(Constants.FIREBASE_URL_USER_ALLEYS).child(userUid);
        userAlleyLocation.push().setValue(alley);
    }

    @Override
    public void onClick(View v) {
        if (v == mPhoneLabel) {
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + mAlley.getPhone()));
            startActivity(phoneIntent);
        }
        if (v == mAddressLabel) {
            Intent mapIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("geo:" + mAlley.getLatitude()
                            + "," + mAlley.getLongitude()
                            + "?q=(" + mAlley.getName() + ")"));
            startActivity(mapIntent);
        }
        if (v == mSaveAlleyButton) {
            openDialog(mAlley);
//            String userUid = mSharedPreferences.getString(Constants.KEY_UID, null);
//            Firebase userAlleysFirebaseRef = new Firebase(Constants.FIREBASE_URL_ALLEYS).child(userUid);
//            Firebase pushRef = userAlleysFirebaseRef.push();
//            String alleyPushId = pushRef.getKey();
//            mAlley.setPushId(alleyPushId);
//            pushRef.setValue(mAlley);
//            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
        }
        if (v == mWebsiteLabel) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mAlley.getWebsite()));
            startActivity(webIntent);
        }
    }

}
