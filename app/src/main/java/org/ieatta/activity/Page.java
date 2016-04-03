package org.ieatta.activity;

import org.ieatta.activity.gallery.GalleryCollection;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

public class Page {

    @VisibleForTesting
    static final int MEDIAWIKI_ORIGIN = 0;
    @VisibleForTesting
    static final int RESTBASE_ORIGIN = 1;

    private final PageTitle title;
    private final PageProperties pageProperties;

    private boolean mapviewActivated = false;

    /**
     * The media gallery collection associated with this page.
     * This will be populated by the Gallery activity when necessary, and will be kept in
     * the page cache because the page itself is cached. Subsequent instances of the Gallery
     * activity will then be able to retrieve the page's gallery collection from cache.
     */
    private GalleryCollection galleryCollection;

    /** Regular constructor */
    public Page(@NonNull PageTitle title,
                @NonNull PageProperties pageProperties) {
        this.title = title;
        this.pageProperties = pageProperties;
    }

    public Page() {
        this.title = null;
        this.pageProperties = null;
    }

    public String getDisplayTitle() {
        return pageProperties.getDisplayTitle();
    }

    public PageProperties getPageProperties() {
        return pageProperties;
    }

    public PageTitle getTitle() {
        return title;
    }

    public GalleryCollection getGalleryCollection() {
        return galleryCollection;
    }

    public void setGalleryCollection(GalleryCollection collection) {
        galleryCollection = collection;
    }

    public boolean toggleMapView(){
        boolean last = mapviewActivated;
        mapviewActivated = !mapviewActivated;
        return last;
    }
}
