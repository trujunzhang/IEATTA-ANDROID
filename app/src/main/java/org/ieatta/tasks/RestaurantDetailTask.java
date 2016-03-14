package org.ieatta.tasks;


import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.models.DBReview;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.database.realm.RealmModelReader;

import java.util.List;

import bolts.Continuation;
import bolts.Task;

public class RestaurantDetailTask {
    private DBRestaurant restaurant;
    private List<DBEvent> events;
    private List<DBReview> reviews;
    private List<DBPhoto> galleryCollection;

    /**
     * Execute Task for Restaurant detail.
     *
     * @param UUID restaurant's UUID
     * @return
     */
    public Task<Void> executeTask(final String UUID) {
        return new RealmModelReader<DBRestaurant>(DBRestaurant.class).getFirstObject(LocalDatabaseQuery.get(UUID), false).onSuccess(new Continuation<DBRestaurant, Void>() {
            @Override
            public Void then(Task<DBRestaurant> task) throws Exception {
                DBRestaurant result = task.getResult();
                String displayName = result.getDisplayName();
                return null;
            }
        });

//        return new RealmModelReader(DBRestaurant.class).fetchResults(LocalDatabaseQuery.get(UUID)).onSuccessTask(new Continuation<DBRestaurant, Task<List<DBPhoto>>>() {
//            @Override
//            public Task<List<DBPhoto>> then(Task<DBRestaurant> task) throws Exception {
//                RestaurantDetailTask.this.restaurant = task.getResult();
//                return LocalDatabaseQuery.queryPhotosForRestaurant(UUID);
//            }
//        }).onSuccessTask(new Continuation<List<DBPhoto>, Task<List<DBEvent>>>() {
//            @Override
//            public Task<List<DBEvent>> then(Task<List<DBPhoto>> task) throws Exception {
//                RestaurantDetailTask.this.galleryCollection = task.getResult();
//                return new RealmModelReader(DBRestaurant.class).fetchResults(
//                        new DBBuilder().whereEqualTo(ParseObjectConstant.kPAPFieldLocalRestaurantKey, UUID));
//            }
//        }).onSuccessTask(new Continuation<List<DBEvent>, Task<List<DBReview>>>() {
//            @Override
//            public Task<List<DBReview>> then(Task<List<DBEvent>> task) throws Exception {
//                return new RealmModelReader(DBReview.class).fetchResults(
//                        new DBBuilder().whereEqualTo(ParseObjectConstant.kPAPFieldReviewRefKey, UUID)
//                                .whereEqualTo(ParseObjectConstant.kPAPFieldReviewTypeKey, ReviewType.Review_Restaurant.getType()));
//            }
//        }).onSuccess(new Continuation<List<DBReview>,Void>() {
//            @Override
//            public Void then(Task<List<DBReview>> task) throws Exception {
//                RestaurantDetailTask.this.reviews = task.getResult();
//                return null;
//            }
//        });
    }

}
