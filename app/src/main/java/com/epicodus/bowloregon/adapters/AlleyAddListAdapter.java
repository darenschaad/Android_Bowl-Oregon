package com.epicodus.bowloregon.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.bowloregon.Constants;
import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.models.Alley;
import com.epicodus.bowloregon.ui.AlleyAddActivity;
import com.epicodus.bowloregon.ui.ScoresActivity;
import com.epicodus.bowloregon.util.ItemTouchHelperViewHolder;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daren on 6/18/2016.
 */
public class AlleyAddListAdapter extends RecyclerView.Adapter<AlleyAddListAdapter.AlleyViewHolder> {
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;
    private ArrayList<Alley> mAlleys = new ArrayList<>();
    private Query mQuery;
    private Context mContext;
    private Firebase mFirebaseScoreAlleysRef;
    private Firebase mFirebaseUserAlleysRef;
    private SharedPreferences mSharedPreferences;
    private ArrayList<String> mUserAlleyIds = new ArrayList<>();

    public AlleyAddListAdapter(Context context, ArrayList<Alley> alleys) {
        mContext = context;
        mAlleys = alleys;
    }

    @Override
    public AlleyAddListAdapter.AlleyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alley_add_list_item, parent, false);
        AlleyViewHolder viewHolder = new AlleyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AlleyAddListAdapter.AlleyViewHolder holder, int position) {
        holder.bindAlley(mAlleys.get(position));
    }

    @Override
    public int getItemCount() {
        return mAlleys.size();
    }

    public class AlleyViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        @Bind(R.id.alleyImageView)
        ImageView mAlleyImageView;
        @Bind(R.id.alleyNameTextView) TextView mNameTextView;
        private Context mContext;


        @Override
        public void onItemSelected() {
            itemView.animate()
                    .alpha(0.7f)
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setDuration(500);
        }

        @Override
        public void onItemClear() {
            itemView.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f);
        }
        public AlleyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            mFirebaseScoreAlleysRef = new Firebase(Constants.FIREBASE_URL_USER_ALLEYS);
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int itemPosition = getLayoutPosition();
                    openDialog(mAlleys.get(itemPosition));
                }
            });
        }

        private void openDialog(final Alley alley) {
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




        public void bindAlley(Alley alley) {
            Picasso.with(mContext)
                    .load(alley.getImageUrl())
                    .resize(MAX_WIDTH, MAX_HEIGHT)
                    .centerCrop()
                    .into(mAlleyImageView);
            mNameTextView.setText(alley.getName());
        }

        public void saveAlleyToFirebase(Alley alley) {
            String userUid = mSharedPreferences.getString(Constants.KEY_UID, null);
            final Firebase userAlleyLocation = new Firebase(Constants.FIREBASE_URL_USER_ALLEYS).child(userUid);
            userAlleyLocation.push().setValue(alley);
            mContext.startActivity(new Intent(mContext, ScoresActivity.class));
            return;
        }
    }
}

