package org.ieatta.test.adapter;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.tableview.storage.MemoryStorage;

import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.cells.model.IEAReviewsCellModel;
import org.ieatta.provide.MainSegueIdentifier;
import org.ieatta.tasks.EventDetailTask;
import org.ieatta.test.adapter.cell.FooterView;
import org.ieatta.test.adapter.cell.HeaderFooterViewModel;
import org.ieatta.test.adapter.cell.HeaderView;
import org.ieatta.test.adapter.cell.ItemViewModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bolts.Continuation;
import bolts.Task;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class MemoryStorageTest {
    private MemoryStorage memoryStorage;
    private final HeaderFooterViewModel headerViewModel = new HeaderFooterViewModel("testHeaderView");
    private final HeaderFooterViewModel footerViewModel = new HeaderFooterViewModel("testFooterView");

    @Before
    public void setUp() throws Exception {
        this.memoryStorage = new MemoryStorage(null);
    }

    @Test
    public void testEmptySections() throws InterruptedException {
        //when
        this.memoryStorage.updateTableSections();
        //How
        int itemCount = this.memoryStorage.getItemCount();
        //then
        assertThat("Fetched item count", (itemCount == 0));
    }

    @Test
    public void testPositionStart() throws InterruptedException {
        //When
        this.memoryStorage.setHeaderItem(headerViewModel, HeaderView.getType());
        this.memoryStorage.setFooterItem(footerViewModel, FooterView.getType());

        int forSectionIndex = 0;
        this.memoryStorage.setSectionHeaderModel(headerViewModel, forSectionIndex, HeaderView.getType());
        List<Object> items= new LinkedList<Object>(){
            {
                add(new ItemViewModel("first"));
                add(new ItemViewModel("second"));
                add(new ItemViewModel("third"));
            }
        };
        this.memoryStorage.addItems(items, forSectionIndex);

        this.memoryStorage.updateTableSections();
        //How
        int itemCount = this.memoryStorage.getItemCount();
        //Then
        assertThat("Fetched item count", (itemCount == 6));
    }
}