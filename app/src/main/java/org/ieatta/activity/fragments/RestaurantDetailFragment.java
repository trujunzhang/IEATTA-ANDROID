package org.ieatta.activity.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tableview.RecycleViewManager;
import com.tableview.adapter.NSIndexPath;
import com.tableview.adapter.RecyclerOnItemClickListener;
import com.tableview.utils.CollectionUtil;

import org.ieatta.IEAApp;
import org.ieatta.R;
import org.ieatta.cells.IEANearRestaurantMoreCell;
import org.ieatta.cells.IEANearRestaurantsCell;
import org.ieatta.cells.header.IEARestaurantDetailHeaderCell;
import org.ieatta.cells.model.IEANearRestaurantMore;
import org.ieatta.cells.model.IEARestaurantDetailHeader;
import org.ieatta.cells.model.SectionTitleCellModel;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.provide.IEAEditKey;
import org.ieatta.tasks.NearRestaurantsTask;
import org.ieatta.tasks.RestaurantDetailTask;
import org.ieatta.utils.LocationUtil;
import org.ieatta.views.ObservableWebView;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

public class RestaurantDetailFragment extends PageFragment {
    enum RestaurantDetailSection {
        sectionHeader,//= 0
        sectionGoogleMapAddress,//= 1
        sectionEvents,//= 2
        sectionPhotos,//= 3
        sectionReviews,//= 4
    }

    private IEAApp app;
    private RecycleViewManager manager;
    private ObservableWebView mRecycleView;

    private RestaurantDetailTask task = new RestaurantDetailTask();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_recycleview, container, false);
        mRecycleView = (ObservableWebView) view.findViewById(R.id.recycleView);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (IEAApp) getActivity().getApplicationContext();
        manager = new RecycleViewManager(this.getActivity().getApplicationContext());
    }

    private void setupUI() {
        this.manager.setRegisterCellClass(IEARestaurantDetailHeaderCell.getType(), RestaurantDetailSection.sectionHeader.ordinal());
        this.manager.setSectionItems(CollectionUtil.createList(new IEARestaurantDetailHeader(self, self.restaurant)), RestaurantDetailSection.sectionHeader.ordinal());

//        this.manager.showGoogleMapAddress(RestaurantDetailSection.sectionGoogleMapAddress.ordinal());
        this.manager.setRegisterCellClassWhenSelected(IEARestaurantEventsCell.getType(), RestaurantDetailSection.sectionEvents.ordinal());
        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Events_Recorded), RestaurantDetailSection.sectionEvents.ordinal());

    }

    @Override
    public void loadPage() {
        manager.startManagingWithDelegate(mRecycleView);
        manager.setOnItemClickListener(new RecyclerOnItemClickListener() {
            @Override
            public void onItemClick(View view, NSIndexPath indexPath, Object model, int position, boolean isLongClick) {

            }
        });

        this.setupUI();
        this.manager.setSectionItems(IEANearRestaurantMore.getMoresItems(), NearRestaurantSection.section_more_items.ordinal());

        Location location = LocationUtil.getLocation();
        task.executeTask(location).onSuccess(new Continuation<Void, Object>() {
            @Override
            public Object then(Task<Void> task) throws Exception {
                RealmResults<DBRestaurant> restaurants = RestaurantDetailFragment.this.task.getRestaurants();
                RestaurantDetailFragment.this.manager.setSectionItems(restaurants, NearRestaurantSection.section_restaurants.ordinal());
                return null;
            }
        },Task.UI_THREAD_EXECUTOR).continueWith(new Continuation<Object, Object>() {
            @Override
            public Object then(Task<Object> task) throws Exception {
                Exception error = task.getError();
                return null;
            }
        });
    }
}
