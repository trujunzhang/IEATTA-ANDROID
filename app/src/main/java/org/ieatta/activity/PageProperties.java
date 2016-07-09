package org.ieatta.activity;

import org.ieatta.tasks.FragmentTask;

import java.util.List;

public class PageProperties {

    private final String displayTitleText;
    private final List<LeadImage> leadImages;

    private LeadMapView leadMapView;
    private FragmentTask fragmentTask;

    public LeadMapView getLeadMapView() {
        return leadMapView;
    }

    public PageProperties(LeadImageCollection leadImageCollection, String displayTitleText, LeadMapView leadMapView, FragmentTask fragmentTask) {
        this.leadImages = leadImageCollection.leadImages;

        this.displayTitleText = displayTitleText;
        this.leadMapView = leadMapView;
        this.fragmentTask = fragmentTask;
    }

    public String getDisplayTitle() {
        return displayTitleText;
    }

    public List<LeadImage> getLeadImages() {
        return leadImages;
    }

    public FragmentTask getFragmentTask() {
        return this.fragmentTask;
    }
}
