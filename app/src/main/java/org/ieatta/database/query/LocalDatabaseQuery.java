package org.ieatta.database.query;


import android.location.Location;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.realm.RealmModelReader;

import bolts.Task;

public class LocalDatabaseQuery {

    public static Task<DBRestaurant> queryNearRestaurants(Location location){
        new RealmModelReader().fetchRestaurants();
        return null;
    }

}
