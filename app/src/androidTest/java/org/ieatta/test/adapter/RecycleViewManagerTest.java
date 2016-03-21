package org.ieatta.test.adapter;

import android.support.test.runner.AndroidJUnit4;

import com.tableview.RecycleViewManager;
import com.tableview.TableViewControllerAdapter;
import com.tableview.storage.DTTableViewManager;
import com.tableview.storage.TableViewConfiguration;

import org.ieatta.test.adapter.cell.FooterView;
import org.ieatta.test.adapter.cell.HeaderView;
import org.ieatta.test.adapter.cell.HeaderFooterViewModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class RecycleViewManagerTest {
    private RecycleViewManager manager;
    private DTTableViewManager mProvider;

    @Before
    public void setUp() throws Exception {
        manager = new RecycleViewManager();
        mProvider = manager.getTableManager();

        TableViewConfiguration configuration = new TableViewConfiguration(new TableViewConfiguration.Builder());
        TableViewControllerAdapter adapter = new TableViewControllerAdapter(this.mProvider);
        mProvider.setConfiguration(configuration, adapter);
    }

    @Test
    public void testHeaderView()  {
        HeaderFooterViewModel headerViewModel = new HeaderFooterViewModel("testHeaderView");

        this.manager.setRegisterHeaderView(HeaderView.getType());
        this.manager.setHeaderItem(headerViewModel, HeaderView.getType());
        this.manager.updateTableSections();

        Object expectModel = this.mProvider.memoryStorage.getRowModel(0);
        assertThat("The same cell type.", headerViewModel.equals(expectModel));
    }

    @Test
    public void testFooterView()  {
        HeaderFooterViewModel footerViewModel = new HeaderFooterViewModel("testFooterView");

        this.manager.setRegisterFooterClass(FooterView.getType());
        this.manager.setFooterItem(footerViewModel, FooterView.getType());
        this.manager.updateTableSections();

        Object expectModel = this.mProvider.memoryStorage.getRowModel(0);
        assertThat("The same cell type.", footerViewModel.equals(expectModel));
    }
}
