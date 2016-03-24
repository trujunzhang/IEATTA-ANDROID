package org.ieatta.activity.fragments.search;

import org.ieatta.IEAApp;
import org.ieatta.activity.PageActivity;
import org.ieatta.activity.fragments.PageFragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import org.ieatta.R;
import org.ieatta.activity.history.HistoryEntry;
import org.wikipedia.analytics.SearchFunnel;

import static org.wikipedia.util.DeviceUtil.hideSoftKeyboard;

public class SearchArticlesFragment extends PageFragment {

    private static final String ARG_LAST_SEARCHED_TEXT = "lastSearchedText";
    private static final String ARG_SEARCH_CURRENT_PANEL = "searchCurrentPanel";

    private static final int PANEL_RECENT_SEARCHES = 0;
    private static final int PANEL_SEARCH_RESULTS = 1;

    private IEAApp app;
    private SearchView searchView;
    private EditText searchEditText;
    private SearchFunnel funnel;
    private TextView langButton;


    public SearchFunnel getFunnel() {
        return funnel;
    }

    private boolean launchedFromWidget = false;
    public void setLaunchedFromWidget(boolean fromWidget) {
        launchedFromWidget = fromWidget;
    }
    public boolean isLaunchedFromWidget() {
        return launchedFromWidget;
    }

    /**
     * Whether the Search fragment is currently showing.
     */
    private boolean isSearchActive = false;

    /**
     * The last search term that the user entered. This will be passed into
     * the TitleSearch and FullSearch sub-fragments.
     */
    private String lastSearchedText;

    /**
     * View that contains the whole Search fragment. This is what should be shown/hidden when
     * the search is called for from the main activity.
     */
    private View searchContainerView;

    private RecentSearchesFragment recentSearchesFragment;
    private SearchResultsFragment searchResultsFragment;


    /**
     * Activate the Search fragment.
     */
    public void openSearch() {
        // create a new funnel every time Search is opened, to get a new session ID
        funnel = new SearchFunnel(IEAApp.getInstance());
        funnel.searchStart();
        isSearchActive = true;
        // invalidate our activity's ActionBar, so that all action items are removed, and
        // we can fill up the whole width of the ActionBar with our SearchView.
        getActivity().supportInvalidateOptionsMenu();
        ((PageActivity)getActivity()).getSearchBarHideHandler().setForceNoFade(true);
        setSearchViewEnabled(true);
        ((PageActivity) getActivity()).getDrawerToggle().setDrawerIndicatorEnabled(false);
        // show ourselves
        searchContainerView.setVisibility(View.VISIBLE);

        // if the current search string is empty, then it's a fresh start, so we'll show
        // recent searches by default. Otherwise, the currently-selected panel should already
        // be visible, so we don't need to do anything.
        if (TextUtils.isEmpty(lastSearchedText)) {
            showPanel(PANEL_RECENT_SEARCHES);
        }
    }


    /**
     * Show a particular panel, which can be one of:
     * - PANEL_RECENT_SEARCHES
     * - PANEL_SEARCH_RESULTS
     * Automatically hides the previous panel.
     * @param panel Which panel to show.
     */
    private void showPanel(int panel) {
        switch (panel) {
            case PANEL_RECENT_SEARCHES:
                searchResultsFragment.hide();
                recentSearchesFragment.show();
                break;
            case PANEL_SEARCH_RESULTS:
                recentSearchesFragment.hide();
                searchResultsFragment.show();
                break;
            default:
                break;
        }
    }


    private void setSearchViewEnabled(boolean enabled) {
        LinearLayout enabledSearchBar = (LinearLayout) getActivity().findViewById(R.id.search_bar_enabled);
        TextView searchButton = (TextView) getActivity().findViewById(R.id.main_search_bar_text);
        langButton = (TextView) getActivity().findViewById(R.id.search_lang_button);
        FrameLayout langButtonContainer = (FrameLayout) getActivity().findViewById(R.id.search_lang_button_container);

        if (enabled) {
            // set up the language picker
//            langButton.setText(app.getAppOrSystemLanguageCode().toUpperCase());
//            formatLangButtonText();
            langButtonContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    showLangPreferenceDialog();
                }
            });

            // set up the SearchView
            if (searchView == null) {
                searchView = (SearchView)getActivity().findViewById(R.id.main_search_view);
//                searchView.setOnQueryTextListener(searchQueryListener);
//                searchView.setOnCloseListener(searchCloseListener);

                searchEditText = (EditText) searchView
                        .findViewById(android.support.v7.appcompat.R.id.search_src_text);
                // make the text size be the same as the size of the search field
                // placeholder in the main activity
                searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, searchButton.getTextSize());
                // reset its background
                searchEditText.setBackgroundColor(Color.TRANSPARENT);
                // make the search frame match_parent
                View searchEditFrame = searchView
                        .findViewById(android.support.v7.appcompat.R.id.search_edit_frame);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                searchEditFrame.setLayoutParams(params);
                // center the search text in it
                searchEditText.setGravity(Gravity.CENTER_VERTICAL);
                // remove focus line from search plate
                View searchEditPlate = searchView
                        .findViewById(android.support.v7.appcompat.R.id.search_plate);
                searchEditPlate.setBackgroundColor(Color.TRANSPARENT);
            }

//            updateZeroChrome();
            searchView.setIconified(false);
            searchView.requestFocusFromTouch();

            // if we already have a previous search query, then put it into the SearchView, and it will
            // automatically trigger the showing of the corresponding search results.
//            if (isValidQuery(lastSearchedText)) {
//                searchView.setQuery(lastSearchedText, false);
//                // automatically select all text in the search field, so that typing a new character
//                // will clear it by default
//                if (searchEditText != null) {
//                    searchEditText.selectAll();
//                }
//            }
            searchButton.setVisibility(View.GONE);
            enabledSearchBar.setVisibility(View.VISIBLE);
        } else {
            enabledSearchBar.setVisibility(View.GONE);
            searchButton.setVisibility(View.VISIBLE);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_recycleview, container, false);

        return view;
    }

    @Override
    public void loadPage(HistoryEntry entry) {

    }

    @Override
    public void postLoadPage() {

    }

    public void switchToSearch(String queryText) {
        startSearch(queryText, true);
        searchView.setQuery(queryText, false);
    }


    /**
     * Kick off a search, based on a given search term. Will automatically pass the search to
     * Title search or Full search, based on which one is currently displayed.
     * If the search term is empty, the "recent searches" view will be shown.
     * @param term Phrase to search for.
     * @param force Whether to "force" starting this search. If the search is not forced, the
     *              search may be delayed by a small time, so that network requests are not sent
     *              too often.  If the search is forced, the network request is sent immediately.
     */
    public void startSearch(String term, boolean force) {
        if (!isSearchActive) {
            openSearch();
        }

//        if (TextUtils.isEmpty(term)) {
//            showPanel(PANEL_RECENT_SEARCHES);
//        } else if (getActivePanel() == PANEL_RECENT_SEARCHES) {
//            //start with title search...
//            showPanel(PANEL_SEARCH_RESULTS);
//        }

        lastSearchedText = term;

        searchResultsFragment.startSearch(term, force);
    }
}
