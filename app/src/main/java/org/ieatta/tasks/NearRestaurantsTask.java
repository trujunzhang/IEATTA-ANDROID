package org.ieatta.tasks;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tableview.adapter.NSIndexPath;
import com.tableview.adapter.RecyclerOnItemClickListener;
import com.tableview.RecycleViewManager;

import org.ieatta.R;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.cells.IEANearRestaurantsCell;
import org.ieatta.cells.model.SectionTitleCellModel;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.provide.IEAEditKey;
import org.ieatta.provide.MainSegueIdentifier;
import org.ieatta.tasks.NearRestaurantsTask;
import org.ieatta.utils.LocationUtil;
import org.ieatta.views.ObservableWebView;

import bolts.Continuation;
import bolts.Task;

import android.content.Context;
import android.location.Location;

import com.parse.ParseGeoPoint;

import org.ieatta.activity.PageViewModel;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.cells.IEANearRestaurantsCell;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.query.LocalDatabaseQuery;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.support.annotation.VisibleForTesting;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

public class NearRestaurantsTask extends FragmentTask {
    @VisibleForTesting
    public NearRestaurantsTask(HistoryEntry entry) {
        super(entry);
    }

    public NearRestaurantsTask(HistoryEntry entry, Context context, PageViewModel model) {
        super(entry, context, model);
    }

    public RealmResults<DBRestaurant> restaurants;

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
//        this.manager.setRegisterCellClassWhenSelected(IEANearRestaurantMoreCell.getType(), NearRestaurantSection.section_more_items.ordinal());
        this.manager.setRegisterCellClassWhenSelected(IEANearRestaurantsCell.getType(), NearRestaurantSection.section_restaurants.ordinal());

//        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.More), NearRestaurantSection.section_more_items.ordinal());
        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Nearby_Restaurants), NearRestaurantSection.section_restaurants.ordinal());

    }

    @Override
    public void postUI() {
        // this.manager.setSectionItems(IEANearRestaurantMore.getMoresItems(), NearRestaurantSection.section_more_items.ordinal());
        this.manager.setSectionItems(this.restaurants, NearRestaurantSection.section_restaurants.ordinal());
    }

}
