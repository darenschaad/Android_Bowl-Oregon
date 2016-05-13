package com.epicodus.bowloregon.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.models.Game;
import com.epicodus.bowloregon.util.FirebaseRecyclerAdapter;
import com.firebase.client.Query;

/**
 * Created by Guest on 5/13/16.
 */
public class FirebaseGameListAdapter extends FirebaseRecyclerAdapter<GameViewHolder, Game> {

    public FirebaseGameListAdapter(Query query, Class<Game> itemClass) {
        super(query, itemClass);
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_list_item, parent, false);
        return new GameViewHolder(view, getItems());
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {
        holder.bindGame(getItem(position));
    }

    @Override
    protected void itemAdded(Game item, String key, int position) {

    }

    @Override
    protected void itemChanged(Game oldItem, Game newItem, String key, int position) {

    }

    @Override
    protected void itemRemoved(Game item, String key, int position) {

    }

    @Override
    protected void itemMoved(Game item, String key, int oldPosition, int newPosition) {

    }
}
