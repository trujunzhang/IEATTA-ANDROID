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

    public static final RecyclerOnItemClickListener itemClickListener = new RecyclerOnItemClickListener() {
        @Override
        public void onItemClick(View view, NSIndexPath indexPath, Object model, int position, boolean isLongClick) {

        }
    };

    @Override
    public void onContentHeightChanged(int contentHeight) {
        this.manager.updateHeaderItem(new IEAHeaderViewModel(contentHeight));
    }


    private RecycleViewManager manager;

    private RestaurantDetailTask task ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = new RecycleViewManager(this.getActivity().getApplicationContext());
    }


    @Override
    public void loadPage(HistoryEntry entry) {
        this.task = new RestaurantDetailTask(entry,this.getContext(),this.model);

        manager.startManagingWithDelegate(webView);
        manager.setOnItemClickListener(itemClickListener);

        this.task.prepareUI();

//        String restaurantUUID = "1CE562A4-A978-4B75-9B7B-2F3CF9F42A04"; // The Flying Falafel
//        String restaurantUUID = "33ED9F31-F6A5-43A4-8D11-8E511CA0BD39"; // The Spice Jar
//        String restaurantUUID = entry.getHPara();
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
