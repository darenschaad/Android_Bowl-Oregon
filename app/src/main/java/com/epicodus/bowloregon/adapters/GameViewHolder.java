package com.epicodus.bowloregon.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;

import com.epicodus.bowloregon.Constants;
import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.models.Game;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Guest on 5/13/16.
 */
public class GameViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.scoreTextView) TextView mScoreTextView;
    @Bind(R.id.bowlingAlleyNameTextView) TextView mAlleyTextView;
    @Bind(R.id.dateTextView) TextView mDateTextView;
    @Bind(R.id.deleteButton) Button mDeleteButton;

    private Context mContext;
    private ArrayList<Game> mGames = new ArrayList<>();
    private SharedPreferences mSharedPreferences;
    private Game mGame;

    public GameViewHolder(View itemView, ArrayList<Game> games) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        mGames = games;
    }

    public void bindGame(final Game game) {
        Log.d("game alley name", game.getAlleyName() + " null?");
        mGame = game;
        String scoreString = new Double(game.getScore()).toString();
        mScoreTextView.setText(scoreString);
        mAlleyTextView.setText(game.getAlleyName());
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, yyyy");
        mDateTextView.setText(formatter.format(game.getDate()).toString());
        mDeleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Log.d("Click", "It Clicked");
                openDeleteDialog(game);
            }
        });
    }
    private void openDeleteDialog(final Game game) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Remove Item From List");
        builder.setMessage("Are you sure you want to delete this item FOREVER?");
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteItemFromFirebase(game);
                Toast.makeText(mContext.getApplicationContext(), "Deleted forEVER", Toast.LENGTH_SHORT).show();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext.getApplicationContext(), "Phew! That was close!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    public void deleteItemFromFirebase(Game game) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String userUid = mSharedPreferences.getString(Constants.KEY_UID, null);
        String id = game.getPushId();
        String alleyName = game.getAlleyName();
        Firebase savedItemRef = new Firebase(Constants.FIREBASE_URL_GAMES).child(userUid).child(alleyName.replaceAll("\\s", ""));
        Firebase finalItem = savedItemRef.child(id);
        finalItem.removeValue();
    }
}
