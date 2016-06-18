package com.epicodus.bowloregon;

import android.os.Build;

/**
 * Created by Guest on 4/29/16.
 */
public class Constants {
    public static final String PLACES_KEY = BuildConfig.PLACES_KEY;

    public static final String KEY_USER_EMAIL = "email";

    public static final String YELP_CONSUMER_KEY = BuildConfig.YELP_CONSUMER_KEY;
    public static final String YELP_CONSUMER_SECRET = BuildConfig.YELP_CONSUMER_SECRET;
    public static final String YELP_TOKEN = BuildConfig.YELP_TOKEN;
    public static final String YELP_TOKEN_SECRET = BuildConfig.YELP_TOKEN_SECRET;
    public static final String PREFERENCES_LOCATION_KEY = "location";

    public static final String YELP_BASE_URL = "https://api.yelp.com/v2/search?term=bowling%20alley";
    public static final String YELP_LOCATION_QUERY_PARAMETER = "location";

    public static final String FIREBASE_URL = BuildConfig.FIREBASE_ROOT_URL;

    public static final String FIREBASE_LOCATION_USERS = "users";
    public static final String FIREBASE_PROPERTY_EMAIL = "email";
    public static final String KEY_UID = "UID";
    public static final String FIREBASE_URL_USERS = FIREBASE_URL + "/" + FIREBASE_LOCATION_USERS;
    public static final String FIREBASE_LOCATION_ALLEYS = "alleys";
    public static final String FIREBASE_URL_ALLEYS = FIREBASE_URL + "/" + FIREBASE_LOCATION_ALLEYS;
    public static final String FIREBASE_LOCATION_GAMES = "games";
    public static final String FIREBASE_URL_GAMES = FIREBASE_URL + "/" + FIREBASE_LOCATION_GAMES;
    public static final String FIREBASE_LOCATION_USER_ALLEYS = "userAlleys";
    public static final String FIREBASE_URL_USER_ALLEYS =  FIREBASE_URL + "/" + FIREBASE_LOCATION_USER_ALLEYS;
}
