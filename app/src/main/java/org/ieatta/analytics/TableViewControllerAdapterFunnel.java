package org.ieatta.analytics;

import org.ieatta.IEAApp;
import org.wikipedia.analytics.Funnel;

public class TableViewControllerAdapterFunnel extends Funnel {

    private static final String SCHEMA_NAME = "TableViewControllerAdapterFunnel";
    private static final int REV_ID = 101012;


    public TableViewControllerAdapterFunnel() {
        super(IEAApp.getInstance(), SCHEMA_NAME, REV_ID, SAMPLE_LOG_1K);
    }

    public void logItemCount(int count) {
        log("getItemCount",count);
    }
}
