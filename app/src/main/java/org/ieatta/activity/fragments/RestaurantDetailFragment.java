package org.ieatta.activity.fragments;

import android.os.Bundle;
import android.view.View;

import com.tableview.RecycleViewManager;
import com.tableview.adapter.NSIndexPath;
import com.tableview.adapter.RecyclerOnItemClickListener;

import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.cells.model.IEAHeaderViewModel;
import org.ieatta.tasks.RestaurantDetailTask;

import bolts.Continuation;
import bolts.Task;

public class RestaurantDetailFragment extends DetailFragment {

    private RestaurantDetailTask task ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void loadPage(HistoryEntry entry) {
        this.task = new RestaurantDetailTask(entry,this.getActivity(),this.model);

        this.task.setupWebView(webView);
        this.task.prepareUI();

        task.executeTask().onSuccess(new Continuation<Void, Object>() {
            @Override
            public Object then(Task<Void> task) throws Exception {
                RestaurantDetailFragment.this.postLoadPage();
                return null;
            }
        }).continueWith(new Continuation<Object, Object>() {
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
