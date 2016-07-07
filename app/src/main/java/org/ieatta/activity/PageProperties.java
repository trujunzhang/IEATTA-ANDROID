package org.ieatta.activity;

import android.support.annotation.Nullable;

import org.ieatta.activity.leadimages.LeadImagesTask;

import java.util.List;

import bolts.Task;

public class PageProperties {

    private final String displayTitleText;
    private final List<LeadImage> leadImages;


    private LeadMapView leadMapView;

    public LeadMapView getLeadMapView() {
        return leadMapView;
    }

    public PageProperties(LeadImageCollection leadImageCollection, String displayTitleText, LeadMapView leadMapView) {
        this.leadImages = leadImageCollection.leadImages;

        this.displayTitleText = displayTitleText;
        this.leadMapView = leadMapView;
    }

    public String getDisplayTitle() {
        return displayTitleText;
    }

    public List<LeadImage> getLeadImages() {
        return leadImages;
    }
}
