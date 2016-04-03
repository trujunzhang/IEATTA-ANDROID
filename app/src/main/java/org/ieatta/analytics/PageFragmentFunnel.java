package org.ieatta.analytics;

import org.ieatta.IEAApp;
import org.wikipedia.analytics.Funnel;

import java.io.File;

public class PageFragmentFunnel extends Funnel {

    private static final String SCHEMA_NAME = "PageFragmentFunnel";
    private static final int REV_ID = 101020;

    public PageFragmentFunnel() {
        super(IEAApp.getInstance(), SCHEMA_NAME, REV_ID, SAMPLE_LOG_1K);
    }

    public void logArticleHeaderViewScrollY(int scrollY, int offset) {
        log("scrollY in the ArticleHeaderView", scrollY, "offset", offset);
    }

    public void logLoadMapView(String status) {
        log("Load map view", status);
    }

    public void logLoadLeadImage(String status) {
        log("Load Lead Image", status);
    }
}
