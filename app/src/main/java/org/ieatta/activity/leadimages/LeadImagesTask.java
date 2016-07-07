package org.ieatta.activity.leadimages;


import android.support.annotation.Nullable;

import org.ieatta.activity.LeadImage;
import org.ieatta.activity.LeadImageCollection;
import org.wikipedia.concurrency.RecurringTask;

import bolts.Continuation;
import bolts.Task;

public class LeadImagesTask {
    public static LeadImagesTask instance;

    static {
        instance = new LeadImagesTask();
    }

    private LeadImageCollection leadImageCollection;

    private LeadImagesHandler leadImagesHandler;
    private RecurringTask recurringTask;

    private LeadImagesTask() {

    }

    public void setLeadImageCollection(LeadImageCollection leadImageCollection) {
        this.leadImageCollection = leadImageCollection;
    }

    public boolean isCached() {
        return this.leadImageCollection.isCached();
    }

    public void carouselLeadImages() {
        this.closeCarouselTask();

        if (this.leadImageCollection.getLeadImageCount() == 1) {
            this.refreshLeadImage();
        } else {
            // set the page title text, and honor any HTML formatting in the title
            recurringTask.periodicTask(new RecurringTask.RecurringEvent() {
                @Override
                public void everyTask() {
                    LeadImagesTask.this.refreshLeadImage();
                }
            }, 0, 20);
        }
    }

    private void closeCarouselTask() {
        if (this.recurringTask != null)
            this.recurringTask.closeTask();

        this.recurringTask = new RecurringTask();
    }

    public void refreshLeadImage() {
        LeadImage currentLeadImage = this.leadImageCollection.getCurrentLeadImage();
//        this.leadImagesHandler.loadLeadImage(currentLeadImage);
//        this.leadImageCollection.nextLeadImage();
    }

    public void setLeadImagesHandler(LeadImagesHandler leadImagesHandler) {
        this.leadImagesHandler = leadImagesHandler;
    }


}
