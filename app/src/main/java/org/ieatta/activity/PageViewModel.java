package org.ieatta.activity;

import android.support.annotation.Nullable;

import org.ieatta.activity.history.HistoryEntry;

/**
 * Shared data between PageFragment and PageLoadStrategy
 */
public class PageViewModel {
    @Nullable
    private Page page;
    private PageTitle title;
    private PageTitle titleOriginal;
    private HistoryEntry curEntry;

    private boolean pushBackStack;
    private int stagedScrollY;

    private int actionbarHeight;

    @Nullable
    public Page getPage() {
        return page;
    }

    public void setPage(@Nullable Page page) {
        this.page = page;
        assert page != null;
        this.setTitle(page.getTitle());
    }

    public PageTitle getTitle() {
        return title;
    }

    public void setTitle(PageTitle title) {
        this.title = title;
    }

    public PageTitle getTitleOriginal() {
        return titleOriginal;
    }

    public void setTitleOriginal(PageTitle titleOriginal) {
        this.titleOriginal = titleOriginal;
    }

    public HistoryEntry getCurEntry() {
        return curEntry;
    }

    public void setCurEntry(HistoryEntry curEntry) {
        this.curEntry = curEntry;
    }

    public boolean isPushBackStack() {
        return pushBackStack;
    }

    public void setPushBackStack(boolean pushBackStack) {
        this.pushBackStack = pushBackStack;
    }

    public int getStagedScrollY() {
        return stagedScrollY;
    }

    public void setStagedScrollY(int stagedScrollY) {
        this.stagedScrollY = stagedScrollY;
    }

    public int getActionbarHeight() {
        return actionbarHeight;
    }

    public void setActionbarHeight(int actionbarHeight) {
        this.actionbarHeight = actionbarHeight;
    }
}
