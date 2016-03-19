package org.ieatta.activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

public class PageProperties {

    @Nullable private final String leadImageUrl;
    /**
     * @return Nullable URL with no scheme. For example, foo.bar.com/ instead of
     *         http://foo.bar.com/.
     */
    @Nullable
    public String getLeadImageUrl() {
        //return leadImageUrl;
        return "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c7/National_Emblem_of_Afghanistan_03.png/640px-National_Emblem_of_Afghanistan_03.png";
    }

    public PageProperties() {
        leadImageUrl = "";
    }
}
