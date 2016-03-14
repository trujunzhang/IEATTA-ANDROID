package org.ieatta.analytics;

import org.ieatta.IEAApp;
import org.wikipedia.analytics.Funnel;

public class ServerTaskFunnel extends Funnel {

    private static final String SCHEMA_NAME = "PhotoFunnel";
    private static final int REV_ID = 101003;

    public ServerTaskFunnel(IEAApp app, String schemaName, int revision) {
        super(app, SCHEMA_NAME, REV_ID, SAMPLE_LOG_1K);
    }

    public ServerTaskFunnel(){
        super(IEAApp.getInstance(), SCHEMA_NAME, REV_ID, SAMPLE_LOG_1K);
    }

    public void logFetchFromServer(int size) {
        log("Get count after pulling objects from Server: ",size);
    }
}
