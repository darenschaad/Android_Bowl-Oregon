package com.epicodus.bowloregon.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.epicodus.bowloregon.R;
import com.epicodus.bowloregon.models.Alley;

import java.util.ArrayList;

/**
 * Created by Daren on 7/30/2016.
 */
public class SpinnerAdapter extends ArrayAdapter {
    private Context mContext;
    private ArrayList<Alley> mAlleys;

    public SpinnerAdapter(Context context, int layoutResourceId, ArrayList<Alley> alleys) {
        super(context, layoutResourceId, alleys);
        mContext = context;
        mAlleys = alleys;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflter = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView row = (TextView) inflter.inflate(R.layout.spinner_alley, parent, false);
        row.setText(mAlleys.get(position).getName());
        Log.d("Spinner", row + "");
        return row;
    }
    public View getDropDownView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflter = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView row = (TextView) inflter.inflate(R.layout.spinner_alley, parent, false);
        row.setText(mAlleys.get(position).getName());
        return row;
    }

}
