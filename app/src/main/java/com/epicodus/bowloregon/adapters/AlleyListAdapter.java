package com.epicodus.bowloregon.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.models.Alley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Guest on 4/29/16.
 */
public class AlleyListAdapter extends RecyclerView.Adapter<AlleyListAdapter.AlleyViewHolder> {
    private ArrayList<Alley> mAlleys = new ArrayList<>();
    private Context mContext;

    public AlleyListAdapter(Context context, ArrayList<Alley> alleys) {
        mContext = context;
        mAlleys = alleys;
    }

    @Override
    public AlleyListAdapter.AlleyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alley_list_item, parent, false);
        AlleyViewHolder viewHolder = new AlleyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AlleyListAdapter.AlleyViewHolder holder, int position) {
        holder.bindAlley(mAlleys.get(position));
    }

    @Override
    public int getItemCount() {
        return mAlleys.size();
    }

    public class AlleyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.alleyImageView)
        ImageView mAlleyImageView;
        @Bind(R.id.alleyNameTextView) TextView mNameTextView;
        @Bind(R.id.categoryTextView) TextView mCategoryTextView;
        @Bind(R.id.ratingTextView)
        TextView mRatingTextView;
        private Context mContext;

        public AlleyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }

        public void bindAlley(Alley alley) {
            Picasso.with(mContext).load(alley.getImageUrl()).into(mAlleyImageView);
            mNameTextView.setText(alley.getName());
            mCategoryTextView.setText(alley.getPhone());
            mRatingTextView.setText("Rating: " + alley.getRating() + "/5");
        }
    }
}
