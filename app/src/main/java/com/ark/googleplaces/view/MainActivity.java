package com.ark.googleplaces.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ark.googleplaces.locationservices.LocationServicesManager;
import com.ark.googleplaces.network.NetworkManager;
import com.ark.googleplaces.R;
import com.ark.googleplaces.controller.PlaceAdapter;
import com.ark.googleplaces.model.Place;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "MainActivity";
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1988;

    /**
     * Use KEYWORD and RADIUS to play with the search results
     * Did not implemented UI to take in these, due to time constraints
     */
    final String KEYWORD = "";//for google places search
    final int RADIUS = 10000;//in meters

    private RecyclerView recyclerView;
    private PlaceAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private LocationServicesManager mLocationServicesManager;
    private NetworkManager mNetworkManager;
    private LocationCallback mLocationCallback;

    //For OnClick functionality in PlaceAdapter
    public interface OnItemClickListener{
        void onItemClick(Place place);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //RecyclerView and Adapter
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PlaceAdapter(this, new ArrayList<Place>(), new OnItemClickListener(){
            @Override
            public void onItemClick(Place place) {
                Double lat = place.getLat();
                Double lon = place.getLon();
                int zoom = 19;//zoom range 0-->21
                Uri gmmIntentUri = Uri.parse("geo:" +lat+ "," +lon + "?z=" + zoom);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        recyclerView.setAdapter(mAdapter);

        //LocationServices and Network Handling
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d(TAG, "onLocationResult");
                //get current location
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    mNetworkManager.getNearbyRestaurants(location, RADIUS);
                    //Or call mNetworkManager.getNearbyPlaces(location, RADIUS, KEYWORD);
                }
            }
        };
        mLocationServicesManager = new LocationServicesManager(this);
        mNetworkManager = new NetworkManager(getApplicationContext(), mAdapter);
        startLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Log.d(TAG, "Permissions granted");
                    startLocationUpdates();
                } else {
                    // permission denied
                }
                return;
            }
        }
    }

    private void startLocationUpdates(){
        mLocationServicesManager.startLocationUpdates(mLocationCallback);
    }
}
