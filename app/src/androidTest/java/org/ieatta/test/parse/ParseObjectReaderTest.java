package org.ieatta.test.parse;

import junit.framework.TestCase;


import android.support.test.runner.AndroidJUnit4;

import org.ieatta.database.models.DBNewRecord;
import org.ieatta.database.provide.PQueryModelType;
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
public class ParseObjectReaderTest {

    @Test
    public void testDBNewRecord() {
        DBNewRecord newRecord = new DBNewRecord();

        newRecord.setModelType(PQueryModelType.Event.getType());

        Date nowDate = new Date();
        newRecord.setObjectCreatedDate(nowDate);

        assertThat("type equal each other.", newRecord.getModelType() == PQueryModelType.Event.getType());
    }

}