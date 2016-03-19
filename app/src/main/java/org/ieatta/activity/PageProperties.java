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

    public class LeadImage{
        public String localUrl;
        private String onlineUrl;

        public Task<String> getOnlineUrl(String usedRef){
            if(TextUtils.isEmpty(onlineUrl)){
                return LocalDatabaseQuery.getPhoto(usedRef).onSuccessTask(new Continuation<DBPhoto, Task<String>>() {
                    @Override
                    public Task<String> then(Task<DBPhoto> task) throws Exception {
                        LeadImage.this.onlineUrl = task.getResult().getOriginalUrl();
                        return Task.forResult(onlineUrl);
                    }
                });
            }

            return Task.forResult(onlineUrl);
        }
    }

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

    public PageProperties(String leadImageUrl, String displayTitleText) {
//        this.leadImageUrl = leadImageUrl;
        this.displayTitleText = displayTitleText;
    }

    public PageProperties(PhotoGalleryModel photoGalleryModel,String displayTitleText) {
        this.photoGalleryModel = photoGalleryModel;
//        leadImageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c7/National_Emblem_of_Afghanistan_03.png/640px-National_Emblem_of_Afghanistan_03.png";
        this.displayTitleText = displayTitleText;
    }

    public String getDisplayTitle() {
        return displayTitleText;
    }
}
