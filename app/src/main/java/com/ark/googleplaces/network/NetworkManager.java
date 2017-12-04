package com.ark.googleplaces.network;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ark.googleplaces.R;
import com.ark.googleplaces.controller.PlaceAdapter;
import com.ark.googleplaces.model.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NetworkManager {

    private static final String TAG = "NetworkManager";
    private static final String PLACES_BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

    private Context mContext;
    private final PlaceAdapter mAdapter;
    private String API_KEY;
    private ArrayList<Place> mPlaces = new ArrayList<>();


    public NetworkManager(Context context, PlaceAdapter adapter){
        mContext = context.getApplicationContext();
        mAdapter = adapter;
        API_KEY = mContext.getString(R.string.GOOGLE_API_KEY);
    }

    public void getNearbyPlaces(Location location, int radius, String keyword){
        String url = PLACES_BASE_URL +
                "location=" +location.getLatitude()+ ","+ location.getLongitude()+
                "&radius=" + radius +
                "&keyword=" + keyword +
                "&key=" + API_KEY;
        makeRequest(url);
    }

    public void getNearbyRestaurants(Location location, int radius){
        Log.d(TAG, "lat: " + location.getLatitude() + ", long: " + location.getLongitude());
        String url = PLACES_BASE_URL  +
                "type=restaurant" +
                "&location=" +location.getLatitude()+ ","+ location.getLongitude()+
                "&radius=" + radius +
                "&key=" + API_KEY;
        makeRequest(url);
    }

    private void refreshPlaces(List<Place> places){
        mPlaces.clear();
        mPlaces.addAll(places);
        mAdapter.setDataset(mPlaces);
    }

    private void makeRequest(String url){
        Log.d(TAG, "Url: " + url);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            List<Place> places = new ArrayList<>();
                            Log.d(TAG, "no of results: " + results.length());
                            if(results.length() > 0) {
                                for (int i = 0; i < results.length(); i++) {
                                    JSONObject googlePlace = results.getJSONObject(i);
                                    places.add(new Place(googlePlace.getString("id"),
                                            googlePlace.getString("name"),
                                            googlePlace.getString("icon"),
                                            googlePlace.getString("vicinity"),
                                            googlePlace.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                                            googlePlace.getJSONObject("geometry").getJSONObject("location").getDouble("lng")));
                                }
                            }
                            refreshPlaces(places);
                        } catch (JSONException e) {
                            Log.e(TAG, "Error occurred while getting places", e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
}
