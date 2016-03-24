package org.ieatta.activity.fragments.search;

import android.content.Context;

import org.ieatta.IEAApp;
import org.wikipedia.concurrency.SaneAsyncTask;

/** AsyncTask to clear out recent search entries. */
public class DeleteAllRecentSearchesTask extends SaneAsyncTask<Void> {
    private final IEAApp app;

    public DeleteAllRecentSearchesTask(Context context) {
        app = (IEAApp) context.getApplicationContext();
    }

    @Override
    public Void performTask() throws Throwable {
        app.getDatabaseClient(RecentSearch.class).deleteAll();
        return null;
    }
}
