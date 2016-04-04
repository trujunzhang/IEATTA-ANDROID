package org.ieatta.tasks;

import android.app.Activity;
import android.support.annotation.VisibleForTesting;
import android.view.View;

import com.tableview.adapter.NSIndexPath;

import org.ieatta.activity.LeadImageCollection;
import org.ieatta.activity.Page;
import org.ieatta.activity.PageProperties;
import org.ieatta.activity.PageTitle;
import org.ieatta.activity.PageViewModel;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.activity.update.UpdateEntry;
import org.ieatta.cells.IEAGalleryThumbnailCell;
import org.ieatta.cells.IEAReviewsCell;
import org.ieatta.cells.headerfooterview.IEAFooterView;
import org.ieatta.cells.headerfooterview.IEAHeaderView;
import org.ieatta.cells.model.IEAFooterViewModel;
import org.ieatta.cells.model.IEAHeaderViewModel;
import org.ieatta.cells.model.IEAReviewsCellModel;
import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRecipe;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.models.DBTeam;
import org.ieatta.database.provide.PhotoUsedType;
import org.ieatta.database.provide.ReviewType;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.database.realm.RealmModelReader;
import org.ieatta.parse.AppConstant;
import org.ieatta.server.cache.ThumbnailImageUtil;

import java.io.File;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

public class RecipeDetailTask extends FragmentTask {
    public DBRestaurant restaurant;
    public DBEvent event;
    public DBTeam team;
    public DBRecipe recipe;

    private LeadImageCollection leadImageCollection; // for recipes

    @VisibleForTesting
    public RecipeDetailTask(HistoryEntry entry) {
        super(entry);
    }

    public RecipeDetailTask(HistoryEntry entry, Activity activity, PageViewModel model) {
        super(entry, activity, model);
    }

    @Override
    public void onItemClick(View view, NSIndexPath indexPath, Object model, int position, boolean isLongClick) {

    }

    enum RecipeDetailSection {
        section_gallery_thumbnail,//= 0
        section_reviews,//= 1
    }

    /**
     * Execute Task for Recipe detail.
     *
     * @return
     */
    public Task<Void> executeTask() {
        final String recipeUUID = this.entry.getHPara();

        return new RealmModelReader<DBRecipe>(DBRecipe.class).getFirstObject(LocalDatabaseQuery.get(recipeUUID), false, realmList).onSuccessTask(new Continuation<DBRecipe, Task<DBTeam>>() {
            @Override
            public Task<DBTeam> then(Task<DBRecipe> task) throws Exception {
                DBRecipe recipe = task.getResult();
                RecipeDetailTask.this.recipeUUID = recipe.getUUID();
                RecipeDetailTask.this.teamUUID = recipe.getOrderedPeopleRef();
                RecipeDetailTask.this.eventUUID = recipe.getEventRef();
                RecipeDetailTask.this.recipe = recipe;
                return new RealmModelReader<DBTeam>(DBTeam.class).getFirstObject(LocalDatabaseQuery.get(RecipeDetailTask.this.teamUUID), false, realmList);
            }
        }).onSuccessTask(new Continuation<DBTeam, Task<DBEvent>>() {
            @Override
            public Task<DBEvent> then(Task<DBTeam> task) throws Exception {
                DBTeam team = task.getResult();
                RecipeDetailTask.this.team = team;
                return new RealmModelReader<DBEvent>(DBEvent.class).getFirstObject(LocalDatabaseQuery.get(RecipeDetailTask.this.eventUUID), false, realmList);
            }
        }).onSuccessTask(new Continuation<DBEvent, Task<DBRestaurant>>() {
            @Override
            public Task<DBRestaurant> then(Task<DBEvent> task) throws Exception {
                DBEvent event = task.getResult();
                RecipeDetailTask.this.event = event;
                RecipeDetailTask.this.restaurantUUID = event.getRestaurantRef();
                return new RealmModelReader<DBRestaurant>(DBRestaurant.class).getFirstObject(LocalDatabaseQuery.get(restaurantUUID), false, realmList);
            }
        }).onSuccessTask(new Continuation<DBRestaurant, Task<RealmResults<DBPhoto>>>() {
            @Override
            public Task<RealmResults<DBPhoto>> then(Task<DBRestaurant> task) throws Exception {
                DBRestaurant restaurant = task.getResult();
                RecipeDetailTask.this.restaurant = restaurant;
                return LocalDatabaseQuery.queryPhotosByModel(recipeUUID, PhotoUsedType.PU_Recipe.getType(), realmList);
            }
        }).onSuccessTask(new Continuation<RealmResults<DBPhoto>, Task<List<File>>>() {
            @Override
            public Task<List<File>> then(Task<RealmResults<DBPhoto>> task) throws Exception {
                RecipeDetailTask.this.leadImageCollection = DBConvert.toLeadImageCollection(task.getResult());
                return ThumbnailImageUtil.sharedInstance.getImagesListTask(recipeUUID);
            }
        }).onSuccessTask(new Continuation<List<File>, Task<List<IEAReviewsCellModel>>>() {
            @Override
            public Task<List<IEAReviewsCellModel>> then(Task<List<File>> task) throws Exception {
                RecipeDetailTask.this.thumbnailGalleryCollection = DBConvert.toGalleryCollection(task.getResult());
                return reviewQuery.queryReview(recipeUUID, ReviewType.Review_Recipe, AppConstant.limit_reviews);
            }
        }).onSuccess(new Continuation<List<IEAReviewsCellModel>, Void>() {
            @Override
            public Void then(Task<List<IEAReviewsCellModel>> task) throws Exception {
                RecipeDetailTask.this.reviewsCellModelList = task.getResult();
                return null;
            }
        });
    }

    @Override
    public Task<Void> executeUpdateTask(UpdateEntry entry) {
        return null;
    }

    @Override
    public void prepareUI() {
        super.prepareUI();

        this.manager.setRegisterCellClass(IEAGalleryThumbnailCell.getType(), RecipeDetailSection.section_gallery_thumbnail.ordinal());
        this.manager.setRegisterCellClass(IEAReviewsCell.getType(), RecipeDetailSection.section_reviews.ordinal());
    }

    @Override
    public void postUI() {
        this.manager.setHeaderItem(new IEAHeaderViewModel(this.getEmptyHeaderViewHeight()), IEAHeaderView.getType());
        this.manager.setFooterItem(new IEAFooterViewModel(), IEAFooterView.getType());

        postPhotosGallery(RecipeDetailSection.section_gallery_thumbnail.ordinal());
        postReviews(RecipeDetailSection.section_reviews.ordinal(),recipeUUID, ReviewType.Review_Recipe, AppConstant.limit_reviews);

        model.setPage(this.getPage());

    }

    public Page getPage() {
        String title = recipe.getDisplayName();
        String description = String.format("$ %s", recipe.getPrice());
        PageTitle pageTitle = new PageTitle(this.recipe.getUUID(),null,description, reviewQuery.ratingReview);
        PageProperties properties = new PageProperties(this.leadImageCollection, title,null);

        return new Page(pageTitle, properties);
    }
}
