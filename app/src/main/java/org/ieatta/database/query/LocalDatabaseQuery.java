package org.ieatta.database.query;

import android.location.Location;

import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.realm.DBBuilder;
import org.ieatta.database.realm.RealmModelReader;
import org.ieatta.parse.ParseObjectConstant;
import org.ieatta.utils.GeoHashUtil;

import java.util.List;

import bolts.Task;

public class LocalDatabaseQuery {

    public static Task<List<DBRestaurant>> queryNearRestaurants(Location location) {
        String containedEncodeHash = GeoHashUtil.getEncodeHash(location);
        DBBuilder builder = new DBBuilder().whereContainedIn("geoHash", containedEncodeHash);
        return new RealmModelReader(DBRestaurant.class).fetchResults(builder, true);
    }

    public static Task<List<DBPhoto>> queryPhotosForRestaurant(String UUID) {
        DBBuilder builder = new DBBuilder().whereEqualTo("restaurantRef", UUID)
                .orderByDescending(ParseObjectConstant.kPAPFieldObjectCreatedDateKey);
        return new RealmModelReader(DBPhoto.class).fetchResults(builder, true);
    }

    public static DBBuilder get(String UUID) {
        DBBuilder builder = new DBBuilder().whereEqualTo("UUID", UUID);
        return builder;
    }

}
