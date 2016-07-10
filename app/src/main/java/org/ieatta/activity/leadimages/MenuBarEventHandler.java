package org.ieatta.activity.leadimages;


import org.ieatta.activity.Page;

public class MenuBarEventHandler {

    private final LeadImagesHandler leadImagesHandler;
    private final ArticleHeaderView articleHeaderView;

    public MenuBarEventHandler(LeadImagesHandler leadImagesHandler, ArticleHeaderView articleHeaderView) {
        this.leadImagesHandler = leadImagesHandler;
        this.articleHeaderView = articleHeaderView;
    }

    public void toggleMapView(Page page) {
        boolean activated = page.toggleMapView();

        // Toggle map icon on the menu bar.
        leadImagesHandler.updateNavigate(activated);
        // Toggle map view on the header view map.
        articleHeaderView.getHeaderMapView().toggleMapView(activated, page.getMapInfo());
    }
}
