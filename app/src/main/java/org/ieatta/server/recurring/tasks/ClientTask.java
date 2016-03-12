package org.ieatta.server.recurring.tasks;

/**
 * Created by djzhang on 11/30/15.
 */
public class ClientTask {

//    public static Task<Void> PushToServerSeriesTask(LocalQuery query) {
//
//        return ParseModelLocalQuery.findInBackgroundFromRealm(query)
//                .onSuccessTask(new Continuation<List<ParseModelAbstract>, Task<Void>>() {
//                    @Override
//                    public Task<Void> then(Task<List<ParseModelAbstract>> task) throws Exception {
//                        return executeSerialTasks(task);
//                    }
//                });
//    }
//
//    private static Task<Void> executeSerialTasks(Task<List<ParseModelAbstract>> previous) {
//        List<ParseModelAbstract> results = previous.getResult();
//        LogUtils.debug("{ count in Push objects to Server }: " + results.size());
//
//        SerialTasksManager<ParseModelAbstract> manager = new SerialTasksManager<>(results);
//        if (manager.hasNext() == false) {
//            return Task.forResult(null);
//        }
//
//        return startPullFromServerSingleTask(manager);
//    }
//
//    private static Task<Void> startPullFromServerSingleTask(final SerialTasksManager<ParseModelAbstract> manager) {
//        return PushObjectToServerTask((NewRecord) manager.next())
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
//
//    /**
//     * Push all offline objects to Parse.com.
//     * <p/>
//     * - parameter newRecordObject: A row data on the NewRecord table.
//     */
//    private static Task<Void> PushObjectToServerTask(final NewRecord newRecord) {
//        LogUtils.debug(" [ newRecord in push to server ]: " + newRecord.printDescription());
//
//        // 1. Get the recoreded emptyModel instance from NewRecord, by the modelType and the modelPoint.
//        // (such as photo,restaurant,event etc)
//        final ParseModelAbstract emptyModel = newRecord.getRecordedModel();
//        LogUtils.debug(" [ emptyModel in push to server ]: " + emptyModel.printDescription());
//
//        final Task.TaskCompletionSource tcs = Task.create();
//
//        // Step1: Get the first model by the emptyModel's uuid.
//        //        And Save it's ParseObject to Parse.com.
//        emptyModel.pushToServer()
//                .onSuccessTask(new Continuation<Void, Task<Void>>() {
//                    @Override
//                    public Task<Void> then(Task<Void> task) throws Exception {
//                        // Step2: Save the newRecord's ParseObject to Parse.com
//                        return newRecord.saveParseObjectToServer();
//                    }
//                }).onSuccessTask(new Continuation() {
//            @Override
//            public Object then(Task task) throws Exception {
//                // Step3: Delete the newRrecord on the localdate.
//                return newRecord.deleteInBackground();
//            }
//        }).onSuccessTask(new Continuation() {
//            @Override
//            public Object then(Task task) throws Exception {
//                // For the specail photo here.
//                // Finally, Post some events.
//                return emptyModel.afterPushToServer();
//            }
//        }).continueWith(new Continuation<Void, Object>() {
//            @Override
//            public Object then(Task<Void> task) throws Exception {
//                if (task.isFaulted()) {
//                    tcs.setError(task.getError());
//                } else {
//                    tcs.setResult(null);
//                }
//                return null;
//            }
//        });
//
//        return tcs.getTask();
//    }

}
