package org.ieatta.activity;

import android.os.Bundle;

import android.os.Looper;
import android.support.v4.view.GravityCompat;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.squareup.otto.Bus;

import org.ieatta.IEAApp;
import org.ieatta.R;
import org.ieatta.activity.fragments.PageFragment;
import org.ieatta.activity.fragments.search.SearchArticlesFragment;
import org.ieatta.activity.fragments.search.SearchBarHideHandler;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.activity.settings.SettingsActivity;
import org.ieatta.provide.MainSegueIdentifier;
import org.ieatta.utils.LocationUtil;
import org.wikipedia.activity.ThemedActionBarActivity;


import org.wikipedia.BackPressedHandler;
import org.wikipedia.activity.ActivityUtil;
import org.wikipedia.ViewAnimations;
import org.wikipedia.settings.Prefs;
import org.wikipedia.util.ApiUtil;
import org.wikipedia.util.DeviceUtil;
import org.wikipedia.util.log.L;
import org.wikipedia.views.WikiDrawerLayout;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import java.util.LinkedList;
import java.util.List;

public class PageActivity extends ThemedActionBarActivity {

    public enum TabPosition {
        CURRENT_TAB,
        NEW_TAB_BACKGROUND,
        NEW_TAB_FOREGROUND
    }

    public static final int ACTIVITY_REQUEST_LANGLINKS = 0;
    public static final int ACTIVITY_REQUEST_EDIT_SECTION = 1;
    public static final int ACTIVITY_REQUEST_GALLERY = 2;

    public static final int PROGRESS_BAR_MAX_VALUE = 10000;

    public static final String ACTION_PAGE_FOR_TITLE = "org.wikipedia.page_for_title";
    public static final String EXTRA_PAGETITLE = "org.wikipedia.pagetitle";
    public static final String EXTRA_HISTORYENTRY = "org.wikipedia.history.historyentry";
    public static final String EXTRA_SEARCH_FROM_WIDGET = "searchFromWidget";
    public static final String EXTRA_FEATURED_ARTICLE_FROM_WIDGET = "featuredArticleFromWidget";

    private static final String ZERO_ON_NOTICE_PRESENTED = "org.wikipedia.zero.zeroOnNoticePresented";
    private static final String LANGUAGE_CODE_BUNDLE_KEY = "language";
    private static final String PLAIN_TEXT_MIME_TYPE = "text/plain";
    private static final String LINK_PREVIEW_FRAGMENT_TAG = "link_preview_dialog";

    private Bus bus;
    private EventBusMethods busMethods;
    private IEAApp app;
    private View fragmentContainerView;
    private WikiDrawerLayout drawerLayout;
    private Menu navMenu;
    private SearchArticlesFragment searchFragment;
    private TextView searchHintText;
    private View toolbarContainer;
    private ActionBarDrawerToggle mDrawerToggle;
    private SearchBarHideHandler searchBarHideHandler;
    private boolean isZeroEnabled;
    private NavDrawerHelper navDrawerHelper;
    private boolean navItemSelected;

    public View getContentView() {
        return fragmentContainerView;
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }

    public SearchBarHideHandler getSearchBarHideHandler() {
        return searchBarHideHandler;
    }

    public Menu getNavMenu() {
        return navMenu;
    }

    public List<HistoryEntry> pages = new LinkedList<>();

