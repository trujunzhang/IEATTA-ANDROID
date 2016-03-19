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
import org.ieatta.cells.header.IEARestaurantDetailHeaderCell;
import org.ieatta.cells.model.IEARestaurantDetailHeader;
import org.ieatta.cells.model.SectionTitleCellModel;
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

import bolts.Continuation;
import bolts.Task;

public class RestaurantDetailFragment extends DetailFragment {

    public static final RecyclerOnItemClickListener itemClickListener = new RecyclerOnItemClickListener() {
        @Override
        public void onItemClick(View view, NSIndexPath indexPath, Object model, int position, boolean isLongClick) {

        }
    };

    enum RestaurantDetailSection {
        section_header,//= 0
        section_google_mapaddress,//= 1
        section_events,//= 2
        section_photogallery,//= 3
        section_reviews,//= 4
    }

    private RecycleViewManager manager;

    private RestaurantDetailTask task = new RestaurantDetailTask();

    public static final int TOC_ACTION_SHOW = 0;
    public static final int TOC_ACTION_HIDE = 1;
    public static final int TOC_ACTION_TOGGLE = 2;

    private boolean pageSaved;
    private boolean pageRefreshed;
    private boolean savedPageCheckComplete;
    private boolean errorState = false;

    private static final int TOC_BUTTON_HIDE_DELAY = 2000;
    private static final int REFRESH_SPINNER_ADDITIONAL_OFFSET = (int) (16 * IEAApp.getInstance().getScreenDensity());


    @NonNull
    private TabFunnel tabFunnel = new TabFunnel();

    @Nullable
    private PageScrollFunnel pageScrollFunnel;

    /**
     * Whether to save the full page content as soon as it's loaded.
     * Used in the following cases:
     * - Stored page content is corrupted
     * - Page bookmarks are imported from the old app.
     * In the above cases, loading of the saved page will "fail", and will
     * automatically bounce to the online version of the page. Once the online page
     * loads successfully, the content will be saved, thereby reconstructing the
     * stored version of the page.
     */
    private boolean saveOnComplete = false;

    private ArticleHeaderView articleHeaderView;
    private LeadImagesHandler leadImagesHandler;
    private SearchBarHideHandler searchBarHideHandler;
    private ObservableWebView webView;
    private SwipeRefreshLayoutWithScroll refreshView;
        private WikiDrawerLayout tocDrawer;

    private EditHandler editHandler;

    private SavedPagesFunnel savedPagesFunnel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        webView = (ObservableWebView) rootView.findViewById(R.id.recycleView);


        tocDrawer = (WikiDrawerLayout) rootView.findViewById(R.id.page_toc_drawer);
        tocDrawer.setDragEdgeWidth(getResources().getDimensionPixelSize(R.dimen.drawer_drag_margin));

        refreshView = (SwipeRefreshLayoutWithScroll) rootView
                .findViewById(R.id.page_refresh_container);
        int swipeOffset = DimenUtil.getContentTopOffsetPx(getActivity()) + REFRESH_SPINNER_ADDITIONAL_OFFSET;
        refreshView.setProgressViewOffset(false, -swipeOffset, swipeOffset);
        // if we want to give it a custom color:
        //refreshView.setProgressBackgroundColor(R.color.swipe_refresh_circle);
        refreshView.setScrollableChild(webView);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = new RecycleViewManager(this.getActivity().getApplicationContext());
    }

    private void setupUI() {
        this.manager.setRegisterCellClass(IEARestaurantDetailHeaderCell.getType(), RestaurantDetailSection.section_header.ordinal());
        this.manager.setRegisterCellClass(IEARestaurantEventsCell.getType(), RestaurantDetailSection.section_events.ordinal());

        this.manager.appendSectionTitleCell(new SectionTitleCellModel(IEAEditKey.Section_Title, R.string.Events_Recorded), RestaurantDetailSection.section_events.ordinal());
    }

    @Override
    public void loadPage() {
        manager.startManagingWithDelegate(webView);
        manager.setOnItemClickListener(itemClickListener);

        this.setupUI();

        String uuid = "1CE562A4-A978-4B75-9B7B-2F3CF9F42A04";
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

    private void reloadPage() {
//        this.manager.setSectionItems(CollectionUtil.createList(new IEARestaurantDetailHeader(this.task.restaurant)), RestaurantDetailSection.section_header.ordinal());
//        this.manager.showGoogleMapAddress(RestaurantDetailSection.section_google_mapaddress.ordinal());

        model.setPage(task.getPage());
//        model.setTitle(task.);
        searchBarHideHandler = getPageActivity().getSearchBarHideHandler();
        searchBarHideHandler.setScrollView(webView);
        leadImagesHandler = new LeadImagesHandler(this,  webView, articleHeaderView);
        pageLoadStrategy.setUp(this, refreshView, webView, searchBarHideHandler,
                leadImagesHandler, new LinkedList<PageBackStackItem>());
        pageLoadStrategy.onLeadSectionLoaded(0);
    }


    @Override
    public void onPause() {
        super.onPause();
        closePageScrollFunnel();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        initPageScrollFunnel();
    }


    private void initPageScrollFunnel() {
//        if (model.getPage() != null) {
//            pageScrollFunnel = new PageScrollFunnel(app, model.getPage().getPageProperties().getPageId());
//        }
    }

    private void closePageScrollFunnel() {
//        if (pageScrollFunnel != null && webView.getContentHeight() > 0) {
//            pageScrollFunnel.setViewportHeight(webView.getHeight());
//            pageScrollFunnel.setPageHeight(webView.getContentHeight());
//            pageScrollFunnel.logDone();
//        }
        pageScrollFunnel = null;
    }

    // TODO: don't assume host is PageActivity. Use Fragment callbacks pattern.
    private PageActivity getPageActivity() {
        return (PageActivity) getActivity();
    }


}
