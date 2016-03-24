package org.ieatta.test.adapter;

import android.test.ActivityUnitTestCase;

import com.tableview.RecycleViewManager;
import com.tableview.TableViewControllerAdapter;
import com.tableview.storage.DTTableViewManager;
import com.tableview.storage.TableViewConfiguration;

import org.ieatta.test.adapter.cell.FooterView;
import org.ieatta.test.adapter.cell.HeaderFooterViewModel;
import org.ieatta.test.adapter.cell.HeaderView;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class RecycleViewManagerTest extends ActivityUnitTestCase {
    private RecycleViewManager manager;
    private DTTableViewManager mProvider;

    private final HeaderFooterViewModel headerViewModel = new HeaderFooterViewModel("testHeaderView");
    private final HeaderFooterViewModel footerViewModel = new HeaderFooterViewModel("testFooterView");

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
        // when
        this.manager.setRegisterFooterClass(FooterView.getType());
        this.manager.setFooterItem(footerViewModel, FooterView.getType());
        // how
        this.manager.updateTableSections();
        // then
        Object expectModel = this.mProvider.memoryStorage.getRowModel(0);
        assertThat("The same cell type.", footerViewModel.equals(expectModel));
    }

    @Test
    public void testHeaderFooterView()  {
        // when
        this.manager.setRegisterHeaderClass(HeaderView.getType());
        this.manager.setRegisterFooterClass(FooterView.getType());
        this.manager.setHeaderItem(headerViewModel, HeaderView.getType());
        this.manager.setFooterItem(footerViewModel, FooterView.getType());
        // how
        this.manager.updateTableSections();
        // then
        Object expectHeaderModel = this.mProvider.memoryStorage.getRowModel(0);
        Object expectFooterModel = this.mProvider.memoryStorage.getRowModel(1);
        assertThat("The same cell type.", headerViewModel.equals(expectHeaderModel));
        assertThat("The same cell type.", footerViewModel.equals(expectFooterModel));
    }
}
