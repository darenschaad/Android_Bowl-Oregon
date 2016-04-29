package com.epicodus.bowloregon.services;

import com.epicodus.bowloregon.Constants;
import com.epicodus.bowloregon.models.Alley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

/**
 * Created by Guest on 4/29/16.
 */
public class YelpService {
    public static void findAlleys(String location, Callback callback) {
        String CONSUMER_KEY = Constants.YELP_CONSUMER_KEY;
        String CONSUMER_SECRET = Constants.YELP_CONSUMER_SECRET;
        String TOKEN = Constants.YELP_TOKEN;
        String TOKEN_SECRET = Constants.YELP_TOKEN_SECRET;
        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        consumer.setTokenWithSecret(TOKEN, TOKEN_SECRET);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new SigningInterceptor(consumer))
                .build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.YELP_BASE_URL).newBuilder();
        urlBuilder.addQueryParameter(Constants.YELP_LOCATION_QUERY_PARAMETER, location);
        String url = urlBuilder.build().toString();

        Request request= new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public ArrayList<Alley> processResults(Response response) {
        ArrayList<Alley> alleys = new ArrayList<>();
        try {
            String jsonData = response.body().string();
            if (response.isSuccessful()) {
                JSONObject yelpJSON = new JSONObject(jsonData);
                JSONArray businessesJSON = yelpJSON.getJSONArray("businesses");
                for (int i = 0; i < businessesJSON.length(); i++) {
                    JSONObject alleyJSON = businessesJSON.getJSONObject(i);
                    String name = alleyJSON.getString("name");
                    Alley alley = new Alley(name);
                    alleys.add(alley);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
        return alleys;
    }
}
