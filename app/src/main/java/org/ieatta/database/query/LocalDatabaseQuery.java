package org.ieatta.database.query;

import android.location.Location;

import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.realm.DBBuilder;
import org.ieatta.database.realm.RealmModelReader;
import org.ieatta.parse.DBConstant;
import org.ieatta.utils.GeoHashUtil;

import java.util.List;

import bolts.Task;
import io.realm.RealmResults;

public class LocalDatabaseQuery {

    public static Task<RealmResults<DBRestaurant>> queryNearRestaurants(Location location) {
        String containedEncodeHash = GeoHashUtil.getEncodeHash(location);
        DBBuilder builder = new DBBuilder().whereContainedIn(DBConstant.kPAPFieldModelGEOHASH, containedEncodeHash);
        return new RealmModelReader<DBRestaurant>(DBRestaurant.class).fetchResults(builder, false);
    }

    public static Task<RealmResults<DBPhoto>> queryPhotosForRestaurant(String UUID) {
        DBBuilder builder = new DBBuilder()
                .whereEqualTo(DBConstant.kPAPFieldLocalRestaurantKey, UUID)
                .orderByDescending(DBConstant.kPAPFieldObjectCreatedDateKey);
        return new RealmModelReader<DBPhoto>(DBPhoto.class).fetchResults(builder, false);
    }

    public static DBBuilder get(String UUID) {
        return new DBBuilder().whereEqualTo(DBConstant.kPAPFieldObjectUUIDKey, UUID);
    }

    public static DBBuilder getQueryOrderedPeople(String eventUUID) {
        return new DBBuilder().whereEqualTo(DBConstant.kPAPFieldEventKey, eventUUID)
                .orderByDescending(DBConstant.kPAPFieldObjectCreatedDateKey);
    }

    public static DBBuilder getObjectsByUUIDs(List<String> UUIDs) {
        return new DBBuilder().whereContainedIn(DBConstant.kPAPFieldEventKey, UUIDs)
                .orderByDescending(DBConstant.kPAPFieldObjectCreatedDateKey);
    }
}
