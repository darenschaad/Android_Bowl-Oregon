package com.epicodus.bowloregon.models;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    String id;
    String pushId;

    public Alley() {}

    public Alley(Map<String, Object> map){
        name = (String) map.get("name");
        phone = (String) map.get("phone");
        website = (String) map.get("website");
        rating = (double) map.get("rating");
        address = (List<String>) map.get("address");
        latitude = (double) map.get("latitude");
        longitude = (double) map.get("longitude");
        categories = (List<String>) map.get("categories");
        id = (String) map.get("id");

    }

    public Alley(String name, String phone, String website,
                 double rating, String imageUrl, ArrayList<String> address,
                 double latitude, double longitude, ArrayList<String> categories, String id) {
        this.name = name;
        this.phone = phone;
        this.website = website;
        this.rating = rating;
        this.imageUrl = getLargeImageUrl(imageUrl);
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.categories = categories;
        this.id = id;

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

    public String getId() { return id; }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}
