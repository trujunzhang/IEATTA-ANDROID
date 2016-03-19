package org.ieatta.activity;

import org.ieatta.server.cache.ThumbnailImageUtil;

import java.io.File;
import java.util.List;

public class PhotoGalleryModel {
    public int galleryIndex;
    public List<File> galleryCollection;

    private String usedRef;

    public PhotoGalleryModel(String usedRef) {
        this.usedRef = usedRef;
        this.galleryCollection = ThumbnailImageUtil.sharedInstance.getImageFiles(usedRef);
    }

    public PhotoGalleryModel(List<File> galleryCollection) {
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
