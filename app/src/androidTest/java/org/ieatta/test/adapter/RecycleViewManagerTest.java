package org.ieatta.test.adapter;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;

import com.tableview.RecycleViewManager;
import com.tableview.TableViewControllerAdapter;
import com.tableview.storage.DTTableViewManager;
import com.tableview.storage.TableViewConfiguration;

import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.server.cache.ThumbnailImageUtil;
import org.ieatta.test.adapter.cell.HeaderView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wikipedia.util.log.L;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bolts.Continuation;
import bolts.Task;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class RecycleViewManagerTest {
    private RecycleViewManager manager;
    private DTTableViewManager provider;

    @Before
    public void setUp() throws Exception {
        manager = new RecycleViewManager();
        provider = manager.getTableManager();

        Context context = null;
        TableViewConfiguration configuration = new TableViewConfiguration(new TableViewConfiguration.Builder(context));
        provider.setConfiguration(configuration,new TableViewControllerAdapter(this.provider));
    }

    @Test
    public void testHeaderView() throws InterruptedException {
        int layoutResId = 123;
        this.manager.setHeaderView(HeaderView.getType(layoutResId));
        int expect = this.provider.memoryStorage.getItemViewType(0);

        assertThat("The same type.", (layoutResId == (expect)));
    }
}
