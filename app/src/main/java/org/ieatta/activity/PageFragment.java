package org.ieatta.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import org.ieatta.IEAApp;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.activity.leadimages.MenuBarEventHandler;
import org.ieatta.tasks.FragmentTask;
import org.ieatta.views.ObservableWebView;
import org.wikipedia.BackPressedHandler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.mapboxsdk.maps.MapView;

import org.ieatta.R;
import org.ieatta.activity.editing.EditHandler;
import org.ieatta.activity.search.SearchBarHideHandler;
import org.ieatta.activity.leadimages.ArticleHeaderView;
import org.ieatta.activity.leadimages.LeadImagesHandler;
import org.wikipedia.analytics.PageScrollFunnel;
import org.wikipedia.analytics.SavedPagesFunnel;
import org.wikipedia.analytics.TabFunnel;
import org.wikipedia.util.DimenUtil;
import org.wikipedia.views.SwipeRefreshLayoutWithScroll;
import org.wikipedia.views.WikiDrawerLayout;

import static butterknife.ButterKnife.findById;

public class PageFragment extends Fragment implements BackPressedHandler {
    private IEAApp app;

    private MapView mapView;

    protected PageLoadStrategy pageLoadStrategy;
    protected PageViewModel model;

//    private HistoryEntry entry;
//    private boolean pushBackStack;
//    private int stagedScrollY;

    protected ObservableWebView webView;

    private static final int REFRESH_SPINNER_ADDITIONAL_OFFSET = (int) (16 * IEAApp.getInstance().getScreenDensity());

    @NonNull
    private TabFunnel tabFunnel = new TabFunnel();

    @Nullable
    private PageScrollFunnel pageScrollFunnel;

    private Tab currentTab = new Tab();

    public Tab getCurrentTab() {
        return this.currentTab;
    }

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
    private MenuBarEventHandler menuBarEventHandler;

    private SwipeRefreshLayoutWithScroll refreshView;
    private WikiDrawerLayout tocDrawer;

    private EditHandler editHandler;

    private SavedPagesFunnel savedPagesFunnel;

    // TODO: don't assume host is PageActivity. Use Fragment callbacks pattern.
    protected PageActivity getPageActivity() {
        return (PageActivity) getActivity();
    }

    @Override
    public boolean onBackPressed() {
//        if (tocHandler != null && tocHandler.isVisible()) {
//            tocHandler.hide();
//            return true;
//        }
//        if (closeFindInPage()) {
//            return true;
//        }
        if (pageLoadStrategy.popBackStack()) {
            return true;
        }
//        if (tabsProvider.onBackPressed()) {
//            return true;
//        }
//        if (tabList.size() > 1) {
//            // if we're at the end of the current tab's backstack, then pop the current tab.
//            tabList.remove(tabList.size() - 1);
//            tabsProvider.invalidate();
//        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (IEAApp) getActivity().getApplicationContext();
        model = new PageViewModel();
        pageLoadStrategy = new DetailPageLoadStrategy();
    }

    @Nullable
    public Page getPage() {
        return model.getPage();
    }

    @Nullable
    public FragmentTask getTask() {
        return this.pageLoadStrategy.getTask();
    }

    public PageTitle getTitle() {
        return model.getTitle();
    }

    public PageTitle getTitleOriginal() {
        return model.getTitleOriginal();
    }

    @NonNull
    private final SwipeRefreshLayout.OnRefreshListener pageRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            PageFragment.this.refreshView.setRefreshing(false);
//            refreshPage();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_page, container, false);
        webView = (ObservableWebView) rootView.findViewById(R.id.recycleView);
        initWebViewListeners();

        tocDrawer = (WikiDrawerLayout) rootView.findViewById(R.id.page_toc_drawer);
        tocDrawer.setDragEdgeWidth(getResources().getDimensionPixelSize(R.dimen.drawer_drag_margin));

