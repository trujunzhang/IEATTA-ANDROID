package org.ieatta.activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;

import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.query.LocalDatabaseQuery;

import bolts.Continuation;
import bolts.Task;

public class PageProperties {

    private final String displayTitleText;
    public PhotoGalleryModel photoGalleryModel;

    /**
     * @return Nullable URL with no scheme. For example, foo.bar.com/ instead of
     *         http://foo.bar.com/.
     */
    @Nullable
    public String getLeadImageUrl() {
        return this.photoGalleryModel.next();
    }

    public PageProperties(PhotoGalleryModel photoGalleryModel,String displayTitleText) {
        this.photoGalleryModel = photoGalleryModel;
        this.displayTitleText = displayTitleText;
    }

    public String getDisplayTitle() {
        return displayTitleText;
    }
}
