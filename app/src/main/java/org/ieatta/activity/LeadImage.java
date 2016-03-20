package org.ieatta.activity;

import org.ieatta.database.query.OnlineDatabaseQuery;
import org.ieatta.server.cache.CacheImageUtil;

import java.io.File;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by djzhang on 3/20/16.
 */
public class LeadImage {
    public String localUrl;
    private String onlineUrl;
    private String photoUUID;

    public LeadImage(String localUrl) {
        this.localUrl = localUrl;
        this.photoUUID = new File(localUrl).getName().split("_")[1];
    }

    public Task<String> getOnlineUrl() {
        // Already cached in the local.
        File cacheImageFile = CacheImageUtil.sharedInstance.getCacheImageUrl(this.photoUUID);
        if (cacheImageFile != null && cacheImageFile.exists()) {
            return Task.forResult(String.format("file://%s", cacheImageFile.getAbsolutePath()));
        }
        return OnlineDatabaseQuery.downloadOriginalPhoto(this.photoUUID).onSuccessTask(new Continuation<Void, Task<String>>() {
            @Override
            public Task<String> then(Task<Void> task) throws Exception {
                return Task.forResult(String.format("file://%s", CacheImageUtil.sharedInstance.getCacheImageUrl(LeadImage.this.photoUUID).getAbsolutePath()));
            }
        });
    }
}
