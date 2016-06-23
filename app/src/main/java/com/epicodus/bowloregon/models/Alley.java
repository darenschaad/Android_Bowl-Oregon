package com.epicodus.bowloregon.models;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guest on 4/29/16.
 */

@Parcel
public class Alley {
    String name;
    String phone;
    String website;
    double rating;String imageUrl;
    List<String> address = new ArrayList<>();
    double latitude;
    double longitude;
    List<String> categories = new ArrayList<>();
    String pushId;

    public Alley() {}



    public Alley(String name, String phone, String website,
                 double rating, String imageUrl, ArrayList<String> address,
                 double latitude, double longitude, ArrayList<String> categories) {
        this.name = name;
        this.phone = phone;
        this.website = website;
        this.rating = rating;
        this.imageUrl = getLargeImageUrl(imageUrl);
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.categories = categories;
    }



    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return  website;
    }

    public double getRating() {
        return rating;
    }

    public String getImageUrl() { return imageUrl;}

    public String getLargeImageUrl(String imageUrl) {
        String largeImageUrl = imageUrl.substring(0, imageUrl.length() - 6).concat("o.jpg");
        return largeImageUrl;
    }

    public List<String> getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public List<String> getCategories() {
        return categories;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}
