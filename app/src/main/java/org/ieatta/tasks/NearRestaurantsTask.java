package org.ieatta.tasks;


import android.location.Location;

import com.parse.ParseGeoPoint;

import org.ieatta.database.models.DBRestaurant;

import java.util.List;

import bolts.Task;

public class NearRestaurantsTask {
    private List<DBRestaurant> restaurants;

    public Task<Void> executeTask(Location location){
        return null;
    }

    public List<DBRestaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<DBRestaurant> restaurants) {
        this.restaurants = restaurants;
    }
}
