package org.ieatta.tasks;

import org.ieatta.activity.LeadImageCollection;
import org.ieatta.activity.Page;
import org.ieatta.activity.PageProperties;
import org.ieatta.activity.PageTitle;
import org.ieatta.activity.gallery.GalleryCollection;
import org.ieatta.cells.model.ReviewsCellModel;
import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRecipe;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.models.DBTeam;
import org.ieatta.database.provide.PhotoUsedType;
import org.ieatta.database.provide.ReviewType;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.database.query.ReviewQuery;
import org.ieatta.database.realm.RealmModelReader;
import org.ieatta.server.cache.ThumbnailImageUtil;

import java.io.File;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

public class RecipeDetailTask {
    public DBRestaurant restaurant;
    public DBEvent event;
    public DBTeam team;
    public DBRecipe recipe;

    private LeadImageCollection leadImageCollection;
    public List<ReviewsCellModel> reviewsCellModelList;
    public GalleryCollection thumbnailGalleryCollection;

    /**
     * Execute Task for Recipe detail.
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
                        return LocalDatabaseQuery.queryPhotosByModel(recipeUUID, PhotoUsedType.PU_Recipe.getType());
                    }
                }).onSuccessTask(new Continuation<RealmResults<DBPhoto>, Task<List<File>>>() {
                    @Override
                    public Task<List<File>> then(Task<RealmResults<DBPhoto>> task) throws Exception {
                        RecipeDetailTask.this.leadImageCollection = DBConvert.toLeadImageCollection(task.getResult());
                        return ThumbnailImageUtil.sharedInstance.getImagesListTask(recipeUUID);
                    }
                }).onSuccessTask(new Continuation<List<File>, Task<List<ReviewsCellModel>>>() {
                    @Override
                    public Task<List<ReviewsCellModel>> then(Task<List<File>> task) throws Exception {
                        RecipeDetailTask.this.thumbnailGalleryCollection = DBConvert.toGalleryCollection(task.getResult());
                        return new ReviewQuery().queryReview(recipeUUID, ReviewType.Review_Recipe);
                    }
                }).onSuccess(new Continuation<List<ReviewsCellModel>, Void>() {
                    @Override
                    public Void then(Task<List<ReviewsCellModel>> task) throws Exception {
                        RecipeDetailTask.this.reviewsCellModelList = task.getResult();
                        return null;
                    }
                });
    }

    public Page getPage() {
        String title = recipe.getDisplayName();
        PageTitle pageTitle = new PageTitle(this.recipe.getUUID());
        PageProperties properties = new PageProperties(this.leadImageCollection, title);

        return new Page(pageTitle, properties);
    }
}
