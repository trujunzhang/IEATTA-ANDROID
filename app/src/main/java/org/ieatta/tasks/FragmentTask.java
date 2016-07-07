package org.ieatta.tasks;

import android.app.Activity;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.widget.AdapterView;

import com.tableview.RecycleViewManager;
import com.tableview.TableViewControllerAdapter;
import com.tableview.adapter.NSIndexPath;

import com.tableview.storage.models.RowModel;
import com.tableview.utils.CollectionUtil;

import org.ieatta.R;
import org.ieatta.activity.PageActivity;
import org.ieatta.activity.PageFragment;
import org.ieatta.activity.PageTitle;
import org.ieatta.activity.PageViewModel;
import org.ieatta.activity.gallery.GalleryActivity;
import org.ieatta.activity.gallery.GalleryCollection;
import org.ieatta.activity.gallery.GalleryThumbnailScrollView;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.activity.leadimages.LeadImagesHandler;
import org.ieatta.activity.leadimages.MenuBarCallback;
import org.ieatta.activity.leadimages.MenuBarEventHandler;
import org.ieatta.activity.update.UpdateEntry;
import org.ieatta.analytics.GalleryFunnel;
import org.ieatta.analytics.RecycleCellFunnel;
import org.ieatta.cells.IEAGalleryThumbnailCell;
import org.ieatta.cells.header.IEAMoreReviewsFooterCell;
import org.ieatta.cells.header.IEAPhotoGalleryFooterCell;
import org.ieatta.cells.headerfooterview.IEAFooterView;
import org.ieatta.cells.headerfooterview.IEAHeaderView;
import org.ieatta.cells.model.EditCellModel;
import org.ieatta.cells.model.IEAGalleryThumbnail;
import org.ieatta.cells.model.IEAHeaderViewModel;
import org.ieatta.cells.model.IEAReviewsCellModel;
import org.ieatta.cells.model.SectionMoreReviewsFooterCellModel;
import org.ieatta.cells.model.SectionPhotoGalleryFooterCellModel;
import org.ieatta.cells.model.SectionTitleCellModel;
import org.ieatta.database.provide.ReviewType;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.database.query.ReviewQuery;
import org.ieatta.provide.IEAEditKey;
import org.ieatta.views.ObservableWebView;
import org.wikipedia.util.DimenUtil;

import java.util.LinkedList;
import java.util.List;

import bolts.Task;
import io.realm.Realm;

public abstract class FragmentTask extends MenuBarCallback implements AdapterView.OnItemClickListener, LeadImagesHandler.OnContentHeightChangedListener {
    protected String mRestaurantUUID;
    protected String mEventUUID;
    protected String mTeamUUID;
    protected String mRecipeUUID;

    public RecycleViewManager manager;
    protected PageViewModel model;
    public HistoryEntry entry;
    protected Activity activity;

    // For showing photo's thumbnail gallery.
    public GalleryCollection thumbnailGalleryCollection = new GalleryCollection();

    // For showing reviews list.
    public List<IEAReviewsCellModel> reviewsCellModelList;
    protected List<Realm> realmList = new LinkedList<>();
    protected ReviewQuery reviewQuery = new ReviewQuery();

    public void onItemClick(View view, NSIndexPath indexPath, Object model, int position, boolean isLongClick) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RowModel item = this.manager.getMemoryStorage().getItem(position);
        this.onItemClick(view, item.indexPath, item.model, position, false);
    }

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

    public Task<Void> executeUpdateTask(UpdateEntry entry) {
        return Task.forResult(null);
    }

    public Task<Void> executePhotosGalleryTask() {
        return Task.forResult(null);
    }

    public void prepareUI() {
        this.manager.setRegisterHeaderView(IEAHeaderView.getType());
        this.manager.setRegisterFooterView(IEAFooterView.getType());
    }

    public abstract void postUI();

    public void postPhotosGallery(int forSectionIndex) {
        this.manager.setRegisterCellClass(IEAGalleryThumbnailCell.getType(), forSectionIndex);
        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.PhotosGallery), forSectionIndex);
        this.manager.setSectionItems(CollectionUtil.createList(new IEAGalleryThumbnail(this.thumbnailGalleryCollection, this.galleryViewListener)), forSectionIndex);
        // this.manager.setFooterModelInSection(new SectionPhotoGalleryFooterCellModel(mRestaurantUUID, ReviewType.Review_Restaurant), forSectionIndex, IEAPhotoGalleryFooterCell.getType());
    }

    public void postReviews(int forSectionIndex, String reviewRef, ReviewType type, int limit) {
        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Reviews), forSectionIndex);
        this.manager.setSectionItems(this.reviewsCellModelList, forSectionIndex);

        int otherCount = this.reviewQuery.reviewsCount <= 0 ? 0 : (this.reviewQuery.reviewsCount - limit);
        new RecycleCellFunnel().logOtherReviewsCount(otherCount);
        this.manager.setFooterModelInSection(new SectionMoreReviewsFooterCellModel(otherCount), forSectionIndex, IEAMoreReviewsFooterCell.getType());
    }

    protected int getEmptyHeaderViewHeight() {
        return DimenUtil.getDisplayWidthPx();
    }

    public void setupWebView(ObservableWebView webView) {
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
            PageTitle imageTitle = new PageTitle(imageUUID, null, null);
            GalleryActivity.showGallery(activity, model.getTitle(), imageTitle,
                    GalleryFunnel.SOURCE_LINK_PREVIEW);
        }

        @Override
        public void onGalleryAddPhotoItemClicked() {

        }
    };

    public boolean isMainPage() {
        return false;
    }

    public boolean haveLeadImage() {
        return true;
    }

    protected MenuBarEventHandler getMenuBarEventHandler() {
        PageActivity activity = (PageActivity) this.activity;
        PageFragment pageFragment = activity.getCurPageFragment();

        return pageFragment.getMenuBarEventHandler();
    }
}
