package org.ieatta.activity.leadimages;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.location.Location;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.ieatta.IEAApp;
import org.ieatta.R;
import org.ieatta.activity.PageProperties;
import org.ieatta.activity.PageTitle;
import org.ieatta.activity.fragments.DetailFragment;
import org.ieatta.activity.Page;
import org.ieatta.views.ObservableWebView;
import org.json.JSONException;
import org.json.JSONObject;
import org.wikipedia.concurrency.RecurringTask;
import org.wikipedia.util.DimenUtil;
import org.wikipedia.util.UriUtil;


import bolts.Continuation;
import bolts.Task;

import static org.wikipedia.util.DimenUtil.getContentTopOffsetPx;

public class LeadImagesHandler {
    /**
     * Minimum screen height for enabling lead images. If the screen is smaller than
     * this height, lead images will not be displayed, and will be substituted with just
     * the page title.
     */
    private static final int MIN_SCREEN_HEIGHT_DP = 480;

    public interface OnLeadImageLayoutListener {
        void onLayoutComplete(int sequence);
    }

    @NonNull private final DetailFragment parentFragment;
    @NonNull private final ObservableWebView webView;

    @NonNull private final ArticleHeaderView articleHeaderView;
    private View image;

    private int displayHeightDp;
    private float faceYOffsetNormalized;
    private float displayDensity;

    private RecurringTask task = new RecurringTask();

    public LeadImagesHandler(@NonNull final DetailFragment parentFragment,
                             @NonNull ObservableWebView webView,
                             @NonNull ArticleHeaderView articleHeaderView) {
        this.articleHeaderView = articleHeaderView;
        this.articleHeaderView.setMenuBarCallback(new MenuBarCallback());
        this.parentFragment = parentFragment;
        this.webView = webView;

        image = articleHeaderView.getImage();

        initDisplayDimensions();

        initArticleHeaderView();

        // hide ourselves by default
        hide();
    }

    /**
     * Completely hide the lead image view. Useful in case of network errors, etc.
     * The only way to "show" the lead image view is by calling the beginLayout function.
     */
    public void hide() {
        articleHeaderView.hide();
    }

    @Nullable public Bitmap getLeadImageBitmap() {
        return isLeadImageEnabled() ? articleHeaderView.copyBitmap() : null;
    }

    public boolean isLeadImageEnabled() {
        return true;
//        return IEAApp.getInstance().isImageDownloadEnabled()
//                && displayHeightDp >= MIN_SCREEN_HEIGHT_DP
//                && !TextUtils.isEmpty(getLeadImageUrl());
    }

    public void updateBookmark(boolean bookmarkSaved) {
        articleHeaderView.updateBookmark(bookmarkSaved);
    }

    public void updateNavigate(@Nullable Location geo) {
        articleHeaderView.updateNavigate(geo != null);
    }

    public void setAnimationPaused(boolean paused) {
        articleHeaderView.setAnimationPaused(paused);
    }

    /**
     * Returns the normalized (0.0 to 1.0) vertical focus position of the lead image.
     * A value of 0.0 represents the top of the image, and 1.0 represents the bottom.
     * The "focus position" is currently defined by automatic face detection, but may be
     * defined by other factors in the future.
     *
     * @return Normalized vertical focus position.
     */
    public float getLeadImageFocusY() {
        return faceYOffsetNormalized;
    }

