package org.ieatta.activity;

import org.ieatta.server.cache.ThumbnailImageUtil;

import java.io.File;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;

import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.query.LocalDatabaseQuery;

import bolts.Continuation;
import bolts.Task;

public class PhotoGalleryModel {
    public int galleryIndex;
    public List<LeadImage> leadImages;

    private String usedRef;

    class LeadImage {
        public String localUrl;
        private String onlineUrl;

        public LeadImage(String filePath) {
            this.localUrl = String.format("file://%s", filePath);
        }

        public Task<String> getOnlineUrl(String usedRef) {
            if (TextUtils.isEmpty(onlineUrl)) {
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

    public PhotoGalleryModel(List<File> galleryCollection, String usedRef) {
        this.usedRef = usedRef;
        this.leadImages = new LinkedList<>();
        for (File file : galleryCollection) {
            LeadImage leadImage = new LeadImage(file.getAbsolutePath());
            this.leadImages.add(leadImage);
        }
    }

    public Task<String> leadImageLocal() {
        if (leadImages.size() == 0) {
            return Task.forError(new Exception("Lead Images is empty!"));
        }
        int index = galleryIndex % leadImages.size();
        LeadImage leadImage = leadImages.get(index);
        return Task.forResult(leadImage.localUrl);
    }

    public Task<String> leadImageOnline() {
        if (leadImages.size() == 0) {
            return Task.forError(new Exception("Lead Images is empty!"));
        }
        int index = galleryIndex % leadImages.size();
        LeadImage leadImage = leadImages.get(index);
        return  leadImage.getOnlineUrl(this.usedRef);
    }

}
