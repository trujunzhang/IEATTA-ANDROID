package org.ieatta.activity;

import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

public class Page {

    @VisibleForTesting
    static final int MEDIAWIKI_ORIGIN = 0;
    @VisibleForTesting
    static final int RESTBASE_ORIGIN = 1;

    private final PageTitle title;
    private final PageProperties pageProperties;

    /** Regular constructor */
    public Page(@NonNull PageTitle title,
                @NonNull PageProperties pageProperties) {
        this.title = title;
        this.pageProperties = pageProperties;
    }

    public String getDisplayTitle() {
        return pageProperties.getDisplayTitle();
    }

    public PageProperties getPageProperties() {
        return pageProperties;
    }

    public PageTitle getTitle() {
        return title;
    }
}
