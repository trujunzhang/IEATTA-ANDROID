package org.ieatta.database.query;

import android.location.Location;

import com.github.davidmoten.geo.GeoHash;

import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.realm.DBBuilder;
import org.ieatta.database.realm.RealmModelReader;
import org.ieatta.utils.GeoHashUtil;

import bolts.Task;

public class LocalDatabaseQuery {

    public static Task<DBRestaurant> queryNearRestaurants(Location location){
        String encodeHash = GeoHashUtil.getEncodeHash(location);


        DBBuilder builder = new DBBuilder();
        builder.whereContainedIn("geoHash",encodeHash);

        new RealmModelReader(DBRestaurant.class).fetchResults(builder);

        return null;
    }

}
