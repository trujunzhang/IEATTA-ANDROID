package org.ieatta.database.query;


import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.ieatta.database.provide.PQueryModelType;
import org.ieatta.parse.DBConstant;
import org.ieatta.parse.ParseQueryUtil;
import org.ieatta.server.cache.CacheImageUtil;
import org.ieatta.server.cache.OriginalImageUtil;

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
        ParseQuery<ParseObject> query = ParseQueryUtil.createQueryForPhoto(uuid, PQueryModelType.Photo);

        return query.getFirstInBackground().onSuccessTask(new Continuation<ParseObject, Task<InputStream>>() {
            @Override
            public Task<InputStream> then(Task<ParseObject> task) throws Exception {
                final ParseFile originalFile = task.getResult().getParseFile(DBConstant.kPAPFieldOriginalImageKey);
                return originalFile.getDataStreamInBackground();
            }
        }).onSuccessTask(new Continuation<InputStream, Task<Void>>() {
            @Override
            public Task<Void> then(Task<InputStream> task) throws Exception {
                return CacheImageUtil.sharedInstance.saveTakenPhoto(task.getResult(),uuid);
            }
        });
    }
}
