package com.epicodus.bowloregon.models;

import java.util.ArrayList;

/**
 * Created by Guest on 4/29/16.
 */
public class Alley {
    private String mName;
    private String mPhone;
    private String mWebsite;
    private double mRating;
    private String mImageUrl;
    private ArrayList<String> mAddress = new ArrayList<>();
    private double mLatitude;
    private double mLongitude;



    public Alley(String name, String phone, String website,
                 double rating, String imageUrl, ArrayList<String> address,
                 double latitude, double longitude){
        this.mName = name;
        this.mPhone = phone;
        this.mWebsite = website;
        this.mRating = rating;
        this.mImageUrl = imageUrl;
        this.mAddress = address;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public String getName() {
        return mName;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getWebsite() {
        return  mWebsite;
    }

    public double getRating() {
        return mRating;
    }

    public String getImageUrl(){
        return mImageUrl;
    }

    public ArrayList<String> getAddress() {
        return mAddress;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }
}
