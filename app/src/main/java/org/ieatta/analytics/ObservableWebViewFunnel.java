package org.ieatta.analytics;

import org.ieatta.IEAApp;
import org.wikipedia.analytics.Funnel;

public class ObservableWebViewFunnel extends Funnel {

    private static final String SCHEMA_NAME = "ObservableWebViewFunnel";
    private static final int REV_ID = 101014;

    public ObservableWebViewFunnel(IEAApp app, String schemaName, int revision) {
        super(app, SCHEMA_NAME, REV_ID, SAMPLE_LOG_1K);
    }

    public ObservableWebViewFunnel() {
        super(IEAApp.getInstance(), SCHEMA_NAME, REV_ID, SAMPLE_LOG_1K);
    }

    public void logOnDraw(int contentHeight) {
        log("contentHeight on draw: ", contentHeight);
    }

    public void logOnScrollChanged(int oldTop, int top, boolean isHumanScroll) {
        log("oldTop: ", oldTop, "top", top, "isHumanScroll", isHumanScroll);
    }
}
