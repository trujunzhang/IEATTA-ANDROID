package org.ieatta.activity.fragments;

import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tableview.RecycleViewManager;
import com.tableview.adapter.NSIndexPath;
import com.tableview.adapter.RecyclerOnItemClickListener;
import com.tableview.utils.CollectionUtil;

import org.ieatta.IEAApp;
import org.ieatta.R;
import org.ieatta.activity.DetailPageLoadStrategy;
import org.ieatta.activity.JsonPageLoadStrategy;
import org.ieatta.activity.Page;
import org.ieatta.activity.PageActivity;
import org.ieatta.activity.PageBackStackItem;
import org.ieatta.activity.PageLoadStrategy;
import org.ieatta.activity.PageViewModel;
import org.ieatta.activity.editing.EditHandler;
import org.ieatta.activity.fragments.search.SearchBarHideHandler;
import org.ieatta.activity.leadimages.ArticleHeaderView;
import org.ieatta.activity.leadimages.LeadImagesHandler;
import org.ieatta.cells.IEARestaurantEventsCell;
import org.ieatta.cells.header.IEAEmptyHeaderCell;
import org.ieatta.cells.header.IEARestaurantDetailHeaderCell;
import org.ieatta.cells.model.IEAEmptyHeader;
import org.ieatta.cells.model.IEARestaurantDetailHeader;
import org.ieatta.cells.model.SectionTitleCellModel;
import org.ieatta.database.models.DBEvent;
import org.ieatta.provide.IEAEditKey;
import org.ieatta.tasks.RestaurantDetailTask;
import org.ieatta.views.ObservableWebView;
import org.wikipedia.analytics.PageScrollFunnel;
import org.wikipedia.analytics.SavedPagesFunnel;
import org.wikipedia.analytics.TabFunnel;
import org.wikipedia.util.DimenUtil;
import org.wikipedia.views.SwipeRefreshLayoutWithScroll;
import org.wikipedia.views.WikiDrawerLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

import static butterknife.ButterKnife.findById;

public class RestaurantDetailFragment extends DetailFragment {

    public static final RecyclerOnItemClickListener itemClickListener = new RecyclerOnItemClickListener() {
        @Override
        public void onItemClick(View view, NSIndexPath indexPath, Object model, int position, boolean isLongClick) {

        }
    };

    @Override
    public void onContentHeightChanged(int contentHeight) {
        this.manager.updateSectionItem(new IEAEmptyHeader(contentHeight), RestaurantDetailSection.section_leadimage.ordinal(),0);
    }

    enum RestaurantDetailSection {
        section_leadimage,
        section_events,//= 0
        section_photogallery,//= 1
        section_reviews,//= 2
    }

    private RecycleViewManager manager;

    private RestaurantDetailTask task = new RestaurantDetailTask();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = new RecycleViewManager(this.getActivity().getApplicationContext());
    }

    private void setupUI() {
        this.manager.setRegisterCellClass(IEAEmptyHeaderCell.getType(), RestaurantDetailSection.section_leadimage.ordinal());
        this.manager.setRegisterCellClass(IEARestaurantEventsCell.getType(), RestaurantDetailSection.section_events.ordinal());

        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Events_Recorded), RestaurantDetailSection.section_events.ordinal());
    }

    @Override
    public void loadPage() {
        manager.startManagingWithDelegate(webView);
        manager.setOnItemClickListener(itemClickListener);

        this.setupUI();

        // String uuid = "1CE562A4-A978-4B75-9B7B-2F3CF9F42A04"; // The Flying Falafel
        String uuid = "33ED9F31-F6A5-43A4-8D11-8E511CA0BD39"; // The Spice Jar
        task.executeTask(uuid).onSuccess(new Continuation<Void, Object>() {
            @Override
            public Object then(Task<Void> task) throws Exception {
                RestaurantDetailFragment.this.reloadPage();
                return null;
            }
        }).continueWith(new Continuation<Object, Object>() {
            @Override
            public Object then(Task<Object> task) throws Exception {
                return null;
            }
        });
    }

    @Override
    protected void reloadPage() {
        this.manager.setSectionItems(CollectionUtil.createList(new IEAEmptyHeader(this.getScreenHeight())), RestaurantDetailSection.section_leadimage.ordinal());
        this.manager.setSectionItems(task.events, RestaurantDetailSection.section_events.ordinal());

        model.setPage(task.getPage());

        super.reloadPage();
    }

}
