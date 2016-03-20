package org.ieatta.activity;

import android.support.annotation.Nullable;

import bolts.Task;

public class PageProperties {

    private final String displayTitleText;
    public LeadImagesModel leadImagesModel;

    /**
     * @return Nullable URL with no scheme. For example, foo.bar.com/ instead of
     *         http://foo.bar.com/.
     */
    @Nullable
    public Task<String> getLeadImageLocalUrl() {
        return this.leadImagesModel.leadImageLocal();
    }

    @Nullable
    public Task<String> getLeadImageOnlineUrl() {
        return this.leadImagesModel.leadImageOnline();
    }

    public boolean isCached(){
        return this.leadImagesModel.isCached();
    }

    public void nextLeadImage(){
        this.leadImagesModel.nextLeadImage();
    }

    public PageProperties(LeadImagesModel leadImagesModel,String displayTitleText) {
        this.leadImagesModel = leadImagesModel;
        this.displayTitleText = displayTitleText;
    }

    public String getDisplayTitle() {
        return displayTitleText;
    }
}
