package org.ieatta.server.sync;


import bolts.Continuation;
import bolts.Task;

/**
 * Created by djzhang on 11/27/15.
 */
public class ParseAsyncHandler {
    private ParseAsyncHandler self = this;
    private static final int PAGE_NUMBER_FETCH_NEW_RECORD = 20;
    private static final int PAGE_NUMBER_PUSH_NEW_RECORD = 6;

    public static final ParseAsyncHandler sharedInstance = new ParseAsyncHandler();

    private boolean didEndAsync = true;

//    public Task<Void> PullObjectsFromServer() {
//
//        return com.ieatta.com.parse.async.tasks.ServerTask.PullFromServerSeriesTask(new AsyncCacheInfo(AsyncCacheInfo.TAG_NEW_RECORD_DATE).createQuery(PAGE_NUMBER_FETCH_NEW_RECORD))
//                .onSuccessTask(new Continuation<Void, Task<Void>>() {
//                    @Override
//                    public Task<Void> then(Task<Void> task) throws Exception {
//                        return PushNewRecordToServerTask.PushToServerSeriesTask(new NewRecord().createQueryForPushObjectsToServer(PAGE_NUMBER_PUSH_NEW_RECORD));
//                    }
//                });
//    }

//    private void endAsyncTasks(Exception error) {
//        if (error != null) {
//            LogUtils.debug("Error when async database: " + error.getLocalizedMessage());
//        } else {
//            LogUtils.debug("Async database task end sucessfully!");
//        }
//
//        this.didEndAsync = true;
//    }


    /**
     * Start Point
     */
//    public void executeParseAsyncHandler() {
//        // Already have the same async handler.
//        if (this.didEndAsync == false) {
//            return;
//        }
//
//        // 1. Prepare tasks.
//        this.didEndAsync = false;
//        this.PullObjectsFromServer()
//                .continueWith(new Continuation() {
//                    @Override
//                    public Object then(Task task) throws Exception {
//                        self.endAsyncTasks(task.getError());
//                        return null;
//                    }
//                });
//
//    }


}
