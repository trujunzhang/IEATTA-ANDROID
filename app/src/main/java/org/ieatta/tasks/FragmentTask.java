package org.ieatta.tasks;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.tableview.RecycleViewManager;
import com.tableview.adapter.RecyclerOnItemClickListener;

import org.ieatta.activity.PageTitle;
import org.ieatta.activity.PageViewModel;
import org.ieatta.activity.gallery.GalleryActivity;
import org.ieatta.activity.gallery.GalleryThumbnailScrollView;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.activity.leadimages.LeadImagesHandler;
import org.ieatta.analytics.GalleryFunnel;
import org.ieatta.cells.model.IEAHeaderViewModel;
import org.ieatta.views.ObservableWebView;
import org.wikipedia.util.DimenUtil;

import bolts.Task;

public abstract class FragmentTask implements RecyclerOnItemClickListener,LeadImagesHandler.OnContentHeightChangedListener {

    protected RecycleViewManager manager;
    protected PageViewModel model;
    public HistoryEntry entry;
    private Activity activity;
    private ObservableWebView webView;

    @VisibleForTesting
    public FragmentTask(HistoryEntry entry) {
        this.entry = entry;
    }

    public FragmentTask(HistoryEntry entry, Activity activity, PageViewModel model) {
        this.entry = entry;
        this.activity = activity;
        this.model = model;
        this.manager = new RecycleViewManager(activity.getApplicationContext());
    }

    public abstract Task<Void> executeTask();

    public abstract void prepareUI();

    public abstract void postUI();


    protected int getEmptyHeaderViewHeight() {
        return DimenUtil.getDisplayWidthPx();
    }


    public void setupWebView(ObservableWebView webView) {
        this.webView = webView;
        manager.startManagingWithDelegate(webView);
        manager.setOnItemClickListener(this);
    }

    @Override
    public void onContentHeightChanged(int contentHeight) {
        this.manager.updateHeaderItem(new IEAHeaderViewModel(contentHeight));
    }


    public int getWebViewScrollY(ObservableWebView webView) {
        return webView.getLastTop();
    }


    protected GalleryThumbnailScrollView.GalleryViewListener galleryViewListener
            = new GalleryThumbnailScrollView.GalleryViewListener() {
        @Override
        public void onGalleryItemClicked(String imageUUID) {
            PageTitle imageTitle = new PageTitle(imageUUID);
            GalleryActivity.showGallery(activity, model.getTitle(), imageTitle,
                    GalleryFunnel.SOURCE_LINK_PREVIEW);
        }
    };
}
