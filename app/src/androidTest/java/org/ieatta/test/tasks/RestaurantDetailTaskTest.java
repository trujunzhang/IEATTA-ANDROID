package org.ieatta.test.tasks;

import android.support.test.runner.AndroidJUnit4;

import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.cells.model.ReviewsCellModel;
import org.ieatta.provide.MainSegueIdentifier;
import org.ieatta.server.cache.ThumbnailImageUtil;
import org.ieatta.tasks.RestaurantDetailTask;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bolts.Continuation;
import bolts.Task;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class RestaurantDetailTaskTest {
    private static final int TASK_COMPLETION_TIMEOUT = 20000;
    private RestaurantDetailTask task;
    private String restaurantUUID = "1CE562A4-A978-4B75-9B7B-2F3CF9F42A04"; // The Flying Falafel
//    private String restaurantUUID = "33ED9F31-F6A5-43A4-8D11-8E511CA0BD39"; // The Spice Jar

    @Before
    public void setUp() throws Exception {
        HistoryEntry entry = new HistoryEntry(MainSegueIdentifier.detailRestaurantSegueIdentifier,restaurantUUID);
        this.task = new RestaurantDetailTask(entry,null,null);
    }

    @Test
    public void testRestaurantDetail() throws InterruptedException {
        final CountDownLatch completionLatch = new CountDownLatch(1);
        task.executeTask().onSuccess(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                RestaurantDetailTask _task = RestaurantDetailTaskTest.this.task;
                List<ReviewsCellModel> cellModelList = _task.reviewsCellModelList;
                if (cellModelList.size() > 0) {
                    ReviewsCellModel cellModel = cellModelList.get(0);
                    String usedRef = cellModel.userUUID;
                    File imageUrl = ThumbnailImageUtil.sharedInstance.getImageFile(usedRef);
                    if (imageUrl != null && imageUrl.exists()) {
                        String path = imageUrl.getAbsolutePath();
                    } else {
                        String title = cellModel.title;
                    }
                }
                return null;
            }
        }).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                Exception error = task.getError();
                completionLatch.countDown();
                return null;
            }
        });
        completionLatch.await(TASK_COMPLETION_TIMEOUT, TimeUnit.MILLISECONDS);
    }
}