package org.ieatta.activity.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.ieatta.IEAApp;
import org.ieatta.activity.DetailPageLoadStrategy;
import org.ieatta.activity.PageActivity;
import org.ieatta.activity.PageLoadStrategy;
import org.ieatta.activity.PageViewModel;
import org.ieatta.activity.history.HistoryEntry;
import org.ieatta.views.ObservableWebView;
import org.wikipedia.BackPressedHandler;

public abstract class PageFragment extends Fragment implements BackPressedHandler {
    private IEAApp app;

    protected PageLoadStrategy pageLoadStrategy;
    protected PageViewModel model;

    private HistoryEntry entry;
    private boolean pushBackStack;
    private int stagedScrollY;

    public abstract void loadPage(HistoryEntry entry);

    public void loadPage(HistoryEntry entry, boolean pushBackStack, int stagedScrollY) {
        this.entry = entry;
        this.pushBackStack = pushBackStack;
        this.stagedScrollY = stagedScrollY;

        this.loadPage(entry);
    }

    public void postLoadPage() {
        pageLoadStrategy.load(pushBackStack, stagedScrollY);
    }

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

    public int getWebViewScrollY(ObservableWebView webView) {
        return webView.getLastTop();
    }
}
