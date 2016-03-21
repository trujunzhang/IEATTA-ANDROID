package org.ieatta.test.adapter;

import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityUnitTestCase;

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

public class RecycleViewManagerTest extends ActivityUnitTestCase {
    private RecycleViewManager manager;
    private DTTableViewManager mProvider;

    public RecycleViewManagerTest(Class activityClass) {
        super(activityClass);
    }

    @Before
    public void setUp() throws Exception {
        manager = new RecycleViewManager();
        mProvider = manager.getTableManager();

        TableViewConfiguration configuration = new TableViewConfiguration(new TableViewConfiguration.Builder(this.getActivity().getApplicationContext()));
        TableViewControllerAdapter adapter = new TableViewControllerAdapter(this.mProvider);
        mProvider.setConfiguration(configuration, adapter);
    }

    @Test
    public void testHeaderView()  {
        HeaderFooterViewModel headerViewModel = new HeaderFooterViewModel("testHeaderView");
        // when
        this.manager.setRegisterHeaderView(HeaderView.getType());
        this.manager.setHeaderItem(headerViewModel, HeaderView.getType());
        // how
        this.manager.updateTableSections();
        // then
        Object expectModel = this.mProvider.memoryStorage.getRowModel(0);
        assertThat("The same cell type.", headerViewModel.equals(expectModel));
    }

    @Test
    public void testFooterView()  {
        HeaderFooterViewModel footerViewModel = new HeaderFooterViewModel("testFooterView");
        // when
        this.manager.setRegisterFooterClass(FooterView.getType());
        this.manager.setFooterItem(footerViewModel, FooterView.getType());
        // how
        this.manager.updateTableSections();
        // then
        Object expectModel = this.mProvider.memoryStorage.getRowModel(0);
        assertThat("The same cell type.", footerViewModel.equals(expectModel));
    }
}
