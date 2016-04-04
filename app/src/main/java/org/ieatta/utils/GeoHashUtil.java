package org.ieatta.utils;

import android.location.Location;

import com.github.davidmoten.geo.GeoHash;

public class GeoHashUtil {

    public static String getEncodeHash(Location location) {
        if(location == null)
            return "no location";

        String encodeHash = GeoHash.encodeHash(location.getLatitude(), location.getLongitude());
        encodeHash = encodeHash.substring(0,6);
        return encodeHash;
    }
}
