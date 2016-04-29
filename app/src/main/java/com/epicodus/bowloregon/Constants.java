package com.epicodus.bowloregon;

import android.os.Build;

/**
 * Created by Guest on 4/29/16.
 */
public class Constants {
    public static final String PLACES_KEY = BuildConfig.PLACES_KEY;

    public static final String PLACES_BASE_URL = "https://";

    public static final String YELP_CONSUMER_KEY = BuildConfig.YELP_CONSUMER_KEY;
    public static final String YELP_CONSUMER_SECRET = BuildConfig.YELP_CONSUMER_SECRET;
    public static final String YELP_TOKEN = BuildConfig.YELP_TOKEN;
    public static final String YELP_TOKEN_SECRET = BuildConfig.YELP_TOKEN_SECRET;

    public static final String YELP_BASE_URL = "https://api.yelp.com/v2/search?term=bowling";
    public static final String YELP_LOCATION_QUERY_PARAMETER = "location";
}
