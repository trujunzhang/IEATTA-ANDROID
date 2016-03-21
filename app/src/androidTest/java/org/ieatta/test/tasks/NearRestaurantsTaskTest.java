package org.ieatta.test.tasks;

import android.location.Location;
import android.support.test.runner.AndroidJUnit4;

import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.server.cache.ThumbnailImageUtil;
import org.ieatta.tasks.NearRestaurantsTask;
import org.ieatta.utils.LocationUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wikipedia.util.log.L;

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class NearRestaurantsTaskTest {
    private static final int TASK_COMPLETION_TIMEOUT = 20000;
    private NearRestaurantsTask task = new NearRestaurantsTask();

    @Test
    public void testRestaurantDetail() throws InterruptedException {
        final CountDownLatch completionLatch = new CountDownLatch(1);
        Location location = LocationUtil.getLocation();
        task.executeTask(location).onSuccess(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                RealmResults<DBRestaurant> result = NearRestaurantsTaskTest.this.task.getRestaurants();
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