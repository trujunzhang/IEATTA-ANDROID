package org.ieatta.activity.leadimages;

import org.ieatta.activity.PageLoadStrategy;

public class MenuBarCallback extends ArticleMenuBarView.DefaultCallback {
    @Override
    public void onBookmarkClick(boolean bookmarkSaved) {
//            if (getPage() == null) {
//                return;
//            }
//
//            if (bookmarkSaved) {
//                saveBookmark();
//            } else {
//                deleteBookmark();
//            }
    }

    @Override
    public void onShareClick() {
//            if (getPage() != null) {
//                ShareUtil.shareText(getActivity(), getPage().getTitle());
//            }
    }

    @Override
    public void onNavigateClick() {
        openGeoIntent();
    }

    private void saveBookmark() {

    }

    private void deleteBookmark() {

    }

    private void openGeoIntent() {
//            if (getGeo() != null) {
////                UriUtil.sendGeoIntent(getActivity(), getGeo(), getTitle().getDisplayText());
//            }
    }
}
