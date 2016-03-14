package org.ieatta.test.query;

import android.location.Location;
import android.support.test.runner.AndroidJUnit4;

import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class LocalDatabaseQueryTest {
    private static final int TASK_COMPLETION_TIMEOUT = 20000;

    @Test
    public void testQueryNearRestaurants() throws InterruptedException {
        final CountDownLatch completionLatch = new CountDownLatch(1);
        Location location = getLocation();
        LocalDatabaseQuery.queryNearRestaurants(location).onSuccess(new Continuation<RealmResults<DBRestaurant>, Void>() {
            @Override
            public Void then(Task<RealmResults<DBRestaurant>> task) {
                RealmResults<DBRestaurant> result = task.getResult();
                assertThat("Fetched restaurants length", (result.size() == 2));
                completionLatch.countDown();
                return null;
            }
        });
        completionLatch.await(TASK_COMPLETION_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    /**
     * ['dr5ru0r0y4xj','dr5ru1nwsk1v','dr5ru22mf339','dr5ru0r24xuf','dr5ru22mf339','dr5ru0nzfv5q','wtv8r4whgz2y']
     * @return
     */
    private Location getLocation() {
        double[][] data = {
                // {40.738821,-73.994026},// 'dr5ru0r0y4xj'
                {40.738687, -73.993098},// 'dr5ru0rb5cej' (Region test)
        };
        Location location = new Location("");
        location.setLatitude(data[0][0]);
        location.setLongitude(data[0][1]);

        return location;
    }

}