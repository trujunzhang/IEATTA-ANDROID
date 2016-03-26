package org.ieatta.activity;

import org.ieatta.activity.history.HistoryEntry;

public class PageBackStackItem {

    private final HistoryEntry historyEntry;
    public HistoryEntry getHistoryEntry() {
        return historyEntry;
    }

    private int scrollY;
    private int parallaxScrollY;
    public int getScrollY() {
        return scrollY;
    }
    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }

    public PageBackStackItem( HistoryEntry historyEntry) {
        this.historyEntry = historyEntry;
    }

    public int getParallaxScrollY() {
        return parallaxScrollY;
    }

    public void setParallaxScrollY(int parallaxScrollY) {
        this.parallaxScrollY = parallaxScrollY;
    }
}