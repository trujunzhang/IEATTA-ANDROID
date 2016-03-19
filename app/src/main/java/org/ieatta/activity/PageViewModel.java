package org.ieatta.activity;

import android.support.annotation.Nullable;

/**
 * Shared data between PageFragment and PageLoadStrategy
 */
public class PageViewModel {
    @Nullable private Page page;
    private PageTitle title;
    private PageTitle titleOriginal;

    @Nullable
    public Page getPage() {
        return page;
    }

    public void setPage(@Nullable Page page) {
        this.page = page;
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
}
