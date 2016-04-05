package org.ieatta.test.adapter;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.tableview.storage.MemoryStorage;

import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.cells.model.IEAReviewsCellModel;
import org.ieatta.provide.MainSegueIdentifier;
import org.ieatta.tasks.EventDetailTask;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bolts.Continuation;
import bolts.Task;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class MemoryStorageTest {
    private MemoryStorage memoryStorage;

    @Before
    public void setUp() throws Exception {
        this.memoryStorage = new MemoryStorage(null);
    }

    @Test
    public void testEmptySections() throws InterruptedException {
        this.memoryStorage.updateTableSections();
        int itemCount = this.memoryStorage.getItemCount();
        assertThat("Fetched item count", (itemCount == 0));
    }
}