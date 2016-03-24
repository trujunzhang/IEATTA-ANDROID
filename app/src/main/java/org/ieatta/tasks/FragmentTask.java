package org.ieatta.tasks;

import org.ieatta.activity.history.HistoryEntry;

import bolts.Task;

public abstract class FragmentTask {
    public HistoryEntry entry;

    public FragmentTask(HistoryEntry entry) {
        this.entry = entry;
    }

    public abstract Task<Void> executeTask();

}
