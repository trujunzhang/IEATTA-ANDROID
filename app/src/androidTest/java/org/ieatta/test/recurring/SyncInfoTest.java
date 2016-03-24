package org.ieatta.test.recurring;

import android.support.test.runner.AndroidJUnit4;

import org.ieatta.server.recurring.SyncInfo;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class SyncInfoTest {

    public SimpleDateFormat getSimpleDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat;
    }

    @Test
    public void testStoreAndRead() {
        SyncInfo syncInfo = new SyncInfo(SyncInfo.TAG_NEW_RECORD_DATE);

        Date emptyDate = syncInfo.getLastRunTime();
        assertThat("Current last date Must be null", (emptyDate == null));

        final Date lastRunDate = new Date();
        String lastRunDateString = getSimpleDateFormat().format(lastRunDate);

        // 1. store the lastRunDate
        syncInfo.setLastRunTime(lastRunDate);

        // 2. get the lastRunDate
        long lastRunTime = syncInfo.getLastRunTime().getTime();
        String storedDateString = getSimpleDateFormat().format(new Date(lastRunTime));

        assertThat("equal each other.", lastRunDateString.equals(storedDateString));
    }

}