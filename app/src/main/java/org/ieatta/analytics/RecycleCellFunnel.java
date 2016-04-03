package org.ieatta.analytics;

import org.ieatta.IEAApp;
import org.wikipedia.analytics.Funnel;

public class RecycleCellFunnel extends Funnel {

    private static final String SCHEMA_NAME = "RecycleCellFunnel";
    private static final int REV_ID = 101008;

    public RecycleCellFunnel(IEAApp app, String schemaName, int revision) {
        super(app, SCHEMA_NAME, REV_ID, SAMPLE_LOG_1K);
    }

    public RecycleCellFunnel() {
        super(IEAApp.getInstance(), SCHEMA_NAME, REV_ID, SAMPLE_LOG_1K);
    }

    public void logCellInfo(String cellName,String info) {
        log(cellName, info);
    }

    public void logOtherReviewsCount(int otherCount) {
        log("other review count", otherCount);
    }

    public void logArticleHeaderViewRatingCount(int rating) {
        log("rating on the article view",rating);
    }
}
