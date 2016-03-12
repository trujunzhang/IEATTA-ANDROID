package org.ieatta.server.recurring.tasks;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.ieatta.database.models.DBNewRecord;
import org.ieatta.database.provide.PQueryModelType;
import org.ieatta.database.realm.ModelWriter;
import org.ieatta.parse.ParseObjectReader;
import org.ieatta.parse.ParseQueryUtils;
import org.ieatta.server.recurring.SyncInfo;
import org.ieatta.server.recurring.util.SerialTasksManager;
import org.wikipedia.util.log.L;

import java.util.Date;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmObject;

public final class ServerTask {

    public static Task<Void> getFromServer(ParseQuery query) {
        final Task<List<ParseObject>> task = query.findInBackground();
        return task.onSuccessTask(new Continuation<List<ParseObject>, Task<Void>>() {
            @Override
            public Task<Void> then(Task<List<ParseObject>> task) throws Exception {
                return ServerTask.executeSerialTasks(task);
            }
        });
    }

    private static Task<Void> executeSerialTasks(Task<List<ParseObject>> previous) {
        List<ParseObject> results = previous.getResult();
        L.d("Get count after pulling objects from Server: " + results.size());

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
        final Date lastCreateAt = newRecordObject.getCreatedAt();

        final DBNewRecord newRecord = new DBNewRecord();
        ParseObjectReader.reader(newRecordObject, newRecord);

        ParseQuery<ParseObject> query = ParseQueryUtils.createQueryForRecorded(newRecord);
        return query.getFirstInBackground().onSuccessTask(new Continuation<ParseObject, Task<Void>>() {
            @Override
            public Task<Void> then(Task<ParseObject> task) throws Exception {
                ParseObject object = task.getResult();
                if (object == null) {
                    L.d("The getFirst request failed.");
                } else {
                    L.d("Retrieved the object.");
                    RealmObject realmObject = ParseObjectReader.read(object, PQueryModelType.getInstance(newRecord.getModelType()));
                    return new ModelWriter().save(realmObject, PQueryModelType.getInstance(newRecord.getModelType()));
                }

                return null;
            }
        }).onSuccess(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                new SyncInfo(SyncInfo.TAG_NEW_RECORD_DATE).setLastRunTime(lastCreateAt);
                return null;
            }
        });
    }
}
