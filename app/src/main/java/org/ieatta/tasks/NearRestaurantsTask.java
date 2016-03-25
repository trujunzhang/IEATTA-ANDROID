package org.ieatta.tasks;


import android.app.Activity;
import android.location.Location;
import android.support.annotation.VisibleForTesting;
import android.view.View;

import com.tableview.adapter.NSIndexPath;

import org.ieatta.R;
import org.ieatta.activity.PageActivity;
import org.ieatta.activity.PageViewModel;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.cells.IEANearRestaurantsCell;
import org.ieatta.cells.model.SectionTitleCellModel;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.provide.IEAEditKey;
import org.ieatta.provide.MainSegueIdentifier;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

public class NearRestaurantsTask extends FragmentTask {
    @VisibleForTesting
    public NearRestaurantsTask(HistoryEntry entry) {
        super(entry);
    }

    public NearRestaurantsTask(HistoryEntry entry, Activity activity, PageViewModel model) {
        super(entry, activity, model);
    }

    public RealmResults<DBRestaurant> restaurants;

    @Override
    public void onItemClick(View view, NSIndexPath indexPath, Object model, int position, boolean isLongClick) {
        if (model instanceof DBRestaurant) {
            DBRestaurant item = (DBRestaurant) model;

            ((PageActivity)NearRestaurantsTask.this.activity).loadPage(
                    new HistoryEntry(MainSegueIdentifier.detailRestaurantSegueIdentifier,item.getUUID()));
        }
    }

    enum NearRestaurantSection {
        section_more_items,//= 0
        section_restaurants, //= 1
    }

    /**
     * Execute Task for nearby restaurants.
     *
     * @return
     */
    public Task<Void> executeTask() {
        final Location location = this.entry.getLocation();

        return LocalDatabaseQuery.queryNearRestaurants(location).onSuccess(new Continuation<RealmResults<DBRestaurant>, Void>() {
            @Override
            public Void then(Task<RealmResults<DBRestaurant>> task) throws Exception {
                NearRestaurantsTask.this.restaurants = task.getResult();
                return null;
            }
        });
    }

    @Override
    public void prepareUI() {
        super.prepareUI();

        this.manager.setRegisterCellClassWhenSelected(IEANearRestaurantsCell.getType(), NearRestaurantSection.section_restaurants.ordinal());
        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Nearby_Restaurants), NearRestaurantSection.section_restaurants.ordinal());
    }

    @Override
    public void postUI() {

        this.manager.setSectionItems(this.restaurants, NearRestaurantSection.section_restaurants.ordinal());
    }

}