    /**
     * Get the Fragment that is currently at the top of the Activity's backstack.
     * This activity's fragment container will hold multiple fragments stacked onto
     * each other using FragmentManager, and this function will return the current
     * topmost Fragment. It's up to the caller to cast the result to a more specific
     * fragment class, and perform actions on it.
     *
     * @return Fragment at the top of the backstack.
     */
    public Fragment getTopFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.content_fragment_container);
    }

    /**
     * Get the PageViewFragment that is currently at the top of the Activity's backstack.
     * If the current topmost fragment is not a PageViewFragment, return null.
     *
     * @return The PageViewFragment at the top of the backstack, or null if the current
     * top fragment is not a PageViewFragment.
     */
    @Nullable
    public PageFragment getCurPageFragment() {
        Fragment f = getTopFragment();
        if (f instanceof PageFragment) {
            return (PageFragment) f;
        } else {
            return null;
        }
    }

    public void setNavItemSelected(boolean wasSelected) {
        navItemSelected = wasSelected;
    }

    private boolean wasNavItemSelected() {
        return navItemSelected;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (IEAApp) getApplicationContext();
//        app.checkCrashes(this);

        setContentView(R.layout.activity_page);

        setSupportActionBar((Toolbar) findViewById(R.id.main_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarContainer = findViewById(R.id.main_toolbar_container);

        busMethods = new EventBusMethods();
        registerBus();

        fragmentContainerView = findViewById(R.id.content_fragment_container);

        drawerLayout = (WikiDrawerLayout) findViewById(R.id.drawer_layout);
        if (!ApiUtil.hasLollipop()) {
            drawerLayout.setDrawerShadow(R.drawable.nav_drawer_shadow, GravityCompat.START);
        }
        NavigationView navDrawer = (NavigationView) findViewById(R.id.navdrawer);
        navMenu = navDrawer.getMenu();
        navDrawerHelper = new NavDrawerHelper(this, navDrawer.getHeaderView(0));
        navDrawer.setNavigationItemSelectedListener(navDrawerHelper.getNewListener());

        searchFragment = (SearchArticlesFragment) getSupportFragmentManager().findFragmentById(R.id.search_fragment);
        searchHintText = (TextView) findViewById(R.id.main_search_bar_text);
        searchHintText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFragment.openSearch();
            }
        });

        mDrawerToggle = new MainDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,          /* DrawerLayout object */
                R.string.app_name,     /* "open drawer" description */
                R.string.app_name      /* "close drawer" description */
        );

        // Set the drawer toggle as the DrawerListener
        drawerLayout.setDrawerListener(mDrawerToggle);
        drawerLayout.setDragEdgeWidth(
                getResources().getDimensionPixelSize(R.dimen.drawer_drag_margin));
        getSupportActionBar().setTitle("");

        searchBarHideHandler = new SearchBarHideHandler(this, toolbarContainer);

        if (savedInstanceState != null) {
            isZeroEnabled = savedInstanceState.getBoolean("pausedZeroEnabledState");
            if (savedInstanceState.getBoolean("isSearching")) {
                searchFragment.openSearch();
            }
            String language = savedInstanceState.getString(LANGUAGE_CODE_BUNDLE_KEY);

            // Note: when system language is enabled, and the system language is changed outside of
            // the app, MRU languages are not updated. There's no harm in doing that here but since
            // the user didin't choose that language in app, it may be unexpected.
        }
//        searchHintText.setText(getString(isZeroEnabled ? R.string.zero_search_hint : R.string.search_hint));


        if (savedInstanceState == null) {
            // if there's no savedInstanceState, and we're not coming back from a Theme change,
            // then we must have been launched with an Intent, so... handle it!
            handleIntent(getIntent());
        }
        loadMainPageIfNoTabs();

        // Conditionally execute all recurring tasks
