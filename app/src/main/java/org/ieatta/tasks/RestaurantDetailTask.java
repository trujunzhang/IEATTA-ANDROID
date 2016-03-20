package org.ieatta.tasks;

import org.ieatta.activity.Page;
import org.ieatta.activity.PageProperties;
import org.ieatta.activity.PageTitle;
import org.ieatta.activity.PhotoGalleryModel;
import org.ieatta.activity.gallery.GalleryCollection;
import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.models.DBReview;
import org.ieatta.database.provide.ReviewType;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.database.realm.DBBuilder;
import org.ieatta.database.realm.RealmModelReader;
import org.ieatta.parse.DBConstant;
import org.ieatta.server.cache.ThumbnailImageUtil;

import java.io.File;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

public class RestaurantDetailTask {
    public DBRestaurant restaurant;
    public RealmResults<DBEvent> events;
    public RealmResults<DBReview> reviews;
    public GalleryCollection galleryCollection;
    private PhotoGalleryModel photoGalleryModel;

    /**
     * Execute Task for Restaurant detail.
     *
     * @param restaurantUUID restaurant's UUID
     * @return
     */
    public Task<Void> executeTask(final String restaurantUUID) {
        return new RealmModelReader<DBRestaurant>(DBRestaurant.class).getFirstObject(LocalDatabaseQuery.get(restaurantUUID), false).onSuccessTask(new Continuation<DBRestaurant, Task<List<File>>>() {
            @Override
            public Task<List<File>> then(Task<DBRestaurant> task) throws Exception {
                RestaurantDetailTask.this.restaurant = task.getResult();
                return ThumbnailImageUtil.sharedInstance.getImagesListTask(restaurantUUID);
            }
        }).onSuccessTask(new Continuation<List<File>, Task<RealmResults<DBPhoto>>>() {
            @Override
            public Task<RealmResults<DBPhoto>> then(Task<List<File>> task) throws Exception {
                RestaurantDetailTask.this.photoGalleryModel = new PhotoGalleryModel(task.getResult(),restaurantUUID);
                return LocalDatabaseQuery.queryPhotosForRestaurant(restaurantUUID);
            }
        }).onSuccessTask(new Continuation<RealmResults<DBPhoto>, Task<RealmResults<DBEvent>>>() {
            @Override
            public Task<RealmResults<DBEvent>> then(Task<RealmResults<DBPhoto>> task) throws Exception {
                RestaurantDetailTask.this.galleryCollection = new GalleryCollection(DBConvert.toGalleryItem(task.getResult()));
                return new RealmModelReader<DBEvent>(DBEvent.class).fetchResults(
                        new DBBuilder().whereEqualTo(DBConstant.kPAPFieldLocalRestaurantKey, restaurantUUID), false);
//                return new RealmModelReader<DBEvent>(DBEvent.class).fetchResults(new DBBuilder(), false);
            }
        }).onSuccessTask(new Continuation<RealmResults<DBEvent>, Task<RealmResults<DBReview>>>() {
            @Override
            public Task<RealmResults<DBReview>> then(Task<RealmResults<DBEvent>> task) throws Exception {
                RestaurantDetailTask.this.events = task.getResult();
                return new RealmModelReader<DBReview>(DBReview.class).fetchResults(
                        new DBBuilder()
                                .whereEqualTo(DBConstant.kPAPFieldReviewRefKey, restaurantUUID)
                                .whereEqualTo(DBConstant.kPAPFieldReviewTypeKey, ReviewType.Review_Restaurant.getType()), false);
            }
        }).onSuccess(new Continuation<RealmResults<DBReview>, Void>() {
            @Override
            public Void then(Task<RealmResults<DBReview>> task) throws Exception {
                RestaurantDetailTask.this.reviews = task.getResult();
                return null;
            }
        });
    }

    public Page getPage() {
        String title = restaurant.getDisplayName();
        String description = restaurant.getGoogleMapAddress();

        PageTitle pageTitle = new PageTitle(description);
        PageProperties properties = new PageProperties(this.photoGalleryModel,title);

        return new Page(pageTitle, properties);
    }
}
