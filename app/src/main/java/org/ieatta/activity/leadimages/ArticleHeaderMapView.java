package org.ieatta.activity.leadimages;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.ieatta.R;
import org.ieatta.activity.LeadImage;
import org.ieatta.activity.LeadMapView;
import org.ieatta.analytics.PageFragmentFunnel;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArticleHeaderMapView extends FrameLayout {
    @Bind(R.id.mapview)
    MapView mapView;

    private Marker lastMarker;

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

    public void toggleMapView(boolean activated) {
        new PageFragmentFunnel().logMapViewActivated(activated);
        if (activated == true) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
    }

    public void load(@Nullable final LeadMapView leadMapView) {
        if (leadMapView == null) {
            setVisibility(GONE);
        } else {
            setVisibility(GONE);
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(MapboxMap mapboxMap) {

                    // Set map style
                    mapboxMap.setStyleUrl(Style.MAPBOX_STREETS);

                    // Set the camera's starting position
                    LatLng location = new LatLng(leadMapView.getLatitude(), leadMapView.getLongitude());
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(location) // set the camera's center position
                            .zoom(20)  // set the camera's zoom level
                            .tilt(20)  // set the camera's tilt
                            .build();

                    // Move the camera to that position
                    mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    new PageFragmentFunnel().logMapboxMarker(leadMapView.getTitle(), leadMapView.getSnippet());

                    MarkerOptions options = new MarkerOptions()
                            .position(location)
                            .title(leadMapView.getTitle())
                            .snippet(leadMapView.getSnippet());
                    if(lastMarker != null)
                        mapboxMap.removeMarker(lastMarker);

                    lastMarker = mapboxMap.addMarker(options);
                }
            });
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
}