package org.ieatta.activity;

import org.ieatta.server.cache.ThumbnailImageUtil;

import java.io.File;
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
    public List<File> galleryCollection;

    private String usedRef;

    class LeadImage{
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

    public PhotoGalleryModel(List<File> galleryCollection,String usedRef) {
        this.usedRef = usedRef;
        this.galleryCollection = galleryCollection;
    }

    public String next(){
        if(galleryCollection.size()==0){
            return null;
        }
        int index = galleryIndex% galleryCollection.size();
        File file = galleryCollection.get(index);
        return String.format("file://%s", file.getAbsolutePath());
    }

}
