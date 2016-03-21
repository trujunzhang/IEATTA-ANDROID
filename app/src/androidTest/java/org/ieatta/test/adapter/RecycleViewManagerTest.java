package org.ieatta.test.adapter;

import android.support.test.runner.AndroidJUnit4;

import com.tableview.RecycleViewManager;
import com.tableview.TableViewControllerAdapter;
import com.tableview.storage.DTTableViewManager;
import com.tableview.storage.TableViewConfiguration;
import com.tableview.storage.models.CellType;

import org.ieatta.cells.model.IEAHeaderViewModel;
import org.ieatta.test.adapter.cell.HeaderView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class RecycleViewManagerTest {
    private RecycleViewManager manager;
    private DTTableViewManager provider;

    @Before
    public void setUp() throws Exception {
        manager = new RecycleViewManager();
        provider = manager.getTableManager();

        TableViewConfiguration configuration = new TableViewConfiguration(new TableViewConfiguration.Builder());
        TableViewControllerAdapter adapter = new TableViewControllerAdapter(this.provider);
        provider.setConfiguration(configuration, adapter);
    }

    @Test
    public void testHeaderView()  {
        int layoutResId = 123;
        CellType headerType = HeaderView.getType(layoutResId);

        this.manager.setRegisterHeaderView(headerType);
        this.manager.updateHeaderItem(new IEAHeaderViewModel(123));

        int expect = this.provider.memoryStorage.getItemViewType(0);
        assertThat("The same cell type.", (layoutResId == (expect)));
    }
}
