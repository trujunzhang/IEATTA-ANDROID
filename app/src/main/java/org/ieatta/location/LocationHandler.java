package org.ieatta.location;


import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationHandler implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public interface UpdateLocationListener {
        public void locationOnChanged(Location location);
    }

    private UpdateLocationListener listener;

    public void setUpdateLocationListener(UpdateLocationListener listener) {
        this.listener = listener;
    }

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final Activity activity;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private final Context context;

    public LocationHandler(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }


    public void showUpdateLocation() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    public void onStop() {

    }

    public void onResume() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    public void onPause() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
//        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this.activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
//            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    private void handleNewLocation(Location location) {
        if (this.listener != null)
            this.listener.locationOnChanged(location);


//        double currentLatitude = location.getLatitude();
//        double currentLongitude = location.getLongitude();
//        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

//        MarkerOptions options = new MarkerOptions()
//                .position(latLng)
//                .title("I am here!");
//        mMap.addMarker(options);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }


}
