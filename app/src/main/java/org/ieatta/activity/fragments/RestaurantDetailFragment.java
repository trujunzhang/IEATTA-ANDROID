package org.ieatta.activity.fragments;

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
import org.ieatta.cells.IEARestaurantEventsCell;
import org.ieatta.cells.header.IEARestaurantDetailHeaderCell;
import org.ieatta.cells.model.IEARestaurantDetailHeader;
import org.ieatta.cells.model.SectionTitleCellModel;
import org.ieatta.provide.IEAEditKey;
import org.ieatta.tasks.RestaurantDetailTask;
import org.ieatta.views.ObservableWebView;

import bolts.Continuation;
import bolts.Task;

public class RestaurantDetailFragment extends PageFragment {

    public static final RecyclerOnItemClickListener itemClickListener = new RecyclerOnItemClickListener() {
        @Override
        public void onItemClick(View view, NSIndexPath indexPath, Object model, int position, boolean isLongClick) {

        }
    };

    enum RestaurantDetailSection {
        section_header,//= 0
        section_google_mapaddress,//= 1
        section_events,//= 2
        section_photogallery,//= 3
        section_reviews,//= 4
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
        this.manager.setRegisterCellClass(IEARestaurantDetailHeaderCell.getType(), RestaurantDetailSection.section_header.ordinal());
        this.manager.setRegisterCellClass(IEARestaurantEventsCell.getType(), RestaurantDetailSection.section_events.ordinal());

        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Events_Recorded), RestaurantDetailSection.section_events.ordinal());
    }

    @Override
    public void loadPage() {
        manager.startManagingWithDelegate(mRecycleView);
        manager.setOnItemClickListener(itemClickListener);

        this.setupUI();

        String uuid = "1CE562A4-A978-4B75-9B7B-2F3CF9F42A04";
        task.executeTask(uuid).onSuccess(new Continuation<Void, Object>() {
            @Override
            public Object then(Task<Void> task) throws Exception {
                RestaurantDetailFragment.this.reloadPage();
                return null;
            }
        }).continueWith(new Continuation<Object, Object>() {
            @Override
            public Object then(Task<Object> task) throws Exception {
                return null;
            }
        });
    }

    private void reloadPage() {
        this.manager.setSectionItems(CollectionUtil.createList(new IEARestaurantDetailHeader(this.task.restaurant)), RestaurantDetailSection.section_header.ordinal());
//        this.manager.showGoogleMapAddress(RestaurantDetailSection.section_google_mapaddress.ordinal());
    }
}
