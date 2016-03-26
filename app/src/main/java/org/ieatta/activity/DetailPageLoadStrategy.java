package org.ieatta.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import org.ieatta.IEAApp;
import org.ieatta.activity.editing.EditHandler;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.activity.search.SearchBarHideHandler;
import org.ieatta.activity.leadimages.LeadImagesHandler;
import org.ieatta.provide.MainSegueIdentifier;
import org.ieatta.tasks.FragmentTask;
import org.ieatta.views.ObservableWebView;
import org.wikipedia.util.log.L;
import org.wikipedia.views.SwipeRefreshLayoutWithScroll;

import java.util.ArrayList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * Our old page load strategy, which uses the JSON MW API directly and loads a page in multiple steps:
 * First it loads the lead section (sections=0).
 * Then it loads the remaining sections (sections=1-).
 * <p/>
 * This class tracks:
 * - the states the page loading goes through,
 * - a backstack of pages and page positions visited,
 * - and many handlers.
 */
public class DetailPageLoadStrategy implements PageLoadStrategy {
    protected FragmentTask fragmentTask;

    private interface ErrorCallback {
        void call(@Nullable Throwable error);
    }

    private static final String BRIDGE_PAYLOAD_SAVED_PAGE = "savedPage";

    /**
     * List of lightweight history items to serve as the backstack for this fragment.
     * Since the list consists of Parcelable objects, it can be saved and restored from the
     * savedInstanceState of the fragment.
     */
    @NonNull
    private List<PageBackStackItem> backStack = new ArrayList<>();

    @NonNull
    private final SequenceNumber sequenceNumber = new SequenceNumber();

    /**
     * The y-offset position to which the page will be scrolled once it's fully loaded
     * (or loaded to the point where it can be scrolled to the correct position).
     */
    private int stagedScrollY;
    private int sectionTargetFromIntent;
    private String sectionTargetFromTitle;

    /**
     * Whether to write the page contents to cache as soon as it's loaded.
     */
    private boolean cacheOnComplete = true;

    private ErrorCallback networkErrorCallback;

    // copied fields
    private PageViewModel model;
    private PageFragment fragment;
    private PageActivity activity;
    private ObservableWebView webView;
    private SwipeRefreshLayoutWithScroll refreshView;
    @NonNull
    private final IEAApp app = IEAApp.getInstance();
    private LeadImagesHandler leadImagesHandler;
    private SearchBarHideHandler searchBarHideHandler;
    private EditHandler editHandler;

    @Override
    @SuppressWarnings("checkstyle:parameternumber")
    public void setUp(@NonNull PageViewModel model,
                      @NonNull PageFragment fragment,
                      @NonNull SwipeRefreshLayoutWithScroll refreshView,
                      @NonNull ObservableWebView webView,
                      @NonNull SearchBarHideHandler searchBarHideHandler,
                      @NonNull LeadImagesHandler leadImagesHandler,
                      @NonNull List<PageBackStackItem> backStack) {
        this.model = model;
        this.fragment = fragment;
        activity = (PageActivity) fragment.getActivity();
        this.refreshView = refreshView;
        this.webView = webView;
        this.searchBarHideHandler = searchBarHideHandler;
        this.leadImagesHandler = leadImagesHandler;

        this.backStack = backStack;

        // if we already have pages in the backstack (whether it's from savedInstanceState, or
        // from being stored in the activity's fragment backstack), then load the topmost page
        // on the backstack.
        loadFromBackStack();
    }

    @Override
    public void load(boolean pushBackStack, int stagedScrollY, int parallaxScrollY) {
        if (pushBackStack) {
            // update the topmost entry in the backstack, before we start overwriting things.
            updateCurrentBackStackItem();
            pushBackStack();
        }

        // increment our sequence number, so that any async tasks that depend on the sequence
        // will invalidate themselves upon completion.
        sequenceNumber.increase();

        // If this is a refresh, don't clear the webview contents
        this.stagedScrollY = stagedScrollY;

//        fragment.updatePageInfo(null);
//        fragment.setPageSaved(false);
        leadImagesHandler.updateNavigate(null);

        // kick off an event to the WebView that will cause it to clear its contents,
        // and then report back to us when the clearing is complete, so that we can synchronize
        // the transitions of our native components to the new page content.
        // The callback event from the WebView will then call the loadOnWebViewReady()
        // function, which will continue the loading process.
        leadImagesHandler.hide();
//        bottomContentHandler.hide();
        activity.getSearchBarHideHandler().setFadeEnabled(false);

        // Setup new page.
        this.setupCurrentTask(backStack.get(backStack.size() - 1).getHistoryEntry());
    }

    private void restoreLastScrollY(int stagedScrollY) {
        this.webView.scrollToLastTop(stagedScrollY);
    }

    @Override
    public void loadFromBackStack() {
        if (backStack.isEmpty()) {
            return;
        }
        PageBackStackItem item = backStack.get(backStack.size() - 1);
        // display the page based on the backstack item, stage the scrollY position based on
        // the backstack item.
        fragment.loadPage(item.getHistoryEntry(), false, item.getScrollY(), item.getParallaxScrollY());

        L.d("Loaded page " + item.getHistoryEntry().getSource() + " from backstack");
    }

