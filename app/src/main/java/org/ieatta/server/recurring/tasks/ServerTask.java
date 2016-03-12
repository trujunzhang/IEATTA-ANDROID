package org.ieatta.server.recurring.tasks;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.ieatta.parse.ParseQueryUtils;
import org.ieatta.server.recurring.util.SerialTasksManager;
import org.wikipedia.util.log.L;

import java.util.Date;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

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

        ParseQuery<ParseObject> query = ParseQueryUtils.createQueryForRecorded(newRecordObject);

        return query.getFirstInBackground().onSuccessTask(new Continuation<ParseObject, Task<Void>>() {
            @Override
            public Task<Void> then(Task<ParseObject> task) throws Exception {
                ParseObject object = task.getResult();
                if (object == null) {
                    L.d("The getFirst request failed.");
                } else {
                    L.d("Retrieved the object.");
                }
                return null;
            }
        });
    }
}
