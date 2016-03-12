package org.ieatta.test.recurring;

import android.support.test.runner.AndroidJUnit4;

import org.ieatta.server.recurring.SyncInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wikipedia.settings.Prefs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by djzhang on 3/12/16.
 */
@RunWith(AndroidJUnit4.class)
public class SyncInfoTest {

    public SimpleDateFormat getSimpleDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat;
    }

    @Test
    public void testStoreAndRead() {
        Date lastRunDate = new Date();
        Prefs.setLastRunTime("sync", lastRunDate.getTime());

        String lastRunDateString = getSimpleDateFormat().format(lastRunDate);

        long lastRunTime = Prefs.getLastRunTime("sync");
        Date storedDate = new Date(lastRunTime);

        String storedDateString = getSimpleDateFormat().format(storedDate);

        assertThat("equal each other.",lastRunDateString.equals(storedDateString));
    }

}