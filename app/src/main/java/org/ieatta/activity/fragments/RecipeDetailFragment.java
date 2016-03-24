package org.ieatta.activity.fragments;

import android.os.Bundle;
import android.view.View;

import com.tableview.RecycleViewManager;
import com.tableview.adapter.NSIndexPath;
import com.tableview.adapter.RecyclerOnItemClickListener;

import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.cells.model.IEAHeaderViewModel;
import org.ieatta.tasks.RecipeDetailTask;

import bolts.Continuation;
import bolts.Task;

public class RecipeDetailFragment extends DetailFragment {

    private RecipeDetailTask task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void loadPage(HistoryEntry entry) {
        this.task = new RecipeDetailTask(entry,this.getContext(),this.model);

        task.setupWebView(webView);
        this.task.prepareUI();

//        String restaurantUUID = "1CE562A4-A978-4B75-9B7B-2F3CF9F42A04"; // The Flying Falafel
////        String restaurantUUID = "33ED9F31-F6A5-43A4-8D11-8E511CA0BD39"; // The Spice Jar
//        String eventUUID = "07B2D33C-F11D-404B-9D78-016D16BEE9FE"; // White Truffies
//        String teamUUID = "197C0BEF-B432-47B8-988B-99406643623A";// Dolores Chavez
//        String recipeUUID = "95B62D6F-87DF-47E2-8C84-EADAE131BB5D"; // Dark Gelate

        task.executeTask().onSuccess(new Continuation<Void, Object>() {
            @Override
            public Object then(Task<Void> task) throws Exception {
                RecipeDetailFragment.this.postLoadPage();
                return null;
            }
        }).continueWith(new Continuation<Object, Object>() {
            @Override
            public Object then(Task<Object> task) throws Exception {
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
