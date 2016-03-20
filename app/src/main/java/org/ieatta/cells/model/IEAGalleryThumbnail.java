package org.ieatta.cells.model;

import org.ieatta.activity.PageTitle;
import org.ieatta.activity.gallery.GalleryCollection;
import org.ieatta.activity.gallery.GalleryItem;
import org.ieatta.database.models.DBPhoto;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.realm.RealmResults;

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
