package org.ieatta.tasks;


import android.location.Location;

import com.parse.ParseGeoPoint;

import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.query.LocalDatabaseQuery;

import java.util.List;

import bolts.Continuation;
import bolts.Task;

public class NearRestaurantsTask {
    private List<DBRestaurant> restaurants;

    public List<DBRestaurant> getRestaurants() {
        return restaurants;
    }

    public Task<Void> executeTask(Location location){
        return LocalDatabaseQuery.queryNearRestaurants(location).onSuccess(new Continuation<List<DBRestaurant>, Void>() {
            @Override
            public Void then(Task<List<DBRestaurant>> task) throws Exception {
                NearRestaurantsTask.this.restaurants = task.getResult();
                return null;
            }
        });
    }

}
