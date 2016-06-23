package com.epicodus.bowloregon.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.adapters.AlleyPagerAdapter;
import com.epicodus.bowloregon.models.Alley;
import com.epicodus.bowloregon.util.ScaleAndFadePageTransformer;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AlleyDetailActivity extends AppCompatActivity {
    @Bind(R.id.viewPager) ViewPager mViewPager;
    private AlleyPagerAdapter adapterViewPager;
    ArrayList<Alley> mAlleys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alley_detail);
        ButterKnife.bind(this);
        mAlleys = Parcels.unwrap(getIntent().getParcelableExtra("alleys"));
        int startingPosition = Integer.parseInt(getIntent().getStringExtra("position"));
        adapterViewPager = new AlleyPagerAdapter(getSupportFragmentManager(), mAlleys);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);
        mViewPager.setPageTransformer(true, new ScaleAndFadePageTransformer());
    }
}
