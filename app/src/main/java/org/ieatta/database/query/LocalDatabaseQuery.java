package org.ieatta.database.query;

import android.location.Location;

import com.github.davidmoten.geo.GeoHash;

import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.realm.DBBuilder;
import org.ieatta.database.realm.RealmModelReader;
import org.ieatta.utils.GeoHashUtil;

import java.util.List;

import bolts.Continuation;
import bolts.Task;

public class LocalDatabaseQuery {

    public static Task<List<DBRestaurant>> queryNearRestaurants(Location location){
        String containedEncodeHash = GeoHashUtil.getEncodeHash(location);
        DBBuilder builder = new DBBuilder().whereContainedIn("geoHash",containedEncodeHash);
        return new RealmModelReader(DBRestaurant.class).fetchResults(builder);
    }

}
