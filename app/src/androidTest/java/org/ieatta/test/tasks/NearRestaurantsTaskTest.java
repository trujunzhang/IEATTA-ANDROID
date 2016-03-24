package org.ieatta.test.tasks;

import android.location.Location;
import android.support.test.runner.AndroidJUnit4;

import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.provide.MainSegueIdentifier;
import org.ieatta.tasks.NearRestaurantsTask;
import org.ieatta.utils.LocationUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wikipedia.util.log.L;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class NearRestaurantsTaskTest {
    private static final int TASK_COMPLETION_TIMEOUT = 20000;
    private NearRestaurantsTask task;

    @Before
    public void setUp() throws Exception {
        Location location = LocationUtil.getLocation();
        HistoryEntry entry = new HistoryEntry(MainSegueIdentifier.nearbyRestaurants,location);
        this.task = new NearRestaurantsTask(entry);
    }

    @Test
    public void testRestaurantDetail() throws InterruptedException {
        final CountDownLatch completionLatch = new CountDownLatch(1);

        task.executeTask().onSuccess(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                RealmResults<DBRestaurant> result = NearRestaurantsTaskTest.this.task.restaurants;
                int size = result.size();
                L.d("Size of the Restaurants: " + size);
                assertThat("Fetched restaurants length", (size == 3));
                return null;
            }
        }).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                Exception error = task.getError();
                completionLatch.countDown();
                return null;
            }
        });
        completionLatch.await(TASK_COMPLETION_TIMEOUT, TimeUnit.MILLISECONDS);
    }
}