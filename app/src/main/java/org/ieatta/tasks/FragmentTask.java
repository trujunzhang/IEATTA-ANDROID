package org.ieatta.tasks;

import android.app.Activity;
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
import org.ieatta.cells.headerfooterview.IEAFooterView;
import org.ieatta.cells.headerfooterview.IEAHeaderView;
import org.ieatta.cells.model.IEAHeaderViewModel;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.views.ObservableWebView;
import org.wikipedia.util.DimenUtil;

import java.util.LinkedList;
import java.util.List;

import bolts.Task;
import io.realm.Realm;

public abstract class FragmentTask implements RecyclerOnItemClickListener, LeadImagesHandler.OnContentHeightChangedListener {
    protected String restaurantUUID;
    protected String eventUUID;
    protected String teamUUID;
    protected String recipeUUID;

    public RecycleViewManager manager;
    protected PageViewModel model;
    public HistoryEntry entry;
    protected Activity activity;
    private ObservableWebView webView;

    protected static List<Realm> realmList = new LinkedList<>();

    @VisibleForTesting
    public FragmentTask(HistoryEntry entry) {
        this.entry = entry;
        realmList = LocalDatabaseQuery.closeRealmList(realmList);
    }

    public FragmentTask(HistoryEntry entry, Activity activity, PageViewModel model) {
        this.entry = entry;
        this.activity = activity;
        this.model = model;
        this.manager = new RecycleViewManager(activity.getApplicationContext());
    }

    public abstract Task<Void> executeTask();

    public void prepareUI() {
        this.manager.setRegisterHeaderView(IEAHeaderView.getType());
        this.manager.setRegisterFooterView(IEAFooterView.getType());
    }

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

    protected GalleryThumbnailScrollView.GalleryViewListener galleryViewListener
            = new GalleryThumbnailScrollView.GalleryViewListener() {
        @Override
        public void onGalleryItemClicked(String imageUUID) {
            PageTitle imageTitle = new PageTitle(imageUUID);
            GalleryActivity.showGallery(activity, model.getTitle(), imageTitle,
                    GalleryFunnel.SOURCE_LINK_PREVIEW);
        }
    };

    public boolean isMainPage() {
        return false;
    }
}
