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

    public void logArticleHeaderViewScrollY(int scrollY) {
        log("scrollY in the ArticleHeaderView", scrollY);
    }

    public void logCacheThumbnail(File thumbnailFile) {
        log("Cached thumbnail path", thumbnailFile.getAbsolutePath());
    }
}
