package org.ieatta.cells.model;


import org.ieatta.activity.gallery.GalleryCollection;

public class IEAGalleryThumbnail {
    private GalleryCollection result;

    public IEAGalleryThumbnail(GalleryCollection result) {
        this.result = result;
    }

    public GalleryCollection getResult() {
        return result;
    }

    public void setResult(GalleryCollection result) {
        this.result = result;
    }
}
