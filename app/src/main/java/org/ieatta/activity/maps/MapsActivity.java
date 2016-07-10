package org.ieatta.activity.maps;


import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ieatta.IEAApp;
import org.ieatta.R;
import org.ieatta.activity.Page;
import org.ieatta.activity.PageActivity;
import org.ieatta.activity.PageTitle;
import org.ieatta.analytics.GalleryFunnel;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.provide.PQueryModelType;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.tasks.DBConvert;
import org.wikipedia.Site;
import org.wikipedia.ViewAnimations;
import org.wikipedia.activity.ActivityUtil;
import org.wikipedia.activity.ThemedActionBarActivity;

import org.wikipedia.theme.Theme;
import org.wikipedia.util.FeedbackUtil;
import org.wikipedia.util.GradientUtil;
import org.wikipedia.views.ViewUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bolts.Continuation;
import bolts.Task;
import io.realm.Realm;
import io.realm.RealmResults;

import static org.wikipedia.util.StringUtil.trim;
import static org.wikipedia.util.UriUtil.handleExternalLink;
import static org.wikipedia.util.UriUtil.resolveProtocolRelativeUrl;

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

        final Toolbar toolbar = (Toolbar) findViewById(R.id.gallery_toolbar);
        // give it a gradient background
        ViewUtil.setBackgroundDrawable(toolbar, GradientUtil.getCubicGradient(
                getResources().getColor(R.color.lead_gradient_start), Gravity.TOP));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

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
