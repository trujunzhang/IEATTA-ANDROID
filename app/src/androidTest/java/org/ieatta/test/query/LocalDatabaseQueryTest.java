package org.ieatta.test.query;

import android.location.Location;
import android.support.test.runner.AndroidJUnit4;

import com.github.davidmoten.geo.GeoHash;

import org.ieatta.database.models.DBNewRecord;
import org.ieatta.database.provide.PQueryModelType;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.server.recurring.SyncInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wikipedia.settings.Prefs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class LocalDatabaseQueryTest {

    @Test
    public void testQueryNearRestaurants() {
        Location location = getLocation();

        // 'gedz62b7yu1v'
        String encodeHash = GeoHash.encodeHash(location.getLatitude(), location.getLongitude());

        // 'dr5ru0r0y4xj','dr5ru1nwsk1v','dr5ru22mf339','dr5ru0r24xuf','dr5ru22mf339','dr5ru0nzfv5q','wtv8r4whgz2y'

        LocalDatabaseQuery.queryNearRestaurants(null);

//        assertThat("type equal each other.", newRecord.getModelType() == PQueryModelType.Event.getType());
    }

    private Location getLocation() {
        Location location = new Location("");
        location.setLatitude(65.9667);
        location.setLongitude(-18.5333);

        return location;
    }

}