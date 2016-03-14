package org.ieatta.tasks;

import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.models.DBReview;
import org.ieatta.database.models.DBTeam;
import org.ieatta.database.provide.ReviewType;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.database.realm.DBBuilder;
import org.ieatta.database.realm.RealmModelReader;
import org.ieatta.parse.DBConstant;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

public class EventDetailTask {
    public DBRestaurant restaurant;
    public DBEvent event;
    public RealmResults<DBTeam> teams;
    public RealmResults<DBReview> reviews;

    /**
     * Execute Task for Restaurant detail.
     *
     * @param restaurantUUID restaurant's UUID
     * @param eventUUID      event's UUID
     * @return
     */
    public Task<Void> executeTask(final String restaurantUUID, final String eventUUID) {
        return new RealmModelReader<DBRestaurant>(DBRestaurant.class).getFirstObject(LocalDatabaseQuery.get(restaurantUUID), false)
                .onSuccessTask(new Continuation<DBRestaurant, Task<DBEvent>>() {
                    @Override
                    public Task<DBEvent> then(Task<DBRestaurant> task) throws Exception {
                        EventDetailTask.this.restaurant = task.getResult();
                        return new RealmModelReader<DBEvent>(DBEvent.class).getFirstObject(LocalDatabaseQuery.get(eventUUID),false);
                    }
                })
                .onSuccessTask(new Continuation<DBEvent, Task<RealmResults<DBEvent>>>() {
                    @Override
                    public Task<RealmResults<DBEvent>> then(Task<DBEvent> task) throws Exception {
                        EventDetailTask.this.event = task.getResult();
                        return new RealmModelReader<DBEvent>(DBEvent.class).fetchResults(
                                new DBBuilder().whereEqualTo(DBConstant.kPAPFieldLocalRestaurantKey, restaurantUUID), false);
                    }
                }).onSuccessTask(new Continuation<RealmResults<DBEvent>, Task<RealmResults<DBReview>>>() {
                    @Override
                    public Task<RealmResults<DBReview>> then(Task<RealmResults<DBEvent>> task) throws Exception {
                        return new RealmModelReader<DBReview>(DBReview.class).fetchResults(
                                new DBBuilder().whereEqualTo(DBConstant.kPAPFieldReviewRefKey, restaurantUUID)
                                        .whereEqualTo(DBConstant.kPAPFieldReviewTypeKey, ReviewType.Review_Restaurant.getType()), false);
                    }
                }).onSuccess(new Continuation<RealmResults<DBReview>, Void>() {
                    @Override
                    public Void then(Task<RealmResults<DBReview>> task) throws Exception {
                        EventDetailTask.this.reviews = task.getResult();
                        return null;
                    }
                });
    }
}