    /**
     * Triggers a chain of events that will lay out the lead image, page title, and other
     * elements, at the end of which the WebView contents may begin to be composed.
     * These events (performed asynchronously) are in the following order:
     * - Dynamically resize the page title TextView and, if necessary, adjust its font size
     * based on the length of our page title.
     * - Dynamically resize the lead image container view and restyle it, if necessary, depending
     * on whether the page contains a lead image, and whether our screen resolution is high
     * enough to warrant a lead image.
     * - Send a "padding" event to the WebView so that any subsequent content that's added to it
     * will be correctly offset to account for the lead image view (or lack of it)
     * - Make the lead image view visible.
     * - Fire a callback to the provided Listener indicating that the rest of the WebView content
     * can now be loaded.
     * - Fetch and display the WikiData description for this page, if available.
     * <p/>
     * Realistically, the whole process will happen very quickly, and almost unnoticeably to the
     * user. But it still needs to be asynchronous because we're dynamically laying out views, and
     * because the padding "event" that we send to the WebView must come before any other content
     * is sent to it, so that the effect doesn't look jarring to the user.
     *
     * @param listener Listener that will receive an event when the layout is completed.
     */
    public void beginLayout(OnLeadImageLayoutListener listener,
                            int sequence) {
        if (getPage() == null) {
            return;
        }

        initDisplayDimensions();

        // set the page title text, and honor any HTML formatting in the title
        task.periodicTask(new RecurringTask.RecurringEvent() {
            @Override
            public void everyTask() {
                LeadImagesHandler.this.recurringLeadImages();
            }
        },0,20);


        articleHeaderView.setTitle(Html.fromHtml(getPage().getDisplayTitle()));
//        articleHeaderView.setLocale(getPage().getTitle().getSite().getLanguageCode());
        // Set the subtitle, too, so text measurements are accurate.
        layoutWikiDataDescription(getTitle().getDescription());
        layoutViews(listener, sequence);
    }

