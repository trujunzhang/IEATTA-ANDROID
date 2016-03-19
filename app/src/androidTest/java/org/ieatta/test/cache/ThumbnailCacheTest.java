package org.ieatta.test.cache;

import android.support.test.runner.AndroidJUnit4;

import org.ieatta.database.models.DBNewRecord;
import org.ieatta.database.provide.PQueryModelType;
import org.ieatta.server.cache.ThumbnailImageUtil;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.location.Location;
import android.support.test.runner.AndroidJUnit4;

import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.server.cache.ThumbnailImageUtil;
import org.ieatta.utils.LocationUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wikipedia.util.log.L;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

import static org.hamcrest.MatcherAssert.assertThat;


import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(AndroidJUnit4.class)
public class ThumbnailCacheTest {
    private static final int TASK_COMPLETION_TIMEOUT = 20000;

    private HashMap<String,Integer> photoHashmap = new LinkedHashMap<>();

    @Test
    public void testThumbnailCache() throws InterruptedException {

        final CountDownLatch completionLatch = new CountDownLatch(1);

        ThumbnailImageUtil.sharedInstance.getImagesList().onSuccessTask(new Continuation<List<File>, Task<Void>>() {
            @Override
            public Task<Void> then(Task<List<File>> task) throws Exception {
                HashMap<String, Integer> map = ThumbnailCacheTest.this.getPhotoHashmap(task.getResult());
                return null;
            }
        });

        completionLatch.await(TASK_COMPLETION_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private HashMap<String,Integer> getPhotoHashmap(List<File> list){
        HashMap<String, Integer> map = new LinkedHashMap<>();
        for(File fold : list){
            String name = fold.getName();
            int length = fold.listFiles().length;
            map.put(name,length);
        }
        return  map;
    }

}