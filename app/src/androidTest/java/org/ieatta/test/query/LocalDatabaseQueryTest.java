package org.ieatta.test.query;

import android.location.Location;
import android.support.test.runner.AndroidJUnit4;

import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.utils.LocationUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wikipedia.util.log.L;

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
        Location location = LocationUtil.getLocation();
        LocalDatabaseQuery.queryNearRestaurants(location).onSuccess(new Continuation<RealmResults<DBRestaurant>, Void>() {
            @Override
            public Void then(Task<RealmResults<DBRestaurant>> task) {
                RealmResults<DBRestaurant> result = task.getResult();
                int size = result.size();
                L.d("Size of the Restaurants: " + size);
                assertThat("Fetched restaurants length", (size == 3));
                completionLatch.countDown();
                return null;
            }
        });
        completionLatch.await(TASK_COMPLETION_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testQueryPhoto() throws InterruptedException {
        final String usedRef = "C2F23EDC-106C-4D17-A6D6-8EA04E10732A"; // for Restaurant(called "Basta Pasta").
        final CountDownLatch completionLatch = new CountDownLatch(1);

        LocalDatabaseQuery.getPhoto(usedRef).onSuccess(new Continuation<DBPhoto, Object>() {
            @Override
            public Object then(Task<DBPhoto> task) throws Exception {
                DBPhoto photo = task.getResult();
                String expect = photo.getUsedRef();
                L.d("usedRef of the photo: " + expect);
                assertThat("Fetched photo's usedRef", (usedRef == expect));
                completionLatch.countDown();
                return null;
            }
        }).continueWith(new Continuation<Object, Object>() {
            @Override
            public Object then(Task<Object> task) throws Exception {
                assertThat("Fetched photo must not be null", (task.getError() == null));
                completionLatch.countDown();
                return null;
            }
        });
        completionLatch.await(TASK_COMPLETION_TIMEOUT, TimeUnit.MILLISECONDS);
    }
}