package org.ieatta.test.tasks;

import android.support.test.runner.AndroidJUnit4;

import org.ieatta.tasks.RestaurantDetailTask;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bolts.Continuation;
import bolts.Task;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class RestaurantDetailTaskTest {
    private static final int TASK_COMPLETION_TIMEOUT = 20000;
    private RestaurantDetailTask task = new RestaurantDetailTask();
    private String restaurantUUID = "1CE562A4-A978-4B75-9B7B-2F3CF9F42A04";

    @Test
    public void testRestaurantDetail() throws InterruptedException {
        final CountDownLatch completionLatch = new CountDownLatch(1);
        task.executeTask(restaurantUUID).onSuccess(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                completionLatch.countDown();
                RestaurantDetailTask _task = RestaurantDetailTaskTest.this.task;
                return null;
            }
        });
        completionLatch.await(TASK_COMPLETION_TIMEOUT, TimeUnit.MILLISECONDS);
    }
}