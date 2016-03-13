package org.ieatta.tasks;


import android.location.Location;

import com.parse.ParseGeoPoint;

import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.query.LocalDatabaseQuery;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

public class NearRestaurantsTask {
    static class SortByName implements Comparator {
        public int compare(Object o1, Object o2) {
            DBRestaurant s1 = (DBRestaurant) o1;
            DBRestaurant s2 = (DBRestaurant) o2;
            return s1.getDisplayName().toLowerCase().compareTo(s2.getDisplayName().toLowerCase());
        }
    }

    public static List<DBRestaurant> sort(List<DBRestaurant> list) {
        Collections.sort(list, new SortByName());
        return list;
    }

    private List<DBRestaurant> restaurants;

    public List<DBRestaurant> getRestaurants() {
        return restaurants;
    }

    public Task<Void> executeTask(Location location){
        return LocalDatabaseQuery.queryNearRestaurants(location).onSuccess(new Continuation<List<DBRestaurant>, Void>() {
            @Override
            public Void then(Task<List<DBRestaurant>> task) throws Exception {
                NearRestaurantsTask.this.restaurants = NearRestaurantsTask.sort(task.getResult());
                return null;
            }
        });
    }

}
