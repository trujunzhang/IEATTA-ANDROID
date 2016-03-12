package org.ieatta.server.recurring.tasks;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.ieatta.database.models.DBNewRecord;
import org.ieatta.parse.ParseObjectReader;
import org.ieatta.server.recurring.SerialTasksManager;
import org.ieatta.server.recurring.SyncInfo;
import org.wikipedia.util.log.L;

import java.util.Date;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmObject;

public final class ServerTask {

    public static Task<Void> getFromServer(ParseQuery query){
        final Task<List<ParseObject>> inBackground = query.findInBackground();
        return inBackground.onSuccessTask(new Continuation<List<ParseObject>, Task<Void>>() {
            @Override
            public Task<Void> then(Task<List<ParseObject>> task) throws Exception {
                return ServerTask.executeSerialTasks(task);
            }
        });
    }

    private static Task<Void> executeSerialTasks(Task<List<ParseObject>> previous) {
        List<ParseObject> results =  previous.getResult();
        L.d("get count in Pulling objects from Server: " + results.size());

        final SerialTasksManager<ParseObject> manager = new SerialTasksManager<>(results);
        if (manager.hasNext() == false) {
            return Task.forResult(null);
        }

        return startSingleTask(manager);
    }

    private static Task<Void> startSingleTask(final SerialTasksManager<ParseObject> manager) {
        return getObjectsFromServerTask(manager.next()).onSuccessTask(new Continuation<Void, Task<Void>>() {
            @Override
            public Task<Void> then(Task<Void> task) throws Exception {
                if (manager.hasNext() == false)
                    return Task.forResult(null);

                return startSingleTask(manager);
            }
        });
    }

    /**
     * Pull online objects from Parse.com.
     * <p/>
     * - parameter newRecordObject: A row data on the NewRecord table.
     */
    private static Task<Void> getObjectsFromServerTask(ParseObject newRecordObject) {
        Date lastCreateAt = newRecordObject.getCreatedAt();

//         1. Create model instance from record's modelType.
        final RealmObject newRecord = new DBNewRecord();
        ParseObjectReader.reader(newRecordObject, newRecord);
        L.d(" get NewRecordObject's instance: " + newRecord.toString());

        // 2. Pull from server.
        return model.pullFromServerAndPin()
                .onSuccess(new Continuation<Void, Void>() {
                    @Override
                    public Void then(Task<Void> task) throws Exception {

                        /// 1. Update last synched date.
                        new SyncInfo(SyncInfo.TAG_NEW_RECORD_DATE).storeNewRecordDate(lastCreateAt);

                        /// 2. When pull from server successfully, sometimes need to notify have new parse models.
//                          SyncNotify.notify(model);
                        return null;
                    }
                });

    }

}
