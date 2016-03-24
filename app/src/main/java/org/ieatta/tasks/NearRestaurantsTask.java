package org.ieatta.tasks;


import android.location.Location;

import com.parse.ParseGeoPoint;

import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.query.LocalDatabaseQuery;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

public class NearRestaurantsTask extends FragmentTask{
    public NearRestaurantsTask(HistoryEntry entry) {
        super(entry);
    }

    public RealmResults<DBRestaurant> restaurants;

    /**
     * Execute Task for nearby restaurants.
     * @return
     */
    public Task<Void> executeTask(){
        final Location location = this.entry.getLocation();

        return LocalDatabaseQuery.queryNearRestaurants(location).onSuccess(new Continuation<RealmResults<DBRestaurant>, Void>() {
            @Override
            public Void then(Task<RealmResults<DBRestaurant>> task) throws Exception {
                NearRestaurantsTask.this.restaurants = task.getResult();
                return null;
            }
        });
    }

}
