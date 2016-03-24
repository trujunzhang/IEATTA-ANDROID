package org.ieatta.activity.fragments;

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

public class NearRestaurantsFragment extends PageFragment {

    public final RecyclerOnItemClickListener itemClickListener = new RecyclerOnItemClickListener() {
        @Override
        public void onItemClick(View view, NSIndexPath indexPath, Object model, int position, boolean isLongClick) {
            if (model instanceof DBRestaurant) {
                DBRestaurant item = (DBRestaurant) model;
                HistoryEntry newEntry =new HistoryEntry(MainSegueIdentifier.detailRestaurantSegueIdentifier,item.getUUID());

                NearRestaurantsFragment.this.loadPage(newEntry,false,getWebViewScrollY(webView));
            }
        }
    };



    private RecycleViewManager manager;
    private ObservableWebView webView;

    private NearRestaurantsTask task ;

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

        manager = new RecycleViewManager(this.getActivity().getApplicationContext());
    }


    @Override
    public void loadPage(HistoryEntry entry) {
        this.task = new NearRestaurantsTask(entry,this.getContext(),this.model);

        manager.startManagingWithDelegate(webView);
        manager.setOnItemClickListener(itemClickListener);

        this.task.prepareUI();

        task.executeTask().onSuccess(new Continuation<Void, Object>() {
            @Override
            public Object then(Task<Void> task) throws Exception {
                NearRestaurantsFragment.this.postLoadPage();
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR).continueWith(new Continuation<Object, Object>() {
            @Override
            public Object then(Task<Object> task) throws Exception {
                Exception error = task.getError();
                return null;
            }
        });
    }

    @Override
    public void postLoadPage() {
      this.task.postUI();

        super.postLoadPage();
    }
}
