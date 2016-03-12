package org.ieatta.server.recurring;


import org.wikipedia.util.log.L;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by djzhang on 11/27/15.
 */
public class SyncHandler {
    private static final int NUMBER_FETCH_NEW_RECORD = 20;
    private static final int NUMBER_PUSH_NEW_RECORD = 6;

    public static final SyncHandler sharedInstance = new SyncHandler();

    private boolean didEndRecurringTask = true;

    public Task<Void> execute() {
        return null;
//        return ServerTask(new SyncInfo(SyncInfo.TAG_NEW_RECORD_DATE).createQuery(NUMBER_FETCH_NEW_RECORD))
//                .onSuccessTask(new Continuation<Void, Task<Void>>() {
//                    @Override
//                    public Task<Void> then(Task<Void> task) throws Exception {
//                        return ClientTask.PushToServerSeriesTask(new DBNewRecord().createQueryForPushObjectsToServer(NUMBER_PUSH_NEW_RECORD));
//                    }
//                });
    }

    private void endTasks(Exception error) {
        if (error != null) {
            L.d("Error when async database: " + error.getLocalizedMessage());
        } else {
            L.d("Async database task end successfully!");
        }

        this.didEndRecurringTask = true;
    }

    /**
     * Start Point
     */
    public void startTask() {
        if (this.didEndRecurringTask == false)
            return;

        // 1. Prepare tasks.
        this.didEndRecurringTask = false;
        this.execute().continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                SyncHandler.this.endTasks(task.getError());
                return null;
            }
        });
    }
}
