package org.ieatta.activity.maps;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsUtil {

    public static void handleNewLocation(GoogleMap map, LeadMapView leadMapView) {
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
}
