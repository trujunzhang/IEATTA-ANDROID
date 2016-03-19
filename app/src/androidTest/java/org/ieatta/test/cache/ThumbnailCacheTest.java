package org.ieatta.test.cache;

import android.support.test.runner.AndroidJUnit4;

import org.ieatta.database.models.DBNewRecord;
import org.ieatta.database.provide.PQueryModelType;
import org.ieatta.server.cache.ThumbnailImageUtil;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.location.Location;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

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

    private HashMap<String, Integer> photoHashmap = new LinkedHashMap<>();
    int step = 0;

    @Test
    public void testThumbnailCache() throws InterruptedException {
        final CountDownLatch completionLatch = new CountDownLatch(1);

        ThumbnailImageUtil.sharedInstance.getImagesList().continueWithTask(new Continuation<List<File>, Task<Void>>() {
            public Task<Void> then(Task<List<File>> results) throws Exception {
                List<File> fileList = results.getResult();
                Log.d("ThumbnailCacheTest", "Cached folder's count: " + fileList.size());

                // Create a trivial completed task as a base case.
                Task<Void> task = Task.forResult(null);
                for (final File result : fileList) {
                    final String name = result.getName();
                    final int length = result.listFiles().length;

                    // For each item, extend the task with a function to delete the item.
                    task = task.continueWithTask(new Continuation<Void, Task<Void>>() {
                        public Task<Void> then(Task<Void> ignored) throws Exception {
                            // Return a task that will be marked as completed when the delete is finished.
                            return checkSameLength(name, length, step++);
                        }
                    });
                }
                return task;
            }
        }).continueWith(new Continuation<Void, Void>() {
            public Void then(Task<Void> ignored) throws Exception {
                // Every task was verified.
                completionLatch.countDown();
                return null;
            }
        });

        completionLatch.await(TASK_COMPLETION_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private Task<Void> checkSameLength(String uuid, final int length, final int step) {
        return LocalDatabaseQuery.getPhotos(uuid).onSuccess(new Continuation<RealmResults<DBPhoto>, Void>() {
            @Override
            public Void then(Task<RealmResults<DBPhoto>> task) throws Exception {
                int expect = task.getResult().size();
                Log.d("ThumbnailCacheTest", "result(" + step + ") :" + length + "==" + expect);
                assertThat("The same usedRef,The same photo's count", (length == (expect)));
                return null;
            }
        });
    }
}