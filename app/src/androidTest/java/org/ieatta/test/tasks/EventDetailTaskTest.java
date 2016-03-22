package org.ieatta.test.tasks;

import android.support.test.runner.AndroidJUnit4;

import org.ieatta.tasks.EventDetailTask;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bolts.Continuation;
import bolts.Task;

@RunWith(AndroidJUnit4.class)
public class EventDetailTaskTest {
    private static final int TASK_COMPLETION_TIMEOUT = 20000;
    private EventDetailTask task = new EventDetailTask();
    private String restaurantUUID = "1CE562A4-A978-4B75-9B7B-2F3CF9F42A04";
    private String eventUUID = "07B2D33C-F11D-404B-9D78-016D16BEE9FE";

    @Test
    public void testEventDetail() throws InterruptedException {
        final CountDownLatch completionLatch = new CountDownLatch(1);
        task.executeTask(restaurantUUID,eventUUID).onSuccess(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                EventDetailTask _task = EventDetailTaskTest.this.task;
//                _task.
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