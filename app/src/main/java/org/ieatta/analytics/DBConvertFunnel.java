package org.ieatta.analytics;

import org.ieatta.IEAApp;
import org.wikipedia.analytics.Funnel;

public class DBConvertFunnel extends Funnel {

    private static final String SCHEMA_NAME = "DBConvertFunnel";
    private static final int REV_ID = 101009;


    public DBConvertFunnel() {
        super(IEAApp.getInstance(), SCHEMA_NAME, REV_ID, SAMPLE_LOG_1K);
    }

    public void logToGalleryItem(String cellName,String info) {
        log(cellName, info);
    }

    public void logToLeadImageCollection(String localUrl,String onlineUrl) {
        log("local url",localUrl,"online url",onlineUrl);
    }
}
