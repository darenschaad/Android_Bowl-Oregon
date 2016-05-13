package com.epicodus.bowloregon.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.models.Game;

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

    private Context mContext;
    private ArrayList<Game> mGames = new ArrayList<>();

    public GameViewHolder(View itemView, ArrayList<Game> games) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        mGames = games;
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int itemPosition = getLayoutPosition();
//                Intent
//            }
//        });
    }

    public void bindGame(Game game) {
        String scoreString = new Double(game.getScore()).toString();
        mScoreTextView.setText(scoreString);
        mAlleyTextView.setText(game.getAlleyName());
        mDateTextView.setText(game.getDate());


    }
}
