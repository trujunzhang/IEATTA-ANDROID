package org.ieatta.activity;

import org.ieatta.server.cache.BaseImageUtil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import bolts.Task;

public class LeadImageCollection {
    public List<LeadImage> leadImages;
    public int galleryIndex;

    private String usedRef;

    public LeadImageCollection(List<File> galleryCollection, String usedRef) {
        this.usedRef = usedRef;
        this.leadImages = new LinkedList<>();
        for (File file : galleryCollection) {
            this.leadImages.add(new LeadImage(BaseImageUtil.getLocalAbstractPath(file)));
        }
    }

    public LeadImageCollection(List<LeadImage> leadImages) {
        this.leadImages = leadImages;
        this.usedRef = usedRef;
    }


    public void nextLeadImage() {
        this.galleryIndex = ((galleryIndex + 1) % leadImages.size());
    }

    public LeadImage getCurrentLeadImage() {
        if (leadImages.size() == 0)
            return null;

        return leadImages.get(this.galleryIndex);
    }

    public int getLeadImageCount() {
        return this.leadImages.size();
    }
}
