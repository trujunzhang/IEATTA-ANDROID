package org.ieatta.activity;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.text.TextUtils;

import com.parse.ParseFile;

import org.ieatta.IEAApp;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.database.query.OnlineDatabaseQuery;
import org.ieatta.server.cache.CacheImageUtil;

import bolts.Continuation;
import bolts.Task;

public class PhotoGalleryModel {
    public int galleryIndex;
    public List<LeadImage> leadImages;

    private String usedRef;

    class LeadImage {
        public String localUrl;
        private String onlineUrl;
        private String photoUUID;

        public LeadImage(String filePath) {
            this.localUrl = String.format("file://%s", filePath);
            this.photoUUID = new File(filePath).getName().split("_")[1];
        }

        public Task<String> getOnlineUrl() {
            // Already cached in the local.
            File cacheImageFile = CacheImageUtil.sharedInstance.getCacheImageUrl(this.photoUUID);
            if (cacheImageFile != null && cacheImageFile.exists()) {
                return Task.forResult(String.format("file://%s", cacheImageFile.getAbsolutePath()));
            }
            return OnlineDatabaseQuery.downloadOriginalPhoto(this.photoUUID).onSuccessTask(new Continuation<Void, Task<String>>() {
                @Override
                public Task<String> then(Task<Void> task) throws Exception {
                    return Task.forResult(String.format("file://%s", CacheImageUtil.sharedInstance.getCacheImageUrl(LeadImage.this.photoUUID).getAbsolutePath()));
                }
            });
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
        LeadImage leadImage = leadImages.get(this.galleryIndex);
        return Task.forResult(leadImage.localUrl);
    }

    public Task<String> leadImageOnline() {
        if (leadImages.size() == 0) {
            return Task.forError(new Exception("Lead Images is empty!"));
        }
        LeadImage leadImage = leadImages.get(this.galleryIndex);
        return leadImage.getOnlineUrl();
    }

    public void nextLeadImage() {
        this.galleryIndex = ((galleryIndex + 1) % leadImages.size());
    }

}
