package org.ieatta.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import org.ieatta.IEAApp;
import org.ieatta.activity.editing.EditHandler;
import org.ieatta.activity.fragments.DetailFragment;
import org.ieatta.activity.fragments.PageFragment;
import org.ieatta.activity.fragments.search.SearchBarHideHandler;
import org.ieatta.activity.leadimages.LeadImagesHandler;
import org.ieatta.views.ObservableWebView;
import org.wikipedia.util.log.L;
import org.wikipedia.views.SwipeRefreshLayoutWithScroll;

import java.util.ArrayList;
import java.util.List;

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
    private interface ErrorCallback {
        void call(@Nullable Throwable error);
    }

    private static final String BRIDGE_PAYLOAD_SAVED_PAGE = "savedPage";

    private static final int STATE_NO_FETCH = 1;
    private static final int STATE_INITIAL_FETCH = 2;
    private static final int STATE_COMPLETE_FETCH = 3;

    private int state = STATE_NO_FETCH;

    /**
     * List of lightweight history items to serve as the backstack for this fragment.
     * Since the list consists of Parcelable objects, it can be saved and restored from the
     * savedInstanceState of the fragment.
     */
    @NonNull private List<PageBackStackItem> backStack = new ArrayList<>();

    @NonNull private final SequenceNumber sequenceNumber = new SequenceNumber();

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
    @NonNull private final IEAApp app = IEAApp.getInstance();
    private LeadImagesHandler leadImagesHandler;
    private SearchBarHideHandler searchBarHideHandler;
    private EditHandler editHandler;

    @Override
    @SuppressWarnings("checkstyle:parameternumber")
    public void setUp(@NonNull PageViewModel model,
            @NonNull DetailFragment fragment,
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
    public void load(boolean pushBackStack, @NonNull Cache cachePreference, int stagedScrollY) {
        if (pushBackStack) {
            // update the topmost entry in the backstack, before we start overwriting things.
            updateCurrentBackStackItem();
            pushBackStack();
        }

        state = STATE_NO_FETCH;

        // increment our sequence number, so that any async tasks that depend on the sequence
        // will invalidate themselves upon completion.
        sequenceNumber.increase();

//            fragment.updatePageInfo(null);
//            fragment.setPageSaved(false);
//            leadImagesHandler.updateNavigate(null);
    }

    @Override
    public boolean isLoading() {
        return state != STATE_COMPLETE_FETCH;
    }

    @Override
    public void loadFromBackStack() {
        if (backStack.isEmpty()) {
            return;
        }
        PageBackStackItem item = backStack.get(backStack.size() - 1);
        // display the page based on the backstack item, stage the scrollY position based on
        // the backstack item.
//        fragment.loadPage(item.getTitle(), item.getHistoryEntry(), Cache.PREFERRED, false,
//                item.getScrollY());
//        L.d("Loaded page " + item.getTitle().getDisplayText() + " from backstack");
    }

    @Override
    public void updateCurrentBackStackItem() {
        if (backStack.isEmpty()) {
            return;
        }
        PageBackStackItem item = backStack.get(backStack.size() - 1);
        item.setScrollY(webView.getScrollY());
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

    @VisibleForTesting
    protected void commonSectionFetchOnCatch(Throwable caught, int startSequenceNum) {
        ErrorCallback callback = networkErrorCallback;
        networkErrorCallback = null;
        cacheOnComplete = false;
        state = STATE_COMPLETE_FETCH;
        activity.supportInvalidateOptionsMenu();
        if (!sequenceNumber.inSync(startSequenceNum)) {
            return;
        }
        if (callback != null) {
            callback.call(caught);
        }
    }

//    private void updateThumbnail(String thumbUrl) {
//        model.getTitle().setThumbUrl(thumbUrl);
//        model.getTitleOriginal().setThumbUrl(thumbUrl);
//        fragment.invalidateTabs();
//    }

    private boolean isFirstPage() {
        return backStack.size() <= 1 ;
//                && !webView.canGoBack();
    }

    /**
     * Push the current page title onto the backstack.
     */
    private void pushBackStack() {
        PageBackStackItem item = new PageBackStackItem( model.getCurEntry());
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


    public void onLeadSectionLoaded(int startSequenceNum) {
        if (!fragment.isAdded() || !sequenceNumber.inSync(startSequenceNum)) {
            return;
        }

        layoutLeadImage(new Runnable() {
            @Override
            public void run() {
                setState(STATE_INITIAL_FETCH);
                performActionForState(state);
            }
        });

        // Fetch larger thumbnail URL from the network, and save it to our DB.
//        (new PageImagesTask(app.getAPIForSite(model.getTitle().getSite()), model.getTitle().getSite(),
//                Arrays.asList(new PageTitle[]{model.getTitle()}), WikipediaApp.PREFERRED_THUMB_SIZE) {
//            @Override
//            public void onFinish(Map<PageTitle, String> result) {
//                if (result.containsKey(model.getTitle())) {
//                    PageImage pi = new PageImage(model.getTitle(), result.get(model.getTitle()));
//                    app.getDatabaseClient(PageImage.class).upsert(pi, PageImageDatabaseTable.SELECTION_KEYS);
//                    updateThumbnail(result.get(model.getTitle()));
//                }
//            }
//
//            @Override
//            public void onCatch(Throwable caught) {
//                L.w(caught);
//            }
//        }).execute();
    }


    private void setState(int state) {
        if (!fragment.isAdded()) {
            return;
        }
        this.state = state;
        activity.supportInvalidateOptionsMenu();

        // FIXME: Move this out into a PageComplete event of sorts
        if (state == STATE_COMPLETE_FETCH) {
//            fragment.setupToC(model, isFirstPage());
//
//            //add the page to cache!
//            if (cacheOnComplete) {
//                app.getPageCache().put(model.getTitleOriginal(), model.getPage(),
//                        new PageCache.CachePutListener() {
//                            @Override
//                            public void onPutComplete() {
//                            }
//
//                            @Override
//                            public void onPutError(Throwable e) {
//                                L.e("Failed to add page to cache.", e);
//                            }
//                        });
//            }
        }
    }


    private void performActionForState(int forState) {
        if (!fragment.isAdded()) {
            return;
        }
        switch (forState) {
            case STATE_NO_FETCH:
//                activity.updateProgressBar(true, true, 0);
//                loadLeadSection(sequenceNumber.get());
                break;
            case STATE_INITIAL_FETCH:
//                loadRemainingSections(sequenceNumber.get());
                break;
            case STATE_COMPLETE_FETCH:
//                editHandler.setPage(model.getPage());
//                layoutLeadImage(new Runnable() {
//                    @Override
//                    public void run() {
//                        displayNonLeadSectionForUnsavedPage(1);
//                    }
//                });
                break;
            default:
                throw new RuntimeException("Unknown state encountered " + state);
        }
    }


    private class LeadImageLayoutListener implements LeadImagesHandler.OnLeadImageLayoutListener {
        @Nullable private final Runnable runnable;

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
//        Page page = model.getPage();
//
//        sendMarginPayload();
//
//        sendLeadSectionPayload(page);
//
//        sendMiscPayload(page);
//
//        if (webView.getVisibility() != View.VISIBLE) {
//            webView.setVisibility(View.VISIBLE);
//        }
//
//        refreshView.setRefreshing(false);
//        activity.updateProgressBar(true, true, 0);
//
//        leadImagesHandler.updateNavigate(page.getPageProperties().getGeo());
    }
}
