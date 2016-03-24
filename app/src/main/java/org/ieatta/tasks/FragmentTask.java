package org.ieatta.tasks;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.tableview.RecycleViewManager;

import org.ieatta.activity.PageViewModel;
import org.ieatta.activity.history.HistoryEntry;
import org.wikipedia.util.DimenUtil;

import bolts.Task;

public abstract class FragmentTask {

    protected RecycleViewManager manager;
    protected PageViewModel model;
    public HistoryEntry entry;

    @VisibleForTesting
    public FragmentTask(HistoryEntry entry) {
        this.entry = entry;
    }

    public FragmentTask(HistoryEntry entry, Context context, PageViewModel model) {
        this.entry = entry;
        this.model = model;
        this.manager = new RecycleViewManager(context);
    }

    public abstract Task<Void> executeTask();

    public abstract void prepareUI();

    public abstract void postUI();


    protected int getEmptyHeaderViewHeight() {
        return DimenUtil.getDisplayWidthPx();
    }


}
