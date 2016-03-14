package org.ieatta.test.tasks;

import android.support.test.runner.AndroidJUnit4;

import org.ieatta.tasks.OrderedRecipesTask;
import org.ieatta.tasks.RecipeDetailTask;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bolts.Continuation;
import bolts.Task;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailTaskTest {
    private static final int TASK_COMPLETION_TIMEOUT = 20000;
    private RecipeDetailTask task = new RecipeDetailTask();
    private String restaurantUUID = "1CE562A4-A978-4B75-9B7B-2F3CF9F42A04";
    private String eventUUID = "07B2D33C-F11D-404B-9D78-016D16BEE9FE";
    private String teamUUID = "5C52C84D-4E0B-4727-B3FD-274C9F004580";
    private String recipeUUID = "87BDA6B4-3928-44FA-9047-D62F666ADC9F";

    @Test
    public void testOrderedRecipes() throws InterruptedException {
        final CountDownLatch completionLatch = new CountDownLatch(1);
        task.executeTask(restaurantUUID,eventUUID,teamUUID,recipeUUID).onSuccess(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                completionLatch.countDown();
                return null;
            }
        });
        completionLatch.await(TASK_COMPLETION_TIMEOUT, TimeUnit.MILLISECONDS);
    }
}