package org.ieatta.server.sync.tasks;


import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.wikipedia.util.log.L;

import java.util.Date;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by djzhang on 11/30/15.
 */
public class ServerTask {

//    public static Task<Void> PullFromServerSeriesTask(ParseQuery query) {
//
//        return query.findInBackground().onSuccessTask(new Continuation() {
//            @Override
//            public Object then(Task task) throws Exception {
//                return executeSerialTasks(task);
//            }
//        });
//    }

//    private static Task<Void> executeSerialTasks(Task previous) {
//        List<ParseObject> results = (List<ParseObject>) previous.getResult();
//        L.d("{ count in Pull objects from Server }: " + results.size());
//
//        SerialTasksManager<ParseObject> manager = new SerialTasksManager<>(results);
//        if (manager.hasNext() == false) {
//            return Task.forResult(null);
//        }
//
//        return startPullFromServerSingleTask(manager);
//    }
//
//    private static Task<Void> startPullFromServerSingleTask(final SerialTasksManager<ParseObject> manager) {
//        return PullObjectFromServerTask(manager.next())
//                .onSuccessTask(new Continuation() {
//                    @Override
//                    public Object then(Task task) throws Exception {
//                        if (manager.hasNext() == false) {
//                            return Task.forResult(true);
//                        }
//                        return startPullFromServerSingleTask(manager);
//                    }
//                });
//    }

    /**
     * Pull online objects from Parse.com.
     * <p/>
     * - parameter pulledNewRecordObject: A row data on the NewRecord table.
     */
//    private static Task PullObjectFromServerTask(ParseObject pulledNewRecordObject) {
//        final Date lastRecordCreateAt = pulledNewRecordObject.getCreatedAt();

        // 1. Create model instance from record's modelType.
//        final ParseModelAbstract model = NewRecord.getRecordedInstance(pulledNewRecordObject);
//        LogUtils.debug(" [NewRecord from parse.com]: " + model.printDescription());
//
//        // 2. Pull from server.
//        return model.pullFromServerAndPin()
//                .onSuccess(new Continuation<Void, Void>() {
//                    @Override
//                    public Void then(Task<Void> task) throws Exception {
//
//                        /// 1. Update last synched date.
//                        new SyncInfo(SyncInfo.TAG_NEW_RECORD_DATE).storeNewRecordDate(lastRecordCreateAt);
//
//                        /// 2. When pull from server successfully, sometimes need to notify have new parse models.
//                          SyncNotify.notify(model);
//                        return null;
//                    }
//                });

//    }

}
