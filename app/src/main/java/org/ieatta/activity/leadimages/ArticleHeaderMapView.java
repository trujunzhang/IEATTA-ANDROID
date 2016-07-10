package org.ieatta.activity.leadimages;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.location.Location;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.ieatta.R;
import org.ieatta.activity.LeadImage;
import org.ieatta.activity.LeadMapView;
import org.ieatta.activity.MapInfo;
import org.ieatta.analytics.PageFragmentFunnel;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArticleHeaderMapView extends FrameLayout {
    @Bind(R.id.mapview)
    MapView mapView;

    //    private Marker lastMarker;
//
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

    public void toggleMapView(boolean activated, MapInfo mapInfo) {
        new PageFragmentFunnel().logMapViewActivated(activated);
        setVisibility(GONE);
        if (activated)
            setVisibility(VISIBLE);
    }

    public void load(@Nullable final LeadMapView leadMapView) {
        if (leadMapView == null) {
            setVisibility(GONE);
        } else {
            setVisibility(GONE);
//            mapView.getMapAsync(new OnMapReadyCallback() {
//                @Override
//                public void onMapReady(MapboxMap mapboxMap) {
//
//                    // Set map style
//                    mapboxMap.setStyleUrl(Style.MAPBOX_STREETS);
//
//                    // Set the camera's starting position
//                    LatLng location = new LatLng(leadMapView.getLatitude(), leadMapView.getLongitude());
//                    CameraPosition cameraPosition = new CameraPosition.Builder()
//                            .target(location) // set the camera's center position
//                            .zoom(14)  // set the camera's zoom level
//                            .tilt(20)  // set the camera's tilt
//                            .build();
//
//                    // Move the camera to that position
//                    mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//                    new PageFragmentFunnel().logMapboxMarker(leadMapView.getTitle(), leadMapView.getSnippet());
//
//                    MarkerOptions options = new MarkerOptions()
//                            .position(location)
//                            .title(leadMapView.getTitle())
//                            .snippet(leadMapView.getSnippet());
//                    if(lastMarker != null)
//                        mapboxMap.removeMarker(lastMarker);
//
//                    lastMarker = mapboxMap.addMarker(options);
//                }
//            });
        }
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