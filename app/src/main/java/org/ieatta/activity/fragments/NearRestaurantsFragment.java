package org.ieatta.activity.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tableview.adapter.NSIndexPath;
import com.tableview.adapter.RecyclerOnItemClickListener;
import com.tableview.RecycleViewManager;

import org.ieatta.IEAApp;
import org.ieatta.R;
import org.ieatta.cells.IEANearRestaurantMoreCell;
import org.ieatta.cells.IEANearRestaurantsCell;
import org.ieatta.cells.model.SectionTitleCellModel;
import org.ieatta.cells.model.IEANearRestaurantMore;
import org.ieatta.provide.IEAEditKey;
import org.ieatta.tasks.NearRestaurantsTask;
import org.ieatta.utils.LocationUtil;
import org.ieatta.views.ObservableWebView;

import bolts.Continuation;
import bolts.Task;

public class NearRestaurantsFragment extends PageFragment {

    public static final RecyclerOnItemClickListener itemClickListener = new RecyclerOnItemClickListener() {
        @Override
        public void onItemClick(View view, NSIndexPath indexPath, Object model, int position, boolean isLongClick) {

        }
    };

    enum NearRestaurantSection {
        section_more_items,//= 0
        section_restaurants, //= 1
    }

    private IEAApp app;
    private RecycleViewManager manager;
    private ObservableWebView webView;

    private NearRestaurantsTask task = new NearRestaurantsTask();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_recycleview, container, false);
        webView = (ObservableWebView) view.findViewById(R.id.recycleView);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (IEAApp) getActivity().getApplicationContext();
        manager = new RecycleViewManager(this.getActivity().getApplicationContext());
    }

    private void setupUI() {
//        this.manager.setRegisterCellClassWhenSelected(IEANearRestaurantMoreCell.getType(), NearRestaurantSection.section_more_items.ordinal());
        this.manager.setRegisterCellClassWhenSelected(IEANearRestaurantsCell.getType(), NearRestaurantSection.section_restaurants.ordinal());

//        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.More), NearRestaurantSection.section_more_items.ordinal());
        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Nearby_Restaurants), NearRestaurantSection.section_restaurants.ordinal());
    }

    @Override
    public void loadPage() {
        manager.startManagingWithDelegate(webView);
        manager.setOnItemClickListener(itemClickListener);

        this.setupUI();

        Location location = LocationUtil.getLocation();
        task.executeTask(location).onSuccess(new Continuation<Void, Object>() {
            @Override
            public Object then(Task<Void> task) throws Exception {
               NearRestaurantsFragment.this.postLoadPage();
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

    @Override
    public void postLoadPage(){
        // this.manager.setSectionItems(IEANearRestaurantMore.getMoresItems(), NearRestaurantSection.section_more_items.ordinal());
        this.manager.setSectionItems(this.task.getRestaurants(),NearRestaurantSection.section_restaurants.ordinal());
    }
}
