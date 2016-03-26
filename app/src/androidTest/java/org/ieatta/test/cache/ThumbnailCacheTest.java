package org.ieatta.test.cache;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.database.realm.DBBuilder;
import org.ieatta.database.realm.RealmModelReader;
import org.ieatta.server.cache.ThumbnailImageUtil;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bolts.Continuation;
import bolts.Task;
import io.realm.Realm;
import io.realm.RealmResults;

import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(AndroidJUnit4.class)
public class ThumbnailCacheTest {
    private static final int TASK_COMPLETION_TIMEOUT = 20000;

    private HashMap<String, Integer> photoHashmap = new LinkedHashMap<>();
    int step = 0;
    int dbPhotoCount = 0;


    @Test
    public void testThumbnailTotalCount() throws InterruptedException {
        final CountDownLatch completionLatch = new CountDownLatch(1);
        final List<Realm> realmList = new LinkedList<>();

        ThumbnailImageUtil.sharedInstance.getImagesListTask().continueWithTask(new Continuation<List<File>, Task<RealmResults<DBPhoto>>>() {
            public Task<RealmResults<DBPhoto>> then(Task<List<File>> results) throws Exception {
                List<File> fileList = results.getResult();
                for (File fold : fileList) {
                    dbPhotoCount += fold.listFiles().length;
                }
                Log.d("ThumbnailCacheTest", "Cached file's count: " + dbPhotoCount);

                return new RealmModelReader<DBPhoto>(DBPhoto.class).fetchResults(new DBBuilder(), false, realmList);
            }
        }).onSuccess(new Continuation<RealmResults<DBPhoto>, Void>() {
            @Override
            public Void then(Task<RealmResults<DBPhoto>> task) throws Exception {
                int expect = task.getResult().size();
                assertThat("The same photo's count between cached folder and databse.", (dbPhotoCount == (expect)));
                return null;
            }
        }).continueWith(new Continuation<Void, Void>() {
            public Void then(Task<Void> ignored) throws Exception {
                LocalDatabaseQuery.closeRealmList(realmList);
                // Every task was verified.
                completionLatch.countDown();
                return null;
            }
        });

        completionLatch.await(TASK_COMPLETION_TIMEOUT, TimeUnit.MILLISECONDS);
    }


    @Test
    public void testThumbnailCache() throws InterruptedException {
        final CountDownLatch completionLatch = new CountDownLatch(1);

        ThumbnailImageUtil.sharedInstance.getImagesListTask().continueWithTask(new Continuation<List<File>, Task<Void>>() {
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
                            return checkSameLength(name, length, ++step);
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
        final List<Realm> realmList = new LinkedList<>();
        return LocalDatabaseQuery.getPhotos(uuid,realmList).onSuccess(new Continuation<RealmResults<DBPhoto>, Void>() {
            @Override
            public Void then(Task<RealmResults<DBPhoto>> task) throws Exception {
                int expect = task.getResult().size();
                LocalDatabaseQuery.closeRealmList(realmList);
                Log.d("ThumbnailCacheTest", "result(" + step + ") :" + length + "==" + expect);
                assertThat("The same usedRef,The same photo's count", (length == (expect)));
                return null;
            }
        });
    }
}