package org.wikipedia.analytics;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONObject;
import org.wikipedia.page.PageTitle;
import org.wikipedia.WikipediaApp;

import java.util.List;

public class SuggestedPagesFunnel extends Funnel {
    private static final String SCHEMA_NAME = "MobileWikiAppArticleSuggestions";
    private static final int REV_ID = 15302212;
    private static final int READ_MORE_SOURCE_FULL_TEXT = 0;
    private static final int READ_MORE_SOURCE_MORELIKE = 1;
    private static final int READ_MORE_SOURCE_OPENING_TEXT = 2;

    private int readMoreSource;
    private long latency;

    public SuggestedPagesFunnel(WikipediaApp app) {
        super(app, SCHEMA_NAME, REV_ID, Funnel.SAMPLE_LOG_100);
        this.readMoreSource = app.isFeatureReadMoreSearchOpeningTextEnabled() ? READ_MORE_SOURCE_OPENING_TEXT : READ_MORE_SOURCE_MORELIKE;
    }

    @Override
    protected JSONObject preprocessData(@NonNull JSONObject eventData) {
        preprocessData(eventData, "readMoreSource", readMoreSource);
        return super.preprocessData(eventData);
    }

    @Override protected void preprocessSessionToken(@NonNull JSONObject eventData) { }

    public void setLatency(long latency) {
        this.latency = latency;
    }

    public void logSuggestionsShown(PageTitle currentPageTitle, List<PageTitle> suggestedTitles) {
        log(
                currentPageTitle.getSite(),
                "action", "shown",
                "pageTitle", currentPageTitle.getDisplayText(),
                "readMoreList", TextUtils.join("|", suggestedTitles),
                "latency", latency
        );
    }

    public void logSuggestionClicked(PageTitle currentPageTitle, List<PageTitle> suggestedTitles,
                                     int clickedIndex) {
        log(
                currentPageTitle.getSite(),
                "action", "clicked",
                "pageTitle", currentPageTitle.getDisplayText(),
                "readMoreList", TextUtils.join("|", suggestedTitles),
                "readMoreIndex", clickedIndex
        );
    }
}
