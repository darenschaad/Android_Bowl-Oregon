package com.epicodus.bowloregon.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.models.Alley;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

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
    @Bind(R.id.websiteTextView) TextView mWebsiteLabel;
    @Bind(R.id.ratingTextView) TextView mRatingLabel;
    @Bind(R.id.phoneTextView) TextView mPhoneLabel;
    @Bind(R.id.addressTextView) TextView mAddressLabel;
    @Bind(R.id.saveAlleyButton) TextView mSaveAlleyButton;

    private Alley mAlley;

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
        mWebsiteLabel.setText(mAlley.getWebsite());
        mRatingLabel.setText(Double.toString(mAlley.getRating()) + "/5");
        mPhoneLabel.setText(mAlley.getPhone());
        mAddressLabel.setText(android.text.TextUtils.join(", ", mAlley.getAddress()));
        mAddressLabel.setOnClickListener(this);
        mPhoneLabel.setOnClickListener(this);

        return view;
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
    }

}
