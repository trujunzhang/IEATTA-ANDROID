package org.ieatta.database.query;

import android.location.Location;

import com.github.davidmoten.geo.GeoHash;

import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.provide.PQueryModelType;
import org.ieatta.database.realm.DBBuilder;
import org.ieatta.database.realm.RealmModelReader;
import org.ieatta.parse.ParseObjectConstant;
import org.ieatta.utils.GeoHashUtil;

import java.util.List;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmObject;

public class LocalDatabaseQuery <T extends RealmObject> {

    private Class<T > clazz;

    public LocalDatabaseQuery(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static Task<List<DBRestaurant>> queryNearRestaurants(Location location){
        String containedEncodeHash = GeoHashUtil.getEncodeHash(location);
        DBBuilder builder = new DBBuilder().whereContainedIn("geoHash", containedEncodeHash);
        return new RealmModelReader(DBRestaurant.class).fetchResults(builder);
    }

    public static Task<List<DBPhoto>> queryPhotosForRestaurant(String UUID) {
        DBBuilder builder = new DBBuilder().whereEqualTo("restaurantRef",UUID).orderByDescending(ParseObjectConstant.kPAPFieldObjectCreatedDateKey);
        return new RealmModelReader(DBPhoto.class).fetchResults(builder);
    }

    public  Task<T> fetchObject(String UUID){
        DBBuilder builder = new DBBuilder().whereEqualTo("UUID",UUID);
        return new RealmModelReader(clazz).fetchResults(builder);
    }
}
