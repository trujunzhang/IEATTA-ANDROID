package org.wikipedia.analytics;

import org.wikipedia.Site;
import org.ieatta.IEAApp;

import java.util.Hashtable;

/**
 * Creates and stores analytics tracking funnels.
 */
public class FunnelManager {
    private final IEAApp app;
    private final Hashtable<Site, SavedPagesFunnel> savedPageFunnels = new Hashtable<>();

    public FunnelManager(IEAApp app) {
        this.app = app;
    }


    public SavedPagesFunnel getSavedPagesFunnel(Site site) {
        if (!savedPageFunnels.contains(site)) {
            savedPageFunnels.put(site, new SavedPagesFunnel(app, site));
        }

        return savedPageFunnels.get(site);
    }
}
