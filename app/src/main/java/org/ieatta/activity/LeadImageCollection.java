package org.ieatta.activity;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import bolts.Task;

public class LeadImageCollection {
    public int galleryIndex;
    public List<LeadImage> leadImages;

    private String usedRef;

    public LeadImageCollection(List<File> galleryCollection, String usedRef) {
        this.usedRef = usedRef;
        this.leadImages = new LinkedList<>();
        for (File file : galleryCollection) {
            LeadImage leadImage = new LeadImage(String.format("file://%s", file.getAbsolutePath()));
            this.leadImages.add(leadImage);
        }
    }

    public LeadImageCollection(List<LeadImage> leadImages) {
        this.leadImages = leadImages;
        this.usedRef = usedRef;
    }

    public Task<String> leadImageLocal() {
        if (leadImages.size() == 0) {
            return Task.forError(new Exception("Lead Images is empty!"));
        }
        LeadImage leadImage = leadImages.get(this.galleryIndex);
        return leadImage.getLocalUrlTask();
    }

    public Task<String> leadImageOnline() {
        if (leadImages.size() == 0) {
            return Task.forError(new Exception("Lead Images is empty!"));
        }
        LeadImage leadImage = leadImages.get(this.galleryIndex);
        return leadImage.getOnlineUrlTask();
    }

    public void nextLeadImage() {
        this.galleryIndex = ((galleryIndex + 1) % leadImages.size());
    }

    public boolean isCached() {
        if (leadImages.size() == 0) {
            return true;
        }
        LeadImage leadImage = leadImages.get(this.galleryIndex);
        return leadImage.isCached();
    }

    public LeadImage getCurrentLeadImage() {
        if (leadImages.size() == 0) {
            return null;
        }
        return leadImages.get(this.galleryIndex);
    }

    public int getLeadImageCount() {
        return this.leadImages.size();
    }
}
