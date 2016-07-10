package org.ieatta.activity.maps;


import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.ieatta.IEAApp;
import org.ieatta.R;
import org.ieatta.activity.LeadImage;
import org.ieatta.activity.PageActivity;
import org.wikipedia.activity.ThemedActionBarActivity;

public class MapsActivity extends ThemedActionBarActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MapsActivity";

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private LeadMapView leadMapView;

    private GoogleApiClient mGoogleApiClient;

    public static final String EXTRA_LEADMAPVIEW = "leadmapview";

    private IEAApp app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        leadMapView = getIntent().getParcelableExtra(EXTRA_LEADMAPVIEW);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.handleNewLocation(googleMap);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    private void handleNewLocation(GoogleMap map) {
        // Updates the location and zoom of the MapView
        LatLng latLng = new LatLng(leadMapView.getLatitude(), leadMapView.getLongitude());
        final CameraPosition BONDI =
                new CameraPosition.Builder().target(latLng)
                        .zoom(15.5f)
                        .bearing(300)
                        .tilt(50)
                        .build();

        // The duration must be strictly positive so we make it at least 1.
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(BONDI);
        map.animateCamera(update, null);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(leadMapView.getTitle());
        map.addMarker(options);
    }

    public static void showMaps(Activity activity, LeadMapView leadMapView) {
        Intent galleryIntent = new Intent();
        galleryIntent.setClass(activity, MapsActivity.class);
        galleryIntent.putExtra(EXTRA_LEADMAPVIEW, leadMapView);
        activity.startActivityForResult(galleryIntent, PageActivity.ACTIVITY_REQUEST_MAP);
    }
}
