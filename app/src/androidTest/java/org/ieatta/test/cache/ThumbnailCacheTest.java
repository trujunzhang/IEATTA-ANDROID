package org.ieatta.test.cache;

import android.support.test.runner.AndroidJUnit4;

import org.ieatta.database.models.DBNewRecord;
import org.ieatta.database.provide.PQueryModelType;
import org.ieatta.server.cache.ThumbnailImageUtil;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(AndroidJUnit4.class)
public class ThumbnailCacheTest {

    @Test
    public void testThumbnailCache() {
        ThumbnailImageUtil.sharedInstance.getImageCache();
    }

}