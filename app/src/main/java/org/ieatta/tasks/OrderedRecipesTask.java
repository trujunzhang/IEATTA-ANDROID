package org.ieatta.tasks;

import android.app.Activity;
import android.support.annotation.VisibleForTesting;
import android.view.View;

import com.tableview.adapter.NSIndexPath;

import org.ieatta.R;
import org.ieatta.activity.LeadImageCollection;
import org.ieatta.activity.Page;
import org.ieatta.activity.PageActivity;
import org.ieatta.activity.PageProperties;
import org.ieatta.activity.PageTitle;
import org.ieatta.activity.PageViewModel;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.activity.update.UpdateEntry;
import org.ieatta.cells.IEAOrderedRecipeCardCell;
import org.ieatta.cells.headerfooterview.IEAFooterView;
import org.ieatta.cells.headerfooterview.IEAHeaderView;
import org.ieatta.cells.model.IEAFooterViewModel;
import org.ieatta.cells.model.IEAHeaderViewModel;
import org.ieatta.cells.model.SectionTitleCellModel;
import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRecipe;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.models.DBTeam;
import org.ieatta.database.provide.PhotoUsedType;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.database.realm.RealmModelReader;
import org.ieatta.provide.IEAEditKey;
import org.ieatta.provide.MainSegueIdentifier;
import org.wikipedia.util.DimenUtil;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

public class OrderedRecipesTask extends FragmentTask {
    private DBRestaurant restaurant;
    private DBEvent event;
    public DBTeam team;
    public RealmResults<DBRecipe> recipes;
    private LeadImageCollection leadImageCollection; // for restaurants

    @Override
    public boolean haveLeadImage() {
        return false;
    }


    @Override
    public void onItemClick(View view, NSIndexPath indexPath, Object model, int position, boolean isLongClick) {
        if (model instanceof DBRecipe) {
            DBRecipe item = (DBRecipe) model;

            ((PageActivity) OrderedRecipesTask.this.activity).loadPage(
                    new HistoryEntry(MainSegueIdentifier.detailRecipeSegueIdentifier, item.getUUID()));
        }
    }

    enum OrderedRecipesSection {
        section_recipes,       //= 0
    }

    @VisibleForTesting
    public OrderedRecipesTask(HistoryEntry entry) {
        super(entry);
    }

    public OrderedRecipesTask(HistoryEntry entry, Activity activity, PageViewModel model) {
        super(entry, activity, model);
    }

    /**
     * Execute Task for ordered recipes.
     *
     * @return
     */
    public Task<Void> executeTask() {
        final String _eventUUID = this.entry.getHPara();
        final String _teamUUID = this.entry.getVPara();

        return new RealmModelReader<DBEvent>(DBEvent.class).getFirstObject(LocalDatabaseQuery.get(_eventUUID), false, realmList).onSuccessTask(new Continuation<DBEvent, Task<DBRestaurant>>() {
            @Override
            public Task<DBRestaurant> then(Task<DBEvent> task) throws Exception {
                DBEvent event = task.getResult();
                OrderedRecipesTask.this.event = event;
                OrderedRecipesTask.this.mRestaurantUUID = event.getRestaurantRef();
                return new RealmModelReader<DBRestaurant>(DBRestaurant.class).getFirstObject(LocalDatabaseQuery.get(OrderedRecipesTask.this.mRestaurantUUID), false, realmList);
            }
        }).onSuccessTask(new Continuation<DBRestaurant, Task<RealmResults<DBPhoto>>>() {
            @Override
            public Task<RealmResults<DBPhoto>> then(Task<DBRestaurant> task) throws Exception {
                OrderedRecipesTask.this.restaurant = task.getResult();
                return LocalDatabaseQuery.queryPhotosByModel(OrderedRecipesTask.this.mRestaurantUUID, PhotoUsedType.PU_Restaurant.getType(), realmList);
            }
        }).onSuccessTask(new Continuation<RealmResults<DBPhoto>, Task<RealmResults<DBRecipe>>>() {
            @Override
            public Task<RealmResults<DBRecipe>> then(Task<RealmResults<DBPhoto>> task) throws Exception {
                OrderedRecipesTask.this.leadImageCollection = DBConvert.toLeadImageCollection(task.getResult());
                return new RealmModelReader<DBRecipe>(DBRecipe.class).fetchResults(LocalDatabaseQuery.getForRecipes(_teamUUID, _eventUUID), false, realmList);
            }
        }).onSuccess(new Continuation<RealmResults<DBRecipe>, Void>() {
            @Override
            public Void then(Task<RealmResults<DBRecipe>> task) throws Exception {
                OrderedRecipesTask.this.recipes = task.getResult();
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

        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Ordered_Recipes), OrderedRecipesSection.section_recipes.ordinal());
    }

    @Override
    public void postUI() {
        this.manager.setHeaderItem(new IEAHeaderViewModel(this.getStatusBarHeight()), IEAHeaderView.getType());
        this.manager.setFooterItem(new IEAFooterViewModel(), IEAFooterView.getType());

        this.manager.setAndRegisterSectionItems(IEAOrderedRecipeCardCell.getType(), this.recipes, OrderedRecipesSection.section_recipes.ordinal());
    }
}
