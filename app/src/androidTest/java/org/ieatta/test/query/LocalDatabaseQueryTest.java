package org.ieatta.test.query;

import android.location.Location;
import android.support.test.runner.AndroidJUnit4;

import com.github.davidmoten.geo.GeoHash;

import org.ieatta.database.models.DBNewRecord;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.provide.PQueryModelType;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.server.recurring.SyncInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wikipedia.settings.Prefs;
import org.wikipedia.testlib.TestLatch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import bolts.Continuation;
import bolts.Task;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class LocalDatabaseQueryTest {

    private TestLatch completionLatch;
    @Test
    public void testQueryNearRestaurants() {
        completionLatch = new TestLatch();
        Location location = getLocation();
        LocalDatabaseQuery.queryNearRestaurants(location).onSuccess(new Continuation<List<DBRestaurant>, Void>() {
            @Override
            public Void then(Task<List<DBRestaurant>> task) throws Exception {
                List<DBRestaurant> result = task.getResult();
                assertThat("Fetched restaurants length", (1 == 2));
                completionLatch.countDown();
                return null;
            }
        });

        completionLatch.await();
    }

    /**
     * // 'dr5ru0r0y4xj','dr5ru1nwsk1v','dr5ru22mf339','dr5ru0r24xuf','dr5ru22mf339','dr5ru0nzfv5q','wtv8r4whgz2y'
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