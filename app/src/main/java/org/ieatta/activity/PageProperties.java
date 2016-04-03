package org.ieatta.activity;

import android.support.annotation.Nullable;

import bolts.Task;

public class PageProperties {

    private final String displayTitleText;
    public LeadImageCollection leadImageCollection;
    private LeadMapView leadMapView;

    /**
     * @return Nullable URL with no scheme. For example, foo.bar.com/ instead of
     *         http://foo.bar.com/.
     */
    @Nullable
    public Task<String> getLeadImageLocalUrl() {
        return this.leadImageCollection.leadImageLocal();
    }

    @Nullable
    public Task<String> getLeadImageOnlineUrl() {
        return this.leadImageCollection.leadImageOnline();
    }

    public Task<LeadImage> getCurrentLeadImage() {
        return Task.forResult(this.leadImageCollection.getCurrentLeadImage());
    }

    public LeadMapView getLeadMapView(){
        return leadMapView;
    }

    public boolean isCached(){
        return this.leadImageCollection.isCached();
    }

    public void nextLeadImage(){
        this.leadImageCollection.nextLeadImage();
    }

    public PageProperties(LeadImageCollection leadImageCollection,String displayTitleText,LeadMapView leadMapView) {
        this.leadImageCollection = leadImageCollection;
        this.displayTitleText = displayTitleText;
        this.leadMapView = leadMapView;
    }

    public String getDisplayTitle() {
        return displayTitleText;
    }

    public int getLeadImageCount() {
        return this.leadImageCollection.getLeadImageCount();
    }
}
