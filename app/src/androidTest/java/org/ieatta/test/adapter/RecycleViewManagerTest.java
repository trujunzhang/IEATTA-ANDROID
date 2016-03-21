package org.ieatta.test.adapter;

import android.support.test.runner.AndroidJUnit4;

import com.tableview.RecycleViewManager;
import com.tableview.TableViewControllerAdapter;
import com.tableview.storage.DTTableViewManager;
import com.tableview.storage.TableViewConfiguration;
import com.tableview.storage.models.CellType;

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
        int layoutResId = 123;
        String modelName = "testHeaderView";
        HeaderFooterViewModel headerViewModel = new HeaderFooterViewModel(modelName);

        CellType headerType = HeaderView.getType(layoutResId);

        this.manager.setRegisterHeaderView(headerType);
        this.manager.setHeaderItem(headerViewModel, headerType);
        this.manager.updateTableSections();

        Object expectModel = this.mProvider.memoryStorage.getRowModel(0);
        assertThat("The same cell type.", headerViewModel.equals(expectModel));
    }

    @Test
    public void testFooterView()  {
        int layoutResId = 123;
        String modelName = "testFooterView";
        HeaderFooterViewModel footerViewModel = new HeaderFooterViewModel(modelName);

        CellType footerType = FooterView.getType(layoutResId);

        this.manager.setRegisterFooterClass(footerType);
        this.manager.setFooterItem(footerViewModel, footerType);
        this.manager.updateTableSections();

        Object expectModel = this.mProvider.memoryStorage.getRowModel(0);
        assertThat("The same cell type.", footerViewModel.equals(expectModel));
    }
}
