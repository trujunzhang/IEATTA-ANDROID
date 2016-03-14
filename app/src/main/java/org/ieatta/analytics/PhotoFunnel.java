package org.ieatta.analytics;

import org.ieatta.IEAApp;
import org.wikipedia.analytics.Funnel;

import java.io.File;

public class PhotoFunnel extends Funnel {

    private static final String SCHEMA_NAME = "ServerTaskFunnel";
    private static final int REV_ID = 101002;

    public PhotoFunnel(IEAApp app, String schemaName, int revision) {
        super(app, SCHEMA_NAME, REV_ID, SAMPLE_LOG_1K);
    }

    public PhotoFunnel(){
        super(IEAApp.getInstance(), SCHEMA_NAME, REV_ID, SAMPLE_LOG_1K);
    }

    public void logDownloadThumbnail(String thumbnailUrl) {
         log("Download thumbnail url",thumbnailUrl);
    }

    public void logCacheThumbnail(File thumbnailFile) {
         log("Cached thumbnail path",thumbnailFile.getAbsolutePath());
    }
}