    private void recurringLeadImages() {
        if(this.getPage() == null){
            return;
        }
        final PageProperties pageProperties = this.getPage().getPageProperties();

        pageProperties.getLeadImageLocalUrl().onSuccessTask(new Continuation<String, Task<String>>() {
            @Override
            public Task<String> then(Task<String> task) throws Exception {
                LeadImagesHandler.this.loadLeadImage(task.getResult(),true);
                return pageProperties.getLeadImageOnlineUrl();
            }
        },Task.UI_THREAD_EXECUTOR).onSuccess(new Continuation<String, Void>() {
            @Override
            public Void then(Task<String> task) throws Exception {
                LeadImagesHandler.this.loadLeadImage(task.getResult(),false);
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }

    /**
     * The final step in the layout process:
     * Apply sizing and styling to our page title and lead image views, based on how large our
     * page title ended up, and whether we should display the lead image.
     *
     * @param listener Listener that will receive an event when the layout is completed.
     */
    private void layoutViews(OnLeadImageLayoutListener listener, int sequence) {
        if (!isFragmentAdded()) {
            return;
        }

        if (isMainPage()) {
            articleHeaderView.hide();
        } else {
            if (!isLeadImageEnabled()) {
                articleHeaderView.showText();
            } else {
                articleHeaderView.showTextImage();
            }
        }

        // tell our listener that it's ok to start loading the rest of the WebView content
        listener.onLayoutComplete(sequence);
    }

    private void updatePadding() {
        int padding;
        if (isMainPage()) {
            padding = Math.round(getContentTopOffsetPx(getActivity()) / displayDensity);
        } else {
            padding = Math.round(articleHeaderView.getHeight() / displayDensity);
        }

        setWebViewPaddingTop(padding);
    }

    private void setWebViewPaddingTop(int padding) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("paddingTop", padding);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
//        bridge.sendMessage("setPaddingTop", payload);
    }

    /**
     * Final step in the WikiData description process: lay out the description, and animate it
     * into place, along with the page title.
     *
     * @param description WikiData description to be shown.
     */
    private void layoutWikiDataDescription(@Nullable final String description) {
        if (TextUtils.isEmpty(description)) {
            articleHeaderView.setSubtitle(null);
        } else {
            int titleLineCount = articleHeaderView.getLineCount();

            articleHeaderView.setSubtitle(description);

            // Only show the description if it's two lines or less.
            if ((articleHeaderView.getLineCount() - titleLineCount) > 2) {
                articleHeaderView.setSubtitle(null);
            }
        }
    }

    /**
     * Determines and sets displayDensity and displayHeightDp for the lead images layout.
     */
    private void initDisplayDimensions() {
        // preload the display density, since it will be used in a lot of places
        displayDensity = DimenUtil.getDensityScalar();

        int displayHeightPx = DimenUtil.getDisplayHeightPx();

        displayHeightDp = (int) (displayHeightPx / displayDensity);
    }

//    private void loadLeadImage(String url) {
//        loadLeadImage(url);
//    }

    /**
     * @param url Nullable URL with no scheme. For example, foo.bar.com/ instead of
     *            http://foo.bar.com/.
     */
    private void loadLeadImage(@Nullable String url,Boolean local) {
        if (!isMainPage() && !TextUtils.isEmpty(url) && isLeadImageEnabled()) {
            articleHeaderView.setImageYScalar(0);
            articleHeaderView.loadImage(url,local);
        } else {
            articleHeaderView.loadImage(null,local);
        }
    }

//    /**
//     * @return Nullable URL with no scheme. For example, foo.bar.com/ instead of
//     * http://foo.bar.com/.
//     */
//    @Nullable
//    private String getLeadImageUrl() {
//        return getPage() == null ? null : getPage().getPageProperties().getLeadImageUrl();
//    }

    @Nullable
    private Location getGeo() {
//        return getPage() == null ? null : getPage().getPageProperties().getGeo();
        return null;
    }

    private void startKenBurnsAnimation() {
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.lead_image_zoom);
        image.startAnimation(anim);
    }

    private void initArticleHeaderView() {
        articleHeaderView.setOnImageLoadListener(new ImageLoadListener());
        articleHeaderView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            @SuppressWarnings("checkstyle:parameternumber")
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                updatePadding();
            }
        });
    }

    private boolean isMainPage() {
        return false;
    }

    private PageTitle getTitle() {
        return parentFragment.getTitle();
    }

    @Nullable
    private Page getPage() {
        return parentFragment.getPage();
    }

    private boolean isFragmentAdded() {
        return parentFragment.isAdded();
    }

    private float getDimension(@DimenRes int id) {
        return getResources().getDimension(id);
    }

    private Resources getResources() {
        return getActivity().getResources();
    }

    private FragmentActivity getActivity() {
        return parentFragment.getActivity();
    }

    private class MenuBarCallback extends ArticleMenuBarView.DefaultCallback {
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
            if (getGeo() != null) {
//                UriUtil.sendGeoIntent(getActivity(), getGeo(), getTitle().getDisplayText());
            }
        }
    }

    private class ImageLoadListener implements ImageViewWithFace.OnImageLoadListener {
        @Override
        public void onImageLoaded(final int bmpHeight, @Nullable final PointF faceLocation, @ColorInt final int mainColor) {
            articleHeaderView.post(new Runnable() {
                @Override
                public void run() {
                    if (isFragmentAdded()) {
                        applyFaceLocationOffset(bmpHeight, faceLocation);
                        articleHeaderView.setMenuBarColor(mainColor);
                        startKenBurnsAnimation();
                    }
                }
            });
        }

        @Override
        public void onImageFailed() {
            articleHeaderView.resetMenuBarColor();
        }

        private void applyFaceLocationOffset(int bmpHeight, @Nullable PointF faceLocation) {
            faceYOffsetNormalized = faceYScalar(bmpHeight, faceLocation);
            articleHeaderView.setImageYScalar(constrainScalar(faceYOffsetNormalized));
        }

        private float constrainScalar(float scalar) {
            scalar = Math.max(0, scalar);
            scalar = Math.min(scalar, 1);
            return scalar;
        }

        private float faceYScalar(int bmpHeight, @Nullable PointF faceLocation) {
            final float defaultOffsetScalar = .25f;
            float scalar = defaultOffsetScalar;
            if (faceLocation != null) {
                scalar = faceLocation.y;
                // TODO: if it is desirable to offset to the nose, replace this arbitrary hardcoded
                //       value with a proportion. FaceDetector.eyesDistance() presumably provides
                //       the interpupillary distance in pixels. We can multiply this measurement by
                //       the proportion of the length of the nose to the IPD.
                scalar -= getDimension(R.dimen.face_detection_nose_y_offset) / bmpHeight;
            }
            return scalar;
        }
    }
}
