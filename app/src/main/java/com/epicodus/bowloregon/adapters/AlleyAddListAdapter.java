package com.epicodus.bowloregon.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.models.Alley;
import com.epicodus.bowloregon.ui.AlleyDetailActivity;
import com.epicodus.bowloregon.util.ItemTouchHelperViewHolder;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Daren on 6/18/2016.
 */
public class AlleyAddListAdapter extends RecyclerView.Adapter<AlleyAddListAdapter.AlleyViewHolder> {
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;
    private ArrayList<Alley> mAlleys = new ArrayList<>();
    private Context mContext;

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
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int itemPosition = getLayoutPosition();
                    Intent intent = new Intent(mContext, AlleyDetailActivity.class);
                    intent.putExtra("position", itemPosition + "");
                    intent.putExtra("alleys", Parcels.wrap(mAlleys));
                    mContext.startActivity(intent);
                }
            });
        }



        public void bindAlley(Alley alley) {
            Picasso.with(mContext)
                    .load(alley.getImageUrl())
                    .resize(MAX_WIDTH, MAX_HEIGHT)
                    .centerCrop()
                    .into(mAlleyImageView);
            mNameTextView.setText(alley.getName());
        }
    }
}

