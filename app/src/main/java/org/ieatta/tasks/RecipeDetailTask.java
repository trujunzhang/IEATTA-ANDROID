package org.ieatta.tasks;

import org.ieatta.database.models.DBEvent;
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

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

public class RecipeDetailTask {
    public DBRestaurant restaurant;
    public DBEvent event;
    public DBTeam team;
    public DBRecipe recipe;
    public RealmResults<DBReview> reviews;
    public RealmResults<DBPhoto> galleryCollection;

    /**
     * Execute Task for Restaurant detail.
     *
     * @param restaurantUUID restaurant's UUID
     * @param eventUUID      event's UUID
     * @param teamUUID       team's UUID
     * @param recipeUUID     recipe's UUID
     * @return
     */
    public Task<Void> executeTask(final String restaurantUUID, final String eventUUID, final String teamUUID, final String recipeUUID) {
        return new RealmModelReader<DBRestaurant>(DBRestaurant.class).getFirstObject(LocalDatabaseQuery.get(restaurantUUID), false)
                .onSuccessTask(new Continuation<DBRestaurant, Task<DBEvent>>() {
                    @Override
                    public Task<DBEvent> then(Task<DBRestaurant> task) throws Exception {
                        RecipeDetailTask.this.restaurant = task.getResult();
                        return new RealmModelReader<DBEvent>(DBEvent.class).getFirstObject(LocalDatabaseQuery.get(eventUUID), false);
                    }
                })
                .onSuccessTask(new Continuation<DBEvent, Task<DBTeam>>() {
                    @Override
                    public Task<DBTeam> then(Task<DBEvent> task) throws Exception {
                        RecipeDetailTask.this.event = task.getResult();
                        return new RealmModelReader<DBTeam>(DBTeam.class).getFirstObject(LocalDatabaseQuery.get(teamUUID), false);
                    }
                })
                .onSuccessTask(new Continuation<DBTeam, Task<DBRecipe>>() {
                    @Override
                    public Task<DBRecipe> then(Task<DBTeam> task) throws Exception {
                        RecipeDetailTask.this.team = task.getResult();
                        return new RealmModelReader<DBRecipe>(DBRecipe.class).getFirstObject(LocalDatabaseQuery.get(recipeUUID), false);
                    }
                }).onSuccessTask(new Continuation<DBRecipe, Task<RealmResults<DBPhoto>>>() {
                    @Override
                    public Task<RealmResults<DBPhoto>> then(Task<DBRecipe> task) throws Exception {
                        RecipeDetailTask.this.recipe = task.getResult();
                        return LocalDatabaseQuery.queryPhotos(recipeUUID, PhotoUsedType.PU_Recipe.getType());
                    }
                }).onSuccessTask(new Continuation<RealmResults<DBPhoto>, Task<RealmResults<DBReview>>>() {
                    @Override
                    public Task<RealmResults<DBReview>> then(Task<RealmResults<DBPhoto>> task) throws Exception {
                        RecipeDetailTask.this.galleryCollection = task.getResult();
                        return new RealmModelReader<DBReview>(DBReview.class).fetchResults(
                                new DBBuilder()
                                        .whereEqualTo(DBConstant.kPAPFieldReviewRefKey, restaurantUUID)
                                        .whereEqualTo(DBConstant.kPAPFieldReviewTypeKey, ReviewType.Review_Recipe.getType()), false);
                    }
                }).onSuccess(new Continuation<RealmResults<DBReview>, Void>() {
                    @Override
                    public Void then(Task<RealmResults<DBReview>> task) throws Exception {
                        RecipeDetailTask.this.reviews = task.getResult();
                        return null;
                    }
                });
    }
}
