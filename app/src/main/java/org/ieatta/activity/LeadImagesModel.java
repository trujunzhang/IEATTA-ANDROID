package org.ieatta.activity;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import bolts.Task;

public class LeadImagesModel {
    public int galleryIndex;
    public List<LeadImage> leadImages;

    private String usedRef;

    public LeadImagesModel(List<File> galleryCollection, String usedRef) {
        this.usedRef = usedRef;
        this.leadImages = new LinkedList<>();
        for (File file : galleryCollection) {
            LeadImage leadImage = new LeadImage(String.format("file://%s", file.getAbsolutePath()));
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
