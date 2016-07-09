package org.ieatta.test.tasks;

import android.support.test.runner.AndroidJUnit4;

import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.database.models.DBRecipe;
import org.ieatta.provide.MainSegueIdentifier;
import org.ieatta.tasks.OrderedRecipesTask;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bolts.Continuation;
import bolts.Task;
import io.realm.RealmResults;

@RunWith(AndroidJUnit4.class)
public class OrderedRecipesTaskTest {
    private static final int TASK_COMPLETION_TIMEOUT = 20000;
    private OrderedRecipesTask task;
    private String restaurantUUID = "1CE562A4-A978-4B75-9B7B-2F3CF9F42A04"; // The Flying Falafel
    private String eventUUID = "07B2D33C-F11D-404B-9D78-016D16BEE9FE"; // White Truffies
    private String teamUUID = "197C0BEF-B432-47B8-988B-99406643623A";// Dolores Chavez

    @Before
    public void setUp() throws Exception {
        HistoryEntry entry = new HistoryEntry(MainSegueIdentifier.detailOrderedRecipesSegueIdentifier,eventUUID,teamUUID);
        this.task = new OrderedRecipesTask(entry);
    }

    @Test
    public void testOrderedRecipes() throws InterruptedException {
        final CountDownLatch completionLatch = new CountDownLatch(1);
        task.executeTask().onSuccess(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
//                OrderedRecipesTask _task = OrderedRecipesTaskTest.this.task;
//                RealmResults<DBRecipe> recipes = _task.recipes;
                return null;
            }
        }).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                Exception error = task.getError();
                completionLatch.countDown();
                return null;
            }
        });
        completionLatch.await(TASK_COMPLETION_TIMEOUT, TimeUnit.MILLISECONDS);
    }
}