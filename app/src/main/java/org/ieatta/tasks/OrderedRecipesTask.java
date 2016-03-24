package org.ieatta.tasks;

import org.ieatta.activity.LeadImageCollection;
import org.ieatta.activity.Page;
import org.ieatta.activity.PageProperties;
import org.ieatta.activity.PageTitle;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.database.models.DBEvent;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRecipe;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.models.DBTeam;
import org.ieatta.database.provide.PhotoUsedType;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.database.realm.RealmModelReader;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

public class OrderedRecipesTask extends FragmentTask{
    private DBRestaurant restaurant;
    private DBEvent event;
    public DBTeam team;
    public RealmResults<DBRecipe> recipes;
    private LeadImageCollection leadImageCollection; // for restaurants

    private String restaurantUUID;
    private String eventUUID;
    private String teamUUID;
    private String recipeUUID;

    public OrderedRecipesTask(HistoryEntry entry) {
        super(entry);
    }

    /**
     * Execute Task for ordered recipes.
     *
     * @return
     */
    public Task<Void> executeTask() {
        final String eventUUID = this.entry.getHPara();
        final String teamUUID = this.entry.getVPara();

        return new RealmModelReader<DBEvent>(DBEvent.class).getFirstObject(LocalDatabaseQuery.get(eventUUID), false).onSuccessTask(new Continuation<DBEvent, Task<DBRestaurant>>() {
            @Override
            public Task<DBRestaurant> then(Task<DBEvent> task) throws Exception {
                DBEvent event = task.getResult();
                OrderedRecipesTask.this.event = event;
                OrderedRecipesTask.this.restaurantUUID = event.getRestaurantRef();
                return new RealmModelReader<DBRestaurant>(DBRestaurant.class).getFirstObject(LocalDatabaseQuery.get(restaurantUUID), false);
            }
        }).onSuccessTask(new Continuation<DBRestaurant, Task<RealmResults<DBPhoto>>>() {
            @Override
            public Task<RealmResults<DBPhoto>> then(Task<DBRestaurant> task) throws Exception {
                DBRestaurant restaurant = task.getResult();
                OrderedRecipesTask.this.restaurant = restaurant;
                return LocalDatabaseQuery.queryPhotosByModel(restaurantUUID, PhotoUsedType.PU_Restaurant.getType());
            }
        }).onSuccessTask(new Continuation<RealmResults<DBPhoto>, Task<RealmResults<DBRecipe>>>() {
            @Override
            public Task<RealmResults<DBRecipe>> then(Task<RealmResults<DBPhoto>> task) throws Exception {
                OrderedRecipesTask.this.leadImageCollection = DBConvert.toLeadImageCollection(task.getResult());
                return new RealmModelReader<DBRecipe>(DBRecipe.class).fetchResults(LocalDatabaseQuery.getForRecipes(teamUUID, eventUUID), false);
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
