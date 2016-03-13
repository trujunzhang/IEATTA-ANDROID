package org.ieatta.database.query;


import android.location.Location;

import com.github.davidmoten.geo.GeoHash;

import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.realm.RealmModelReader;

import bolts.Task;

public class LocalDatabaseQuery {

    public static Task<DBRestaurant> queryNearRestaurants(Location location){
        String encodeHash = GeoHash.encodeHash(location.getLatitude(), location.getLongitude());

        new RealmModelReader(DBRestaurant.class).fetchRestaurants();

        return null;
    }

}
