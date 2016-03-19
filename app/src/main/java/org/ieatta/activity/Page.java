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

    /** Regular constructor */
    public Page(@NonNull PageTitle title) {
        this.title = title;
    }



}
