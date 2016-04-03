package org.ieatta.tasks;

import android.app.Activity;
import android.support.annotation.VisibleForTesting;
import android.view.View;

import com.tableview.adapter.NSIndexPath;

import org.ieatta.R;
import org.ieatta.activity.LeadImageCollection;
import org.ieatta.activity.LeadMapView;
import org.ieatta.activity.Page;
import org.ieatta.activity.PageActivity;
import org.ieatta.activity.PageFragment;
import org.ieatta.activity.PageProperties;
import org.ieatta.activity.PageTitle;
import org.ieatta.activity.PageViewModel;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.activity.leadimages.MenuBarEventHandler;
import org.ieatta.cells.IEAGalleryThumbnailCell;
import org.ieatta.cells.IEARestaurantEventsCell;
import org.ieatta.cells.IEAReviewsCell;
import org.ieatta.cells.headerfooterview.IEAFooterView;
import org.ieatta.cells.headerfooterview.IEAHeaderView;
import org.ieatta.cells.model.IEAFooterViewModel;
import org.ieatta.cells.model.IEAHeaderViewModel;
import org.ieatta.cells.model.IEAReviewsCellModel;
import org.ieatta.cells.model.SectionTitleCellModel;
import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.provide.PhotoUsedType;
import org.ieatta.database.provide.ReviewType;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.database.query.ReviewQuery;
import org.ieatta.database.realm.DBBuilder;
import org.ieatta.database.realm.RealmModelReader;
import org.ieatta.parse.AppConstant;
import org.ieatta.provide.IEAEditKey;
import org.ieatta.provide.MainSegueIdentifier;
import org.ieatta.server.cache.ThumbnailImageUtil;

import java.io.File;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

public class RestaurantDetailTask extends FragmentTask {

    public DBRestaurant restaurant;
    public RealmResults<DBEvent> events;
    private LeadImageCollection leadImageCollection; // for restaurants

    @VisibleForTesting
    public RestaurantDetailTask(HistoryEntry entry) {
        super(entry);
    }

    public RestaurantDetailTask(HistoryEntry entry, Activity activity, PageViewModel model) {
        super(entry, activity, model);
    }

    @Override
    public void onItemClick(View view, NSIndexPath indexPath, Object model, int position, boolean isLongClick) {
        if (model instanceof DBEvent) {
            DBEvent item = (DBEvent) model;

            ((PageActivity) RestaurantDetailTask.this.activity).loadPage(
                    new HistoryEntry(MainSegueIdentifier.detailEventSegueIdentifier, item.getUUID()));
        }
    }

    enum RestaurantDetailSection {
        section_events,//= 0
        section_gallery_thumbnail,//= 1
        section_reviews,//= 2
    }

    /**
     * Execute Task for Restaurant detail.
     *
     * @return
     */
    public Task<Void> executeTask() {
        final String restaurantUUID = this.entry.getHPara();

        return new RealmModelReader<DBRestaurant>(DBRestaurant.class).getFirstObject(LocalDatabaseQuery.get(restaurantUUID), false, realmList).onSuccessTask(new Continuation<DBRestaurant, Task<RealmResults<DBPhoto>>>() {
            @Override
            public Task<RealmResults<DBPhoto>> then(Task<DBRestaurant> task) throws Exception {
                RestaurantDetailTask.this.restaurant = task.getResult();
                return LocalDatabaseQuery.queryPhotosByModel(restaurantUUID, PhotoUsedType.PU_Restaurant.getType(), realmList);
            }
        }).onSuccessTask(new Continuation<RealmResults<DBPhoto>, Task<List<File>>>() {
            @Override
            public Task<List<File>> then(Task<RealmResults<DBPhoto>> task) throws Exception {
                RestaurantDetailTask.this.leadImageCollection = DBConvert.toLeadImageCollection(task.getResult());
                return ThumbnailImageUtil.sharedInstance.getImagesListTask(restaurantUUID);
            }
        }).onSuccessTask(new Continuation<List<File>, Task<RealmResults<DBEvent>>>() {
            @Override
            public Task<RealmResults<DBEvent>> then(Task<List<File>> task) throws Exception {
                RestaurantDetailTask.this.thumbnailGalleryCollection = DBConvert.toGalleryCollection(task.getResult());
                return new RealmModelReader<DBEvent>(DBEvent.class).fetchResults(
                        new DBBuilder().whereEqualTo(AppConstant.kPAPFieldLocalRestaurantKey, restaurantUUID), false, realmList);
            }
        }).onSuccessTask(new Continuation<RealmResults<DBEvent>, Task<List<IEAReviewsCellModel>>>() {
            @Override
            public Task<List<IEAReviewsCellModel>> then(Task<RealmResults<DBEvent>> task) throws Exception {
                RestaurantDetailTask.this.events = task.getResult();
                return reviewQuery.queryReview(restaurantUUID, ReviewType.Review_Restaurant, AppConstant.limit_reviews);
            }
        }).onSuccess(new Continuation<List<IEAReviewsCellModel>, Void>() {
            @Override
            public Void then(Task<List<IEAReviewsCellModel>> task) throws Exception {
                RestaurantDetailTask.this.reviewsCellModelList = task.getResult();
                return null;
            }
        });
    }

    @Override
    public Task<Void> executePhotosGalleryTask() {
        return Task.forResult(null);
    }

    @Override
    public Task<Void> executeReviewsTask() {
        return Task.forResult(null);
    }

    @Override
    public void prepareUI() {
        super.prepareUI();
        this.manager.setRegisterCellClass(IEARestaurantEventsCell.getType(), RestaurantDetailSection.section_events.ordinal());
        this.manager.setRegisterCellClass(IEAReviewsCell.getType(), RestaurantDetailSection.section_reviews.ordinal());

        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Events_Recorded), RestaurantDetailSection.section_events.ordinal());
    }

    @Override
    public void postUI() {
        this.manager.setHeaderItem(new IEAHeaderViewModel(this.getEmptyHeaderViewHeight()), IEAHeaderView.getType());
        this.manager.setFooterItem(new IEAFooterViewModel(), IEAFooterView.getType());

        this.manager.setSectionItems(this.events, RestaurantDetailSection.section_events.ordinal());

        postPhotosGallery(RestaurantDetailSection.section_gallery_thumbnail.ordinal());
        postReviews(RestaurantDetailSection.section_reviews.ordinal(), restaurantUUID, ReviewType.Review_Restaurant, AppConstant.limit_reviews);

        model.setPage(this.getPage());
    }

    public Page getPage() {
        String title = restaurant.getDisplayName();
        String description = restaurant.getGoogleMapAddress();
        PageTitle pageTitle = new PageTitle(this.restaurant.getUUID(), null, description, reviewQuery.ratingReview);
        LeadMapView leadMapView = new LeadMapView(restaurant.getLatitude(),restaurant.getLongitude(),title,description);
        PageProperties properties = new PageProperties(this.leadImageCollection, title, leadMapView);

        return new Page(pageTitle, properties);
    }

    @Override
    public void onNavigateClick() {
        MenuBarEventHandler menuBarEventHandler = getMenuBarEventHandler();
        menuBarEventHandler.toggleMapView();
    }


}
