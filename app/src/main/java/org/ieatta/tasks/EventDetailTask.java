package org.ieatta.tasks;

import org.ieatta.activity.LeadImageCollection;
import org.ieatta.activity.Page;
import org.ieatta.activity.PageProperties;
import org.ieatta.activity.PageTitle;
import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBPeopleInEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.models.DBReview;
import org.ieatta.database.models.DBTeam;
import org.ieatta.database.provide.ReviewType;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.database.realm.DBBuilder;
import org.ieatta.database.realm.RealmModelReader;
import org.ieatta.parse.DBConstant;

import java.util.LinkedList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

public class EventDetailTask {
    public DBRestaurant restaurant;
    public DBEvent event;
    public RealmResults<DBTeam> teams;
    public RealmResults<DBReview> reviews;
    private LeadImageCollection leadImageCollection;

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
                .onSuccessTask(new Continuation<DBEvent, Task<RealmResults<DBPeopleInEvent>>>() {
                    @Override
                    public Task<RealmResults<DBPeopleInEvent>> then(Task<DBEvent> task) throws Exception {
                        EventDetailTask.this.event = task.getResult();
                        return new RealmModelReader<DBPeopleInEvent>(DBPeopleInEvent.class).fetchResults(
                                LocalDatabaseQuery.getQueryOrderedPeople(eventUUID), false);
                    }
                }).onSuccessTask(new Continuation<RealmResults<DBPeopleInEvent>, Task<RealmResults<DBTeam>>>() {
                    @Override
                    public Task<RealmResults<DBTeam>> then(Task<RealmResults<DBPeopleInEvent>> task) throws Exception {
                        List<String> peoplePoints = EventDetailTask.this.getPeoplePoints(task.getResult());
                        return new RealmModelReader<DBTeam>(DBTeam.class).fetchResults(LocalDatabaseQuery.getObjectsByUUIDs(peoplePoints),false);
                    }
                })
                .onSuccessTask(new Continuation<RealmResults<DBTeam>, Task<RealmResults<DBReview>>>() {
                    @Override
                    public Task<RealmResults<DBReview>> then(Task<RealmResults<DBTeam>> task) throws Exception {
                        EventDetailTask.this.teams = task.getResult();
                        return new RealmModelReader<DBReview>(DBReview.class).fetchResults(
                                new DBBuilder()
                                        .whereEqualTo(DBConstant.kPAPFieldReviewRefKey, restaurantUUID)
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

    private List<String> getPeoplePoints(List<DBPeopleInEvent> peopleInEvent) {
        List<String> peoplePoints = new LinkedList<>();

        for (DBPeopleInEvent model : peopleInEvent) {
            peoplePoints.add(model.getUserRef());
        }

        return peoplePoints;
    }


    public Page getPage() {
        String title = restaurant.getDisplayName();
        PageTitle pageTitle = new PageTitle(this.restaurant.getUUID());
        PageProperties properties = new PageProperties(this.leadImageCollection, title);

        return new Page(pageTitle, properties);
    }
}
