package org.ieatta.activity.fragments;

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
import org.ieatta.views.ObservableWebView;

public class NearRestaurantsFragment extends PageFragment {
    enum NearRestaurantSection {
        section_more_items,//= 0
        section_restaurants, //= 1
    }

    private IEAApp app;
    private RecycleViewManager manager;
    private ObservableWebView mRecycleView;

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
        this.manager.setRegisterCellClassWhenSelected(IEANearRestaurantsCell.getType(), NearRestaurantSection.section_restaurants.ordinal());
        this.manager.setRegisterCellClassWhenSelected(IEANearRestaurantMoreCell.getType(), NearRestaurantSection.section_more_items.ordinal());

        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.More), NearRestaurantSection.section_more_items.ordinal());
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
    }
}
