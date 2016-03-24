package org.ieatta.tasks;

import android.content.Context;

import com.tableview.RecycleViewManager;

import org.ieatta.activity.PageViewModel;
import org.ieatta.activity.history.HistoryEntry;

import bolts.Task;

public abstract class FragmentTask {

    protected RecycleViewManager manager;
    protected PageViewModel model;
    public HistoryEntry entry;

    public FragmentTask(HistoryEntry entry,Context context,PageViewModel model) {
        this.entry = entry;
        this.model = model;
        this.manager = new RecycleViewManager(context);
    }

    public abstract Task<Void> executeTask();

    public abstract void prepareUI();

    public abstract void postUI();

}