//        new RecurringTasksExecutor(app).run();
    }

    private class MainDrawerToggle extends ActionBarDrawerToggle {
        private boolean oncePerSlideLock = false;

        MainDrawerToggle(android.app.Activity activity,
                         android.support.v4.widget.DrawerLayout drawerLayout,
                         int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            // if we want to change the title upon closing:
            //getSupportActionBar().setTitle("");
            if (!wasNavItemSelected()) {
                navDrawerHelper.getFunnel().logCancel();
            }
            setNavItemSelected(false);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            // if we want to change the title upon opening:
            //getSupportActionBar().setTitle("");
            // If we're in the search state, then get out of it.
            if (isSearching()) {
                searchFragment.closeSearch();
            }
            // also make sure we're not inside an action mode
//            if (isCabOpen()) {
//                finishActionMode();
//            }
            updateNavDrawerSelection(getTopFragment());
            navDrawerHelper.getFunnel().logOpen();
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            super.onDrawerSlide(drawerView, 0);
            if (!oncePerSlideLock) {
                // Hide the keyboard when the drawer is opened
                DeviceUtil.hideSoftKeyboard(PageActivity.this);
                //also make sure ToC is hidden
                if (getCurPageFragment() != null) {
//                    getCurPageFragment().toggleToC(PageFragment.TOC_ACTION_HIDE);
                }
                //and make sure to update dynamic items and highlights
                navDrawerHelper.setupDynamicNavDrawerItems();
                oncePerSlideLock = true;
            }
            // and make sure the Toolbar is showing
            showToolbar();
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (newState == DrawerLayout.STATE_IDLE) {
                oncePerSlideLock = false;
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    // Note: this method is invoked even when in CAB mode.
//    @Override
//    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
//        return isBackKeyUp(event) && ToolTipUtil.dismissToolTip(this)
//                || super.dispatchKeyEvent(event);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle other action bar items...
        return ActivityUtil.defaultOnOptionsItemSelected(this, item)
                || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSearchRequested() {
        showToolbar();
        searchFragment.openSearch();
        return true;
    }

    public void showToolbar() {
        ViewAnimations.ensureTranslationY(toolbarContainer, 0);
    }

    public void setNavMenuItemRandomEnabled(boolean enabled) {
        navMenu.findItem(R.id.nav_item_random).setEnabled(enabled);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
//        if (Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getData() != null) {
//            Site site = new Site(intent.getData().getAuthority());
//            PageTitle title = site.titleForUri(intent.getData());
//            HistoryEntry historyEntry = new HistoryEntry(title, HistoryEntry.SOURCE_EXTERNAL_LINK);
//            loadPageInForegroundTab(title, historyEntry);
//        } else if (ACTION_PAGE_FOR_TITLE.equals(intent.getAction())) {
//            PageTitle title = intent.getParcelableExtra(EXTRA_PAGETITLE);
//            HistoryEntry historyEntry = intent.getParcelableExtra(EXTRA_HISTORYENTRY);
//            loadPage(title, historyEntry);
//        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            PageTitle title = new PageTitle(query, app.getSite());
//            HistoryEntry historyEntry = new HistoryEntry(title, HistoryEntry.SOURCE_SEARCH);
//            loadPageInForegroundTab(title, historyEntry);
//        } else if (Intent.ACTION_SEND.equals(intent.getAction())
//                && PLAIN_TEXT_MIME_TYPE.equals(intent.getType())) {
//            new IntentFunnel(app).logShareIntent();
//            handleShareIntent(intent);
//        } else if (Intent.ACTION_PROCESS_TEXT.equals(intent.getAction())
//                && PLAIN_TEXT_MIME_TYPE.equals(intent.getType())) {
//            new IntentFunnel(app).logProcessTextIntent();
//            handleProcessTextIntent(intent);
//        } else if (intent.hasExtra(EXTRA_SEARCH_FROM_WIDGET)) {
//            new IntentFunnel(app).logSearchWidgetTap();
//            openSearch();
//        } else if (intent.hasExtra(EXTRA_FEATURED_ARTICLE_FROM_WIDGET)) {
//            new IntentFunnel(app).logFeaturedArticleWidgetTap();
//            loadMainPageInForegroundTab();
//        } else {
//            loadMainPageIfNoTabs();
//        }
    }

    private void handleShareIntent(Intent intent) {
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        openSearch(text == null ? null : text.trim());
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void handleProcessTextIntent(Intent intent) {
        if (!ApiUtil.hasMarshmallow()) {
            return;
        }
        String text = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT);
        openSearch(text == null ? null : text.trim());
    }

    private void openSearch() {
        openSearch(null);
    }

    private void openSearch(@Nullable final CharSequence query) {
//        fragmentContainerView.post(new Runnable() {
//            @Override
//            public void run() {
//                searchFragment.setLaunchedFromWidget(true);
//                searchFragment.openSearch();
//                if (query != null) {
//                    searchFragment.setSearchText(query);
//                }
//            }
//        });
    }

    /**
     * Returns whether we're currently in a "searching" state (i.e. the search fragment is shown).
     *
     * @return True if currently searching, false otherwise.
     */
    public boolean isSearching() {
        return searchFragment != null && searchFragment.isSearchActive();
    }

    public void closeNavDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void removeAllFragments() {
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * Add a new fragment to the top of the activity's backstack.
     *
     * @param f New fragment to place on top.
     */
    public void pushFragment(Fragment f) {
        pushFragment(f, false);
    }

    /**
     * Add a new fragment to the top of the activity's backstack, and optionally allow state loss.
     * Useful for cases where we might push a fragment from an AsyncTask result.
     *
     * @param f              New fragment to place on top.
     * @param allowStateLoss Whether to allow state loss.
     */
    public void pushFragment(Fragment f, boolean allowStateLoss) {
        closeNavDrawer();
        searchBarHideHandler.setForceNoFade(false);
        searchBarHideHandler.setFadeEnabled(false);
        // if the new fragment is the same class as the current topmost fragment,
        // then just keep the previous fragment there.
        // e.g. if the user selected History, and there's already a History fragment on top,
        // then there's no need to load a new History fragment.
        if (getTopFragment() != null && (getTopFragment().getClass() == f.getClass())) {
            return;
        }

        removeAllFragments();
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.add(R.id.content_fragment_container, f);
        trans.addToBackStack(null);
        if (allowStateLoss) {
            trans.commitAllowingStateLoss();
        } else {
            trans.commit();
        }

        // and make sure the ActionBar is visible
        showToolbar();
    }

    public void resetAfterClearHistory() {
        removeAllFragments();
        Prefs.clearTabs();
        loadMainPageIfNoTabs();
    }

    /**
     * Load a new page, and put it on top of the backstack.
     *
     * @param entry HistoryEntry associated with this page.
     */
    public void loadPage(HistoryEntry entry) {
        this.pages.add(entry);
        loadPage(entry, TabPosition.CURRENT_TAB, false, false);
    }

    // Note: back button first handled in {@link #onOptionsItemSelected()};
    @Override
    public void onBackPressed() {
//        if (isCabOpen()) {
//            finishActionMode();
//            return;
//        }
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeNavDrawer();
            return;
        }
        if (searchFragment.onBackPressed()) {
//            if (searchFragment.isLaunchedFromWidget()) {
//                finish();
//            }
            return;
        }

        Fragment topFragment = getTopFragment();
        if (topFragment != null) {
            boolean onBackPressed = ((BackPressedHandler) topFragment).onBackPressed();
            if (topFragment instanceof BackPressedHandler
                    && onBackPressed) {
                return;
            } else if (!(topFragment instanceof PageFragment)) {
//                pushFragment(new PageFragment(), false);
                return;
            }
        }
        finish();
    }

    private void loadMainPageIfNoTabs() {
        String restaurantUUID = "1CE562A4-A978-4B75-9B7B-2F3CF9F42A04"; // The Flying Falafel
//        String restaurantUUID = "33ED9F31-F6A5-43A4-8D11-8E511CA0BD39"; // The Spice Jar
        String eventUUID = "07B2D33C-F11D-404B-9D78-016D16BEE9FE"; // White Truffies
        String teamUUID = "197C0BEF-B432-47B8-988B-99406643623A";// Dolores Chavez
        String recipeUUID = "95B62D6F-87DF-47E2-8C84-EADAE131BB5D"; // Dark Gelate


//        HistoryEntry entry = new HistoryEntry(MainSegueIdentifier.nearbyRestaurants, LocationUtil.getLocation());
//        HistoryEntry entry =new HistoryEntry(MainSegueIdentifier.detailRestaurantSegueIdentifier,restaurantUUID);
//        HistoryEntry entry =new HistoryEntry(MainSegueIdentifier.detailEventSegueIdentifier,eventUUID);
//        HistoryEntry entry =new HistoryEntry(MainSegueIdentifier.detailOrderedRecipesSegueIdentifier,eventUUID,teamUUID);
        HistoryEntry entry =new HistoryEntry(MainSegueIdentifier.detailRecipeSegueIdentifier,recipeUUID);
        loadPage(entry, TabPosition.CURRENT_TAB, false, true);
    }

    public void loadPage(final HistoryEntry entry,
                         final TabPosition position,
                         boolean allowStateLoss,
                         final boolean mustBeEmpty) {
        if (isDestroyed()) {
            return;
        }

//        app.putCrashReportProperty("api", entry.getSource()+"");
//        app.putCrashReportProperty("hPara", entry.getHPara());
//        app.putCrashReportProperty("vPara", entry.getVPara());

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeNavDrawer();
        }

        pushFragment(MainSegueIdentifier.getFragment(entry.getSource()), allowStateLoss);

        fragmentContainerView.post(new Runnable() {
            @Override
            public void run() {
                PageFragment frag = getCurPageFragment();
                if (frag == null) {
                    return;
                }
                if (position == TabPosition.CURRENT_TAB) {
                    frag.loadPage(entry);
                } else if (position == TabPosition.NEW_TAB_BACKGROUND) {
//                    frag.openInNewBackgroundTabFromMenu(title, entry);
                } else {
//                    frag.openInNewForegroundTabFromMenu(title, entry);
                }
//                app.getSessionFunnel().pageViewed(entry);
            }
        });
    }


    private class EventBusMethods {
//        @Subscribe
//        public void onChangeTextSize(ChangeTextSizeEvent event) {
//            if (getCurPageFragment() != null && getCurPageFragment().getWebView() != null) {
//                getCurPageFragment().updateFontSize();
//            }
//        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (bus == null) {
            registerBus();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState(outState);
    }

    public void updateNavDrawerSelection(Fragment fragment) {
        navDrawerHelper.updateItemSelection(fragment);
    }

    private void saveState(Bundle outState) {
        outState.putBoolean("pausedZeroEnabledState", isZeroEnabled);
        outState.putBoolean("isSearching", isSearching());
//        outState.putString(LANGUAGE_CODE_BUNDLE_KEY, app.getAppOrSystemLanguageCode());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (bus == null) {
            registerBus();
        }
//        if (settingsActivityRequested(requestCode)) {
//            handleSettingsActivityResult(resultCode);
//        }else if (newArticleLanguageSelected(requestCode, resultCode) || galleryFilePageSelected(requestCode, resultCode)) {
//            handleLangLinkOrFilePageResult(data);
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
    }

    private void handleLangLinkOrFilePageResult(final Intent data) {
        fragmentContainerView.post(new Runnable() {
            @Override
            public void run() {
                handleIntent(data);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterBus();
    }

    /**
     * ActionMode that is invoked when the user long-presses inside the WebView.
     *
     * @param mode ActionMode under which this context is starting.
     */
    @Override
    public void onSupportActionModeStarted(ActionMode mode) {
        freezeToolbar();
        super.onSupportActionModeStarted(mode);
    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {
        super.onSupportActionModeFinished(mode);
        searchBarHideHandler.setForceNoFade(false);
    }

    @Override
    public void onActionModeStarted(android.view.ActionMode mode) {
        freezeToolbar();
        super.onActionModeStarted(mode);
    }

    @Override
    public void onActionModeFinished(android.view.ActionMode mode) {
        super.onActionModeFinished(mode);
        searchBarHideHandler.setForceNoFade(false);
    }

    private void freezeToolbar() {
        getSearchBarHideHandler().setForceNoFade(true);
    }

    private void registerBus() {
        bus = app.getBus();
        bus.register(busMethods);
        L.d("Registered bus.");
    }

    private void unregisterBus() {
        bus.unregister(busMethods);
        bus = null;
        L.d("Unregistered bus.");
    }

    private void handleSettingsActivityResult(int resultCode) {
        if (languageChanged(resultCode)) {
            loadNewLanguageMainPage();
        }
    }

    private boolean settingsActivityRequested(int requestCode) {
        return requestCode == SettingsActivity.ACTIVITY_REQUEST_SHOW_SETTINGS;
    }

    private boolean languageChanged(int resultCode) {
        return resultCode == SettingsActivity.ACTIVITY_RESULT_LANGUAGE_CHANGED;
    }

    /**
     * Reload the main page in the new language, after delaying for one second in order to:
     * (1) Make sure that onStart in PageActivity gets called, thus registering the activity for the bus.
     * (2) Ensure a smooth transition, which is very jarring without a delay.
     */
    private void loadNewLanguageMainPage() {
        Handler uiThread = new Handler(Looper.getMainLooper());
//        uiThread.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                loadMainPageInForegroundTab();
//                updateFeaturedPageWidget();
//            }
//        }, DateUtils.SECOND_IN_MILLIS);
    }

    /**
     * Update any instances of our Featured Page widget, since it will change with the currently selected language.
     */
    private void updateFeaturedPageWidget() {
//        Intent widgetIntent = new Intent(this, WidgetProviderFeaturedPage.class);
//        widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(
//                new ComponentName(this, WidgetProviderFeaturedPage.class));
//        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
//        sendBroadcast(widgetIntent);
    }
}
