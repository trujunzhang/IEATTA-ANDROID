package org.ieatta.tasks;

import android.content.Context;

import org.ieatta.activity.Page;
import org.ieatta.activity.PageProperties;
import org.ieatta.activity.PageTitle;
import org.ieatta.activity.LeadImageCollection;
import org.ieatta.activity.PageViewModel;
import org.ieatta.activity.gallery.GalleryCollection;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.cells.model.ReviewsCellModel;
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

import java.util.List;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;
import android.os.Bundle;
import android.view.View;

import com.tableview.RecycleViewManager;
import com.tableview.adapter.NSIndexPath;
import com.tableview.adapter.RecyclerOnItemClickListener;
import com.tableview.utils.CollectionUtil;

import org.ieatta.R;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.cells.IEAGalleryThumbnailCell;
import org.ieatta.cells.IEARestaurantEventsCell;
import org.ieatta.cells.IEAReviewsCell;
import org.ieatta.cells.headerfooterview.IEAFooterView;
import org.ieatta.cells.headerfooterview.IEAHeaderView;
import org.ieatta.cells.model.IEAFooterViewModel;
import org.ieatta.cells.model.IEAGalleryThumbnail;
import org.ieatta.cells.model.IEAHeaderViewModel;
import org.ieatta.cells.model.SectionTitleCellModel;
import org.ieatta.provide.IEAEditKey;
import org.ieatta.tasks.RestaurantDetailTask;

import bolts.Continuation;
import bolts.Task;

import static butterknife.ButterKnife.findById;

public class RestaurantDetailTask extends FragmentTask {

    public DBRestaurant restaurant;
    public RealmResults<DBEvent> events;
    public List<ReviewsCellModel> reviewsCellModelList;
    public GalleryCollection thumbnailGalleryCollection;
    private LeadImageCollection leadImageCollection; // for restaurants

    public RestaurantDetailTask(HistoryEntry entry, Context context, PageViewModel model) {
        super(entry, context, model);
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

        return new RealmModelReader<DBRestaurant>(DBRestaurant.class).getFirstObject(LocalDatabaseQuery.get(restaurantUUID), false).onSuccessTask(new Continuation<DBRestaurant, Task<RealmResults<DBPhoto>>>() {
            @Override
            public Task<RealmResults<DBPhoto>> then(Task<DBRestaurant> task) throws Exception {
                RestaurantDetailTask.this.restaurant = task.getResult();
                return LocalDatabaseQuery.queryPhotosByModel(restaurantUUID, PhotoUsedType.PU_Restaurant.getType());
            }
        }).onSuccessTask(new Continuation<RealmResults<DBPhoto>, Task<RealmResults<DBPhoto>>>() {
            @Override
            public Task<RealmResults<DBPhoto>> then(Task<RealmResults<DBPhoto>> task) throws Exception {
                RestaurantDetailTask.this.leadImageCollection = DBConvert.toLeadImageCollection(task.getResult());
                return LocalDatabaseQuery.queryPhotosForRestaurant(restaurantUUID);
            }
        }).onSuccessTask(new Continuation<RealmResults<DBPhoto>, Task<RealmResults<DBEvent>>>() {
            @Override
            public Task<RealmResults<DBEvent>> then(Task<RealmResults<DBPhoto>> task) throws Exception {
                RestaurantDetailTask.this.thumbnailGalleryCollection = new GalleryCollection(DBConvert.toGalleryItem(task.getResult()));
                return new RealmModelReader<DBEvent>(DBEvent.class).fetchResults(
                        new DBBuilder().whereEqualTo(AppConstant.kPAPFieldLocalRestaurantKey, restaurantUUID), false);
            }
        }).onSuccessTask(new Continuation<RealmResults<DBEvent>, Task<List<ReviewsCellModel>>>() {
            @Override
            public Task<List<ReviewsCellModel>> then(Task<RealmResults<DBEvent>> task) throws Exception {
                RestaurantDetailTask.this.events = task.getResult();
                return new ReviewQuery().queryReview(restaurantUUID,ReviewType.Review_Restaurant);
            }
        }).onSuccess(new Continuation<List<ReviewsCellModel>, Void>() {
            @Override
            public Void then(Task<List<ReviewsCellModel>> task) throws Exception {
                RestaurantDetailTask.this.reviewsCellModelList = task.getResult();
                return null;
            }
        });
    }

    @Override
    public void prepareUI() {
        this.manager.setRegisterHeaderView(IEAHeaderView.getType());
        this.manager.setRegisterFooterView(IEAFooterView.getType());

        this.manager.setRegisterCellClass(IEARestaurantEventsCell.getType(), RestaurantDetailSection.section_events.ordinal());
        this.manager.setRegisterCellClass(IEAGalleryThumbnailCell.getType(), RestaurantDetailSection.section_gallery_thumbnail.ordinal());
        this.manager.setRegisterCellClass(IEAReviewsCell.getType(), RestaurantDetailSection.section_reviews.ordinal());

        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Events_Recorded), RestaurantDetailSection.section_events.ordinal());
        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Reviews), RestaurantDetailSection.section_reviews.ordinal());
    }

    @Override
    public void postUI() {
        this.manager.setHeaderItem(new IEAHeaderViewModel(this.getEmptyHeaderViewHeight()), IEAHeaderView.getType());
        this.manager.setFooterItem(new IEAFooterViewModel(), IEAFooterView.getType());

        this.manager.setSectionItems(this.events, RestaurantDetailSection.section_events.ordinal());

//        this.manager.setSectionItems(CollectionUtil.createList(new IEAGalleryThumbnail(this.this.thumbnailGalleryCollection, this.galleryViewListener)), RestaurantDetailSection.section_gallery_thumbnail.ordinal());
        this.manager.setSectionItems(this.reviewsCellModelList, RestaurantDetailSection.section_reviews.ordinal());

        model.setPage(this.getPage());
    }

    public Page getPage() {
        String title = restaurant.getDisplayName();
        PageTitle pageTitle = new PageTitle(this.restaurant.getUUID());
        PageProperties properties = new PageProperties(this.leadImageCollection, title);

        return new Page(pageTitle, properties);
    }
}
