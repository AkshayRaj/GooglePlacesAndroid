package com.ark.googleplaces.locationservices;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.ark.googleplaces.view.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationServicesManager {

    private static final String TAG = "LocationServicesManager";
    private Activity mActivity;
    private FusedLocationProviderClient mFusedLocationClient;

    public LocationServicesManager(Activity activity) {
        mActivity = activity;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity);
    }

    public void startLocationUpdates(final LocationCallback locationCallback) {
        Log.d(TAG, "startLocationUpdates");
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(30 * 1000);//10s
        locationRequest.setFastestInterval(5 * 1000);//5s
        locationRequest.setSmallestDisplacement(5f);//5 meters
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest mLocationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(mActivity);
        settingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(mActivity, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            //don't have location permissions yet, so prompt user
                            Log.d(TAG, "Do not have location permissions");
                            ActivityCompat.requestPermissions(mActivity,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MainActivity.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                            return;
                        }
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */);
                    }
                });
    }

}
