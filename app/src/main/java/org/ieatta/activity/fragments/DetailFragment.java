package org.ieatta.activity.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ieatta.IEAApp;
import org.ieatta.R;
import org.ieatta.activity.DetailPageLoadStrategy;
import org.ieatta.activity.Page;
import org.ieatta.activity.PageLoadStrategy;
import org.ieatta.activity.PageTitle;
import org.ieatta.activity.PageViewModel;
import org.ieatta.activity.gallery.GalleryActivity;
import org.ieatta.activity.gallery.GalleryThumbnailScrollView;
import org.ieatta.activity.leadimages.LeadImagesHandler;
import org.ieatta.analytics.GalleryFunnel;
import org.wikipedia.BackPressedHandler;

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

public abstract class DetailFragment extends PageFragment implements BackPressedHandler, LeadImagesHandler.OnContentHeightChangedListener {

    private IEAApp app;
    protected PageViewModel model;
    protected PageLoadStrategy pageLoadStrategy;


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
    protected ObservableWebView webView;
    private SwipeRefreshLayoutWithScroll refreshView;
    private WikiDrawerLayout tocDrawer;

    private EditHandler editHandler;

    private SavedPagesFunnel savedPagesFunnel;

    @Nullable
    public Page getPage() {
        return model.getPage();
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
//            refreshPage();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (IEAApp) getActivity().getApplicationContext();
        model = new PageViewModel();
        pageLoadStrategy = new DetailPageLoadStrategy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
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

        // TODO: initialize View references in onCreateView().
        articleHeaderView = (ArticleHeaderView) rootView.findViewById(R.id.page_header_view);


        return rootView;
    }

    protected void reloadPage() {
        searchBarHideHandler = getPageActivity().getSearchBarHideHandler();
        searchBarHideHandler.setScrollView(webView);
        leadImagesHandler = new LeadImagesHandler(this, webView, articleHeaderView);
        leadImagesHandler.addOnContentHeightChangedListener(this);
        pageLoadStrategy.setUp(this, refreshView, webView, searchBarHideHandler,
                leadImagesHandler, new LinkedList<PageBackStackItem>());
        pageLoadStrategy.onLeadSectionLoaded(0);
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

    protected int getScreenHeight(){
        return DimenUtil.getScreenHeight(getActivity().getWindow());
    }

    protected GalleryThumbnailScrollView.GalleryViewListener galleryViewListener
            = new GalleryThumbnailScrollView.GalleryViewListener() {
        @Override
        public void onGalleryItemClicked(String imageUUID) {
            PageTitle imageTitle = new PageTitle(imageUUID, "");
            GalleryActivity.showGallery(getActivity(), model.getTitle(), imageTitle,
                    GalleryFunnel.SOURCE_LINK_PREVIEW);
        }
    };
}
