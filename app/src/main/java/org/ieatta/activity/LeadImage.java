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
    private boolean isCached;

    public LeadImage(String localUrl) {
        this.localUrl = localUrl;
//        if (!TextUtils.isEmpty(localUrl))
//            this.photoUUID = new File(localUrl).getName().split("_")[1];
        this.isCached = false;
    }

    public LeadImage(String localUrl, String onlineUrl) {
        this.localUrl = localUrl;
        this.onlineUrl = onlineUrl;
    }

    public String getLocalUrl() {
        return this.localUrl;
    }

    public String getOnlineUrl() {
        return this.onlineUrl;
    }

}
