package org.ieatta.activity.leadimages;


public class MenuBarEventHandler {

    private final LeadImagesHandler leadImagesHandler;
    private final ArticleHeaderView articleHeaderView;

    public MenuBarEventHandler(LeadImagesHandler leadImagesHandler, ArticleHeaderView articleHeaderView) {
        this.leadImagesHandler = leadImagesHandler;
        this.articleHeaderView = articleHeaderView;
    }

    public void toggleMapView(boolean isMapShowing) {
        leadImagesHandler.updateNavigate(isMapShowing);
        articleHeaderView.getHeaderMapView().toggleMapView(isMapShowing);
    }
}
