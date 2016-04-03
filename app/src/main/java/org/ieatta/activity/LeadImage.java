package org.ieatta.activity;

import android.text.TextUtils;

import org.ieatta.database.query.OnlineDatabaseQuery;
import org.ieatta.server.cache.CacheImageUtil;

import java.io.File;

import bolts.Continuation;
import bolts.Task;

public class LeadImage {
    private String localUrl;
    private String onlineUrl;
    private String photoUUID;
    private boolean isCached;

    public LeadImage(String localUrl) {
        this.localUrl = localUrl;
        if(TextUtils.isEmpty(localUrl) ==false)
            this.photoUUID = new File(localUrl).getName().split("_")[1];
        this.isCached = false;
    }

    public LeadImage(String localUrl, String onlineUrl) {
        this.localUrl = localUrl;
        this.onlineUrl = onlineUrl;
    }

    public boolean isCached(){
        return this.isCached;
    }

    public Task<String> getLocalUrlTask() {
        // Already cached in the local.
        File cacheImageFile = CacheImageUtil.sharedInstance.getCacheImageUrl(this.photoUUID);
        if (cacheImageFile != null && cacheImageFile.exists()) {
            this.isCached = true;
            return Task.forResult(String.format("file://%s", cacheImageFile.getAbsolutePath()));
        }
        // Return the local url.
        return Task.forResult(this.localUrl);
    }

    public String getLocalUrl(){
        return this.localUrl;
    }

    public String getOnlineUrl(){
        return this.onlineUrl;
    }

    public Task<String> getOnlineUrlTask() {
        if(this.isCached == true){
            return Task.forResult(null);
        }
        return OnlineDatabaseQuery.downloadOriginalPhoto(this.photoUUID).onSuccessTask(new Continuation<Void, Task<String>>() {
            @Override
            public Task<String> then(Task<Void> task) throws Exception {
                return Task.forResult(String.format("file://%s", CacheImageUtil.sharedInstance.getCacheImageUrl(LeadImage.this.photoUUID).getAbsolutePath()));
            }
        });
    }
}
