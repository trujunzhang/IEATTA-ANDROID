package org.ieatta.activity.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tableview.RecycleViewManager;
import com.tableview.adapter.NSIndexPath;
import com.tableview.adapter.RecyclerOnItemClickListener;

import org.ieatta.IEAApp;
import org.ieatta.R;
import org.ieatta.cells.IEARestaurantEventsCell;
import org.ieatta.cells.header.IEARestaurantDetailHeaderCell;
import org.ieatta.cells.model.SectionTitleCellModel;
import org.ieatta.provide.IEAEditKey;
import org.ieatta.tasks.RestaurantDetailTask;
import org.ieatta.views.ObservableWebView;

public class RestaurantDetailFragment extends PageFragment {

    public static final RecyclerOnItemClickListener itemClickListener = new RecyclerOnItemClickListener() {
        @Override
        public void onItemClick(View view, NSIndexPath indexPath, Object model, int position, boolean isLongClick) {

        }
    };

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
        this.manager.setRegisterCellClass(IEARestaurantEventsCell.getType(), RestaurantDetailSection.sectionEvents.ordinal());
//        this.manager.setSectionItems(CollectionUtil.createList(new IEARestaurantDetailHeader(this.restaurant)), RestaurantDetailSection.sectionHeader.ordinal());

//        this.manager.showGoogleMapAddress(RestaurantDetailSection.sectionGoogleMapAddress.ordinal());

        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Events_Recorded), RestaurantDetailSection.sectionEvents.ordinal());
    }

    @Override
    public void loadPage() {
        manager.startManagingWithDelegate(mRecycleView);
        manager.setOnItemClickListener(itemClickListener);

        this.setupUI();

//        Location location = LocationUtil.getLocation();
//        task.executeTask(location).onSuccess(new Continuation<Void, Object>() {
//            @Override
//            public Object then(Task<Void> task) throws Exception {
//                RealmResults<DBRestaurant> restaurants = RestaurantDetailFragment.this.task.getRestaurants();
//                RestaurantDetailFragment.this.manager.setSectionItems(restaurants, NearRestaurantSection.section_restaurants.ordinal());
//                return null;
//            }
//        },Task.UI_THREAD_EXECUTOR).continueWith(new Continuation<Object, Object>() {
//            @Override
//            public Object then(Task<Object> task) throws Exception {
//                Exception error = task.getError();
//                return null;
//            }
//        });
    }
}
