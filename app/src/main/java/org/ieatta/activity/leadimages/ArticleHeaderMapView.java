package org.ieatta.activity.leadimages;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

//import com.mapbox.mapboxsdk.annotations.Marker;
//import com.mapbox.mapboxsdk.annotations.MarkerOptions;
//import com.mapbox.mapboxsdk.camera.CameraPosition;
//import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
//import com.mapbox.mapboxsdk.constants.Style;
//import com.mapbox.mapboxsdk.geometry.LatLng;
//import com.mapbox.mapboxsdk.maps.MapView;
//import com.mapbox.mapboxsdk.maps.MapboxMap;
//import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.ieatta.R;
import org.ieatta.activity.maps.LeadMapView;
import org.ieatta.analytics.PageFragmentFunnel;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArticleHeaderMapView extends FrameLayout {
    public interface OnLeadMapViewListener {
        void onMapViewClick(MapView mapView);
    }

    private OnLeadMapViewListener listener;

    public void setListener(OnLeadMapViewListener listener) {
        this.listener = listener;
    }

    @Bind(R.id.mapview)
    MapView mapView;

    public MapView getMapView() {
        return mapView;
    }

    public ArticleHeaderMapView(Context context) {
        super(context);
        init();
    }

    public ArticleHeaderMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArticleHeaderMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ArticleHeaderMapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void toggleMapView(boolean activated, LeadMapView leadMapView) {
        new PageFragmentFunnel().logMapViewActivated(activated);
        setVisibility(GONE);
        if (activated) {
            setVisibility(VISIBLE);
            this.load(leadMapView);
        }
    }

    public void load(@Nullable final LeadMapView leadMapView) {
        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                map.getUiSettings().setMyLocationButtonEnabled(false);

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
        });
    }


    public void setAnimationPaused(boolean paused) {
//        if (image.getController() != null && image.getController().getAnimatable() != null) {
//            if (paused) {
//                image.getController().getAnimatable().stop();
//            } else {
//                image.getController().getAnimatable().start();
//            }
//        }
    }

    public boolean hasImage() {
        return getVisibility() != GONE;
    }

    public void setFocusOffset(float verticalOffset) {
        final float centerHorizontal = 0.5f;
//        image.getHierarchy().setActualImageFocusPoint(new PointF(centerHorizontal, verticalOffset));
    }

    private void init() {
        setVisibility(GONE);

        // Clip the Ken Burns zoom animation applied to the image.
        setClipChildren(true);

        inflate();
        bind();

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ArticleHeaderMapView.this.listener != null) {
                    ArticleHeaderMapView.this.listener.onMapViewClick(ArticleHeaderMapView.this.mapView);
                }
            }
        });
    }

    private void inflate() {
        inflate(getContext(), R.layout.view_article_header_map, this);
    }

    private void bind() {
        ButterKnife.bind(this);
    }

    private void handleNewLocation(Location location) {
//        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
//        mMap.addMarker(options);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}