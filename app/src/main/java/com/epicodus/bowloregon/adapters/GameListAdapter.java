package com.epicodus.bowloregon.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.models.Game;

import java.util.ArrayList;

/**
 * Created by Guest on 5/13/16.
 */
public class GameListAdapter extends RecyclerView.Adapter<GameViewHolder> {
    private ArrayList<Game> mGames = new ArrayList<>();
    private Context mContext;

    public GameListAdapter(Context context, ArrayList<Game> games) {
        mContext = context;
        mGames = games;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_list_item, parent, false);
        GameViewHolder viewHolder = new GameViewHolder(view, mGames);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(GamesViewHolder holder, int position) {
        holder.bindGames(mGamess.get(position));
    }

    @Override
    public int getItemCount() {
        return mGamess.size();
    }
}