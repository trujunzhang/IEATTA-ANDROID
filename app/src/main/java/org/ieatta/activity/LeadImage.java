package org.ieatta.activity;

import android.text.TextUtils;

import org.ieatta.database.query.OnlineDatabaseQuery;
import org.ieatta.server.cache.BaseImageUtil;
import org.ieatta.server.cache.CacheImageUtil;

import java.io.File;

import bolts.Continuation;
import bolts.Task;

public class LeadImage {
    private String localUrl;
    private String onlineUrl;
    private String imageUUID;

    public LeadImage(String localUrl, String onlineUrl, String imageUUID) {
        this.localUrl = localUrl;
        this.onlineUrl = onlineUrl;
        this.imageUUID = imageUUID;
    }


    public String getLocalUrl() {
        return this.localUrl;
    }

    public String getOnlineUrl() {
        return this.onlineUrl;
    }

    public String getImageUUID() {
        return this.imageUUID;
    }

}
