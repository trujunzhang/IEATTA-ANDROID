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
        return leadImageUrl;
    }

    public PageProperties() {
        leadImageUrl = "";
    }
}
