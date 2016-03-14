package org.ieatta.tasks;

import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRecipe;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.models.DBReview;
import org.ieatta.database.models.DBTeam;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.database.realm.RealmModelReader;

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
                }).onSuccess(new Continuation<DBRecipe, Void>() {
                    @Override
                    public Void then(Task<DBRecipe> task) throws Exception {
                        RecipeDetailTask.this.recipe = task.getResult();
                        return null;
                    }
                });
    }
}