    @Override
    public void updateCurrentBackStackItem() {
        if (backStack.isEmpty()) {
            return;
        }
        PageBackStackItem item = backStack.get(backStack.size() - 1);
        item.setScrollY(webView.getLastTop());
        item.setParallaxScrollY(leadImagesHandler.getParallaxScrollY());
    }

    @Override
    public void setBackStack(@NonNull List<PageBackStackItem> backStack) {
        this.backStack = backStack;
    }

    public boolean popBackStack() {
        if (!backStack.isEmpty()) {
            backStack.remove(backStack.size() - 1);
        }

        if (!backStack.isEmpty()) {
            loadFromBackStack();
            return true;
        }

        return false;
    }

    @Override
    public void onHidePageContent() {

    }

    @Override
    public void setEditHandler(EditHandler editHandler) {
        this.editHandler = editHandler;
    }

    @Override
    public void backFromEditing(Intent data) {
        //Retrieve section ID from intent, and find correct section, so where know where to scroll to
//        sectionTargetFromIntent = data.getIntExtra(EditSectionActivity.EXTRA_SECTION_ID, 0);
        //reset our scroll offset, since we have a section scroll target
        stagedScrollY = 0;
    }

    @Override
    public void layoutLeadImage() {
        leadImagesHandler.beginLayout(new LeadImagesHandler.OnLeadImageLayoutListener() {
            @Override
            public void onLayoutComplete(int sequence) {
                if (fragment.isAdded()) {
                    searchBarHideHandler.setFadeEnabled(leadImagesHandler.isLeadImageEnabled());
                }
            }
        }, sequenceNumber.get());
    }

    private void layoutLeadImage(@Nullable Runnable runnable) {
        leadImagesHandler.beginLayout(new LeadImageLayoutListener(runnable), sequenceNumber.get());
    }


//    private void updateThumbnail(String thumbUrl) {
//        model.getTitle().setThumbUrl(thumbUrl);
//        model.getTitleOriginal().setThumbUrl(thumbUrl);
//        fragment.invalidateTabs();
//    }

    private boolean isFirstPage() {
        return backStack.size() <= 1;
//                && !webView.canGoBack();
    }

    /**
     * Push the current page title onto the backstack.
     */
    private void pushBackStack() {
        PageBackStackItem item = new PageBackStackItem(model.getCurEntry());
        backStack.add(item);
    }

//    private boolean isPageEditable(Page page) {
//        return (app.getUserInfoStorage().isLoggedIn() || !isAnonEditingDisabled())
//                && !page.isFilePage()
//                && !page.isMainPage();
//    }

    private float getDimension(@DimenRes int id) {
        return getResources().getDimension(id);
    }

    private Resources getResources() {
        return activity.getResources();
    }

    /**
     * Monotonically increasing sequence number to maintain synchronization when loading page
     * content asynchronously between the Java and JavaScript layers, as well as between synchronous
     * methods and asynchronous callbacks on the UI thread.
     */
    private static class SequenceNumber {
        private int sequence;

        void increase() {
            ++sequence;
        }

        int get() {
            return sequence;
        }

        boolean inSync(int sequence) {
            return this.sequence == sequence;
        }
    }


    @Override
    public FragmentTask getTask() {
        return this.fragmentTask;
    }

    public void onLeadSectionLoaded(int startSequenceNum) {
        if (!fragment.isAdded()) {
            return;
        }

        layoutLeadImage(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    private class LeadImageLayoutListener implements LeadImagesHandler.OnLeadImageLayoutListener {
        @Nullable
        private final Runnable runnable;

        LeadImageLayoutListener(@Nullable Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void onLayoutComplete(int sequence) {
            if (!fragment.isAdded() || !sequenceNumber.inSync(sequence)) {
                return;
            }
            searchBarHideHandler.setFadeEnabled(leadImagesHandler.isLeadImageEnabled());

            if (runnable != null) {
                // when the lead image is laid out, load the lead section and the rest
                // of the sections into the webview.
                displayLeadSection();
                runnable.run();
            }
        }
    }

    private void displayLeadSection() {
        Page page = model.getPage();

        if (webView.getVisibility() != View.VISIBLE) {
            webView.setVisibility(View.VISIBLE);
        }

        refreshView.setRefreshing(false);
//        activity.updateProgressBar(true, true, 0);

//        leadImagesHandler.updateNavigate(page.getPageProperties().getGeo());
    }

    private void setupCurrentTask(HistoryEntry entry) {
        fragmentTask = MainSegueIdentifier.getFragment(entry, this.fragment.getActivity(), this.model);

        fragmentTask.setupWebView(webView);
        fragmentTask.prepareUI();

        fragmentTask.executeTask().onSuccessTask(new Continuation<Void, Task<Void>>() {
            @Override
            public Task<Void> then(Task<Void> task) throws Exception {
                DetailPageLoadStrategy.this.postLoadPage();
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    public void postLoadPage() {
        fragmentTask.postUI();

        webView.setVisibility(View.VISIBLE);
        fragmentTask.manager.reloadTableView();

        this.restoreLastScrollY(model.getStagedScrollY());
        this.onLeadSectionLoaded(0);
    }

}
