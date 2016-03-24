package org.ieatta.test.tasks;

import android.support.test.runner.AndroidJUnit4;

import org.ieatta.activity.gallery.GalleryCollection;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.provide.MainSegueIdentifier;
import org.ieatta.tasks.OrderedRecipesTask;
import org.ieatta.tasks.RecipeDetailTask;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bolts.Continuation;
import bolts.Task;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailTaskTest {
    private static final int TASK_COMPLETION_TIMEOUT = 20000;
    private RecipeDetailTask task;

    String restaurantUUID = "1CE562A4-A978-4B75-9B7B-2F3CF9F42A04"; // The Flying Falafel
    String eventUUID = "07B2D33C-F11D-404B-9D78-016D16BEE9FE"; // White Truffies
    String teamUUID = "197C0BEF-B432-47B8-988B-99406643623A";// Dolores Chavez
    String recipeUUID = "95B62D6F-87DF-47E2-8C84-EADAE131BB5D"; // Dark Gelate

    @Before
    public void setUp() throws Exception {
        HistoryEntry entry = new HistoryEntry(MainSegueIdentifier.detailRecipeSegueIdentifier,recipeUUID);
        this.task = new RecipeDetailTask(entry,null,null);
    }

    @Test
    public void testOrderedRecipes() throws InterruptedException {
        final CountDownLatch completionLatch = new CountDownLatch(1);
        task.executeTask().onSuccess(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                RecipeDetailTask _task = RecipeDetailTaskTest.this.task;
                GalleryCollection collection = _task.thumbnailGalleryCollection;
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