package org.ieatta.analytics;

import org.ieatta.IEAApp;
import org.wikipedia.analytics.Funnel;

import java.io.File;

public class SyncHandlerFunnel extends Funnel {

    private static final String SCHEMA_NAME = "SyncHandlerFunnel";
    private static final int REV_ID = 101004;

    public SyncHandlerFunnel(IEAApp app, String schemaName, int revision) {
        super(app, SCHEMA_NAME, REV_ID, SAMPLE_LOG_1K);
    }

    public SyncHandlerFunnel() {
        super(IEAApp.getInstance(), SCHEMA_NAME, REV_ID, SAMPLE_LOG_1K);
    }

    public void logFetchFromServer(int size) {
        log("Get count after pulling objects from Server: ", size);
    }

    public void logDownloadThumbnail(String thumbnailUrl) {
        log("Download thumbnail url", thumbnailUrl);
    }

    public void logCacheThumbnail(File thumbnailFile) {
        log("Cached thumbnail path", thumbnailFile.getAbsolutePath());
    }

    public void logError(String localizedMessage) {
        log("Error when async database: ", localizedMessage);
    }

    public void logSuccess() {
        log("SyncHandler: ", "Async database task end successfully!");
    }
}
