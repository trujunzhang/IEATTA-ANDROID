package org.ieatta.activity;

import android.support.annotation.Nullable;

import org.ieatta.activity.leadimages.LeadImagesTask;

import bolts.Task;

public class PageProperties {

    private final String displayTitleText;

    private LeadMapView leadMapView;

    public LeadMapView getLeadMapView() {
        return leadMapView;
    }

    public PageProperties(LeadImageCollection leadImageCollection, String displayTitleText, LeadMapView leadMapView) {
        LeadImagesTask.instance.setLeadImageCollection(leadImageCollection);

        this.displayTitleText = displayTitleText;
        this.leadMapView = leadMapView;
    }

    public String getDisplayTitle() {
        return displayTitleText;
    }
}
