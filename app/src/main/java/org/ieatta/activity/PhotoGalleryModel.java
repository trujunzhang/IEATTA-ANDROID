package org.ieatta.activity;

import java.io.File;
import java.util.List;

public class PhotoGalleryModel {
    public int galleryIndex;
    public List<File> galleryCollection;

    public PhotoGalleryModel(List<File> galleryCollection) {
        this.galleryCollection = galleryCollection;
    }

    public String next(){
        int index = galleryIndex% galleryCollection.size();
        File file = galleryCollection.get(index);
        return String.format("file://%s", file.getAbsolutePath());
    }

}
