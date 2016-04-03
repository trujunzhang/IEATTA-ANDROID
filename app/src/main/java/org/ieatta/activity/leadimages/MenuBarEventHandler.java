package org.ieatta.activity.leadimages;


public class MenuBarEventHandler {

    private final LeadImagesHandler leadImagesHandler;
    private final ArticleHeaderView articleHeaderView;

    public MenuBarEventHandler(LeadImagesHandler leadImagesHandler, ArticleHeaderView articleHeaderView) {
        this.leadImagesHandler = leadImagesHandler;
        this.articleHeaderView = articleHeaderView;
    }

    public void toggleMapView(boolean activated) {
        leadImagesHandler.updateNavigate(activated);
        articleHeaderView.getHeaderMapView().toggleMapView(activated);
    }
}
