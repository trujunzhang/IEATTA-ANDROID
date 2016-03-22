package org.ieatta.tasks;

import org.ieatta.activity.LeadImageCollection;
import org.ieatta.activity.Page;
import org.ieatta.activity.PageProperties;
import org.ieatta.activity.PageTitle;
import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBPeopleInEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRecipe;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.models.DBReview;
import org.ieatta.database.models.DBTeam;
import org.ieatta.database.provide.PhotoUsedType;
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

public class OrderedRecipesTask {
    private DBRestaurant restaurant;
    private DBEvent event;
    public DBTeam team;
    public RealmResults<DBRecipe> recipes;
    private LeadImageCollection leadImageCollection;

    /**
     * Execute Task for Restaurant detail.
     *
     * @param restaurantUUID restaurant's UUID
     * @param eventUUID      event's UUID
     * @param teamUUID       team's UUID
     * @return
     */
    public Task<Void> executeTask(final String restaurantUUID, final String eventUUID, final String teamUUID) {
        return new RealmModelReader<DBRestaurant>(DBRestaurant.class).getFirstObject(LocalDatabaseQuery.get(restaurantUUID), false)
                .onSuccessTask(new Continuation<DBRestaurant, Task<RealmResults<DBPhoto>>>() {
                    @Override
                    public Task<RealmResults<DBPhoto>> then(Task<DBRestaurant> task) throws Exception {
                        OrderedRecipesTask.this.restaurant = task.getResult();
                        return LocalDatabaseQuery.queryPhotosByModel(restaurantUUID, PhotoUsedType.PU_Restaurant.getType());
                    }
                }).onSuccessTask(new Continuation<RealmResults<DBPhoto>, Task<DBEvent>>() {
                    @Override
                    public Task<DBEvent> then(Task<RealmResults<DBPhoto>> task) throws Exception {
                        OrderedRecipesTask.this.leadImageCollection = DBConvert.toLeadImageCollection(task.getResult());
                        return new RealmModelReader<DBEvent>(DBEvent.class).getFirstObject(LocalDatabaseQuery.get(eventUUID), false);
                    }
                }).onSuccessTask(new Continuation<DBEvent, Task<DBTeam>>() {
                    @Override
                    public Task<DBTeam> then(Task<DBEvent> task) throws Exception {
                        OrderedRecipesTask.this.event = task.getResult();
                        return new RealmModelReader<DBTeam>(DBTeam.class).getFirstObject(LocalDatabaseQuery.get(teamUUID), false);
                    }
                })
                .onSuccessTask(new Continuation<DBTeam, Task<RealmResults<DBRecipe>>>() {
                    @Override
                    public Task<RealmResults<DBRecipe>> then(Task<DBTeam> task) throws Exception {
                        OrderedRecipesTask.this.team = task.getResult();
                        return new RealmModelReader<DBRecipe>(DBRecipe.class).fetchResults(
                                LocalDatabaseQuery.getForRecipes(teamUUID, eventUUID), false);
                    }
                }).onSuccess(new Continuation<RealmResults<DBRecipe>, Void>() {
                    @Override
                    public Void then(Task<RealmResults<DBRecipe>> task) throws Exception {
                        OrderedRecipesTask.this.recipes = task.getResult();
                        return null;
                    }
                });
    }


    public Page getPage() {
        String title = restaurant.getDisplayName();
        PageTitle pageTitle = new PageTitle(this.restaurant.getUUID());
        PageProperties properties = new PageProperties(this.leadImageCollection, title);

        return new Page(pageTitle, properties);
    }
}