        refreshView = (SwipeRefreshLayoutWithScroll) rootView
                .findViewById(R.id.page_refresh_container);
        int swipeOffset = DimenUtil.getContentTopOffsetPx(getActivity()) + REFRESH_SPINNER_ADDITIONAL_OFFSET;
        refreshView.setProgressViewOffset(false, -swipeOffset, swipeOffset);
        // if we want to give it a custom color:
        //refreshView.setProgressBackgroundColor(R.color.swipe_refresh_circle);
        refreshView.setScrollableChild(webView);
        refreshView.setOnRefreshListener(pageRefreshListener);

        return rootView;
    }


    private void initWebViewListeners() {
        webView.addOnUpOrCancelMotionEventListener(new ObservableWebView.OnUpOrCancelMotionEventListener() {
            @Override
            public void onUpOrCancelMotionEvent() {
                // queue the button to be hidden when the user stops scrolling.
//                hideToCButton(true);
                // update our session, since it's possible for the user to remain on the page for
                // a long time, and we wouldn't want the session to time out.
//                app.getSessionFunnel().touchSession();
            }
        });
        webView.setOnFastScrollListener(new ObservableWebView.OnFastScrollListener() {
            @Override
            public void onFastScroll() {
                // show the ToC button...
//                showToCButton();
                // and immediately queue it to be hidden after a short delay, but only if we're
                // not at the top of the page.
                if (webView.getScrollY() > 0) {
//                    hideToCButton(true);
                }
            }
        });
        webView.addOnScrollChangeListener(new ObservableWebView.OnScrollChangeListener() {
            @Override
            public void onScrollChanged(int oldScrollY, int scrollY, boolean isHumanScroll) {
                if (scrollY <= 0) {
                    // always show the ToC button when we're at the top of the page.
//                    showToCButton();
                }
                if (pageScrollFunnel != null) {
                    pageScrollFunnel.onPageScrolled(oldScrollY, scrollY, isHumanScroll);
                }
            }
        });
    }





    private void initPageScrollFunnel() {
        if (model.getPage() != null) {
            pageScrollFunnel = new PageScrollFunnel(app, this.getTask().entry.getSource());
        }
    }

    private void closePageScrollFunnel() {
        if (pageScrollFunnel != null && webView.getContentHeight() > 0) {
            pageScrollFunnel.setViewportHeight(webView.getHeight());
            pageScrollFunnel.setPageHeight(webView.getContentHeight());
            pageScrollFunnel.logDone();
        }
        pageScrollFunnel = null;
    }

    public void loadPage(HistoryEntry entry, boolean pushBackStack, int stagedScrollY, int parallaxScrollY) {
        webView.setVisibility(View.GONE);
        searchBarHideHandler.setForceNoFade(false);
        searchBarHideHandler.setFadeEnabled(false);

        model.setPushBackStack(pushBackStack);
        model.setStagedScrollY(stagedScrollY);
        model.setCurEntry(entry);
        model.setActionbarHeight(this.getActionBarHeight());


//        this.pageRefreshed = pageRefreshed;
//        if (!pageRefreshed) {
//            savedPageCheckComplete = false;
//            checkIfPageIsSaved();
//        }

        closePageScrollFunnel();
        pageLoadStrategy.load(pushBackStack, stagedScrollY, parallaxScrollY);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        editHandler = new EditHandler();
        pageLoadStrategy.setEditHandler(editHandler);

        // TODO: initialize View references in onCreateView().
        articleHeaderView = findById(getView(), R.id.page_header_view);
        leadImagesHandler = new LeadImagesHandler(this, webView, articleHeaderView);
        menuBarEventHandler = new MenuBarEventHandler(leadImagesHandler,articleHeaderView);
        searchBarHideHandler = getPageActivity().getSearchBarHideHandler();
        searchBarHideHandler.setScrollView(webView);

        pageLoadStrategy.setUp(model, this, refreshView, webView, searchBarHideHandler,
                leadImagesHandler, getCurrentTab().getBackStack());

        mapView = articleHeaderView.getMapView();
        mapView.onCreate(savedInstanceState);
    }

    public int getActionBarHeight() {
        return searchBarHideHandler.getActionBarHeight();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        initPageScrollFunnel();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        closePageScrollFunnel();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    public MenuBarEventHandler getMenuBarEventHandler() {
        return menuBarEventHandler;
    }
}
