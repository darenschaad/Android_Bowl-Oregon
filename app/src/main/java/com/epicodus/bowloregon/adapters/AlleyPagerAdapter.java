package com.epicodus.bowloregon.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.epicodus.bowloregon.models.Alley;
import com.epicodus.bowloregon.ui.AlleyDetailFragment;

import java.util.ArrayList;

public class AlleyPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Alley> mAlleys;

    public AlleyPagerAdapter(FragmentManager fm, ArrayList<Alley> alleys) {
        super(fm);
        mAlleys = alleys;
    }

    @Override
    public Fragment getItem(int position) {
        return AlleyDetailFragment.newInstance(mAlleys.get(position));
    }

    @Override
    public int getCount() {
        return mAlleys.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mAlleys.get(position).getName();
    }
}