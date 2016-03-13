package org.ieatta.analytics;

import org.ieatta.IEATTAApp;
import org.wikipedia.analytics.Funnel;

import java.io.File;

public class ServerTaskFunnel extends Funnel {

    private static final String SCHEMA_NAME = "PhotoFunnel";
    private static final int REV_ID = 101003;

    public ServerTaskFunnel(IEATTAApp app, String schemaName, int revision) {
        super(app, SCHEMA_NAME, REV_ID, SAMPLE_LOG_1K);
    }

    public ServerTaskFunnel(){
        super(IEATTAApp.getInstance(), SCHEMA_NAME, REV_ID, SAMPLE_LOG_1K);
    }

    public void logFetchFromServer(int size) {
        log("Get count after pulling objects from Server: ",size);
    }
}
