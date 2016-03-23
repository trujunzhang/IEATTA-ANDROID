package org.ieatta.database.query;


import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.ieatta.database.provide.PQueryModelType;
import org.ieatta.parse.AppConstant;
import org.ieatta.parse.ParseQueryUtil;
import org.ieatta.server.cache.CacheImageUtil;

import java.io.InputStream;

import bolts.Continuation;
import bolts.Task;

public class OnlineDatabaseQuery {

    /**
     * Query the online Photo and download the original image to cache local.
     * @param uuid   photo's UUID
     * @return
     */
    public static Task<Void> downloadOriginalPhoto(final String uuid){
        ParseQuery<ParseObject> query = ParseQueryUtil.createQueryByUUID(uuid, PQueryModelType.Photo);

        return query.getFirstInBackground().onSuccessTask(new Continuation<ParseObject, Task<InputStream>>() {
            @Override
            public Task<InputStream> then(Task<ParseObject> task) throws Exception {
                final ParseFile originalFile = task.getResult().getParseFile(AppConstant.kPAPFieldOriginalImageKey);
                return originalFile.getDataStreamInBackground();
            }
        }).onSuccessTask(new Continuation<InputStream, Task<Void>>() {
            @Override
            public Task<Void> then(Task<InputStream> task) throws Exception {
                return CacheImageUtil.sharedInstance.saveTakenPhoto(task.getResult(),uuid);
            }
        }).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                Exception error = task.getError();
                return null;
            }
        });
    }
}
