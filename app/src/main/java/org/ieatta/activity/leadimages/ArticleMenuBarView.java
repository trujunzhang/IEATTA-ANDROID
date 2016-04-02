package org.ieatta.activity.leadimages;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.ieatta.R;
import org.wikipedia.util.FeedbackUtil;
import org.wikipedia.util.StringUtil;
import org.wikipedia.util.log.L;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class ArticleMenuBarView extends LinearLayout {


    public interface Callback {
        void onBookmarkClick(boolean bookmarkSaved);

        void onShareClick();

        void onNavigateClick();

        void onWriteReviewClick();

        void onSeeReviewsClick();

        void onEditClick();

        void onAddEventClick();

        void onSelectPersonClick();

        void onAddFoodClick();
    }

    public static class DefaultCallback implements Callback {
        @Override
        public void onBookmarkClick(boolean bookmarkSaved) {
        }

        @Override
        public void onShareClick() {
        }

        @Override
        public void onNavigateClick() {
        }

        @Override
        public void onWriteReviewClick() {

        }

        @Override
        public void onSeeReviewsClick() {

        }

        @Override
        public void onEditClick() {

        }

        @Override
        public void onAddEventClick() {

        }

        @Override
        public void onSelectPersonClick() {

        }

        @Override
        public void onAddFoodClick() {

        }
    }

    @Bind(R.id.view_article_menu_bar_bookmark)
    ImageView bookmark;
    @Bind(R.id.view_article_menu_bar_share)
    ImageView share;
    @Bind(R.id.view_article_menu_bar_navigate)
    ImageView navigate;

    @Bind(R.id.view_article_menu_bar_write_review)
    ImageView write_review;
    @Bind(R.id.view_article_menu_bar_see_reviews)
    ImageView see_reviews;
    @Bind(R.id.view_article_menu_bar_edit)
    ImageView edit;

    @Bind(R.id.view_article_menu_bar_add_event)
    ImageView add_event;
    @Bind(R.id.view_article_menu_bar_select_person)
    ImageView select_people;
    @Bind(R.id.view_article_menu_bar_add_food)
    ImageView add_food;

    @NonNull
    private Callback callback = new DefaultCallback();

    public ArticleMenuBarView(Context context) {
        super(context);
        init();
    }

    public ArticleMenuBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArticleMenuBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ArticleMenuBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setCallback(@Nullable Callback callback) {
        this.callback = callback == null ? new DefaultCallback() : callback;
    }

    public void updateBookmark(boolean bookmarkSaved) {
        bookmark.setActivated(bookmarkSaved);
    }

    public void updateNavigate(boolean geolocated) {
        navigate.setVisibility(geolocated ? VISIBLE : GONE);
    }

    public void updateMenuItemsVisibilities(int source) {
        HashMap<Integer, Boolean> map = MenuBarItemVisibilities.menuBarItemsVisibility(source);

        bookmark.setVisibility(map.get(MenuBarItemVisibilities.type_menu_bar_bookmark) ? VISIBLE : GONE);
        share.setVisibility(map.get(MenuBarItemVisibilities.type_menu_bar_share) ? VISIBLE : GONE);
        navigate.setVisibility(map.get(MenuBarItemVisibilities.type_menu_bar_navigate) ? VISIBLE : GONE);

        write_review.setVisibility(map.get(MenuBarItemVisibilities.type_menu_bar_write_review) ? VISIBLE : GONE);
        see_reviews.setVisibility(map.get(MenuBarItemVisibilities.type_menu_bar_see_reviews) ? VISIBLE : GONE);
        edit.setVisibility(map.get(MenuBarItemVisibilities.type_menu_bar_edit) ? VISIBLE : GONE);

        add_event.setVisibility(map.get(MenuBarItemVisibilities.type_menu_bar_add_event) ? VISIBLE : GONE);
        select_people.setVisibility(map.get(MenuBarItemVisibilities.type_menu_bar_select_person) ? VISIBLE : GONE);
        add_food.setVisibility(map.get(MenuBarItemVisibilities.type_menu_bar_add_food) ? VISIBLE : GONE);
    }

    public void resetMenuBarColor() {
        setMenuBarColor(getResources().getColor(R.color.grey_700));
    }

    public void setMenuBarColor(@ColorInt int color) {
        final int animDuration = 500;
        final ObjectAnimator animator = ObjectAnimator.ofObject(getBackground(), "color",
                new ArgbEvaluator(), color);
        animator.setDuration(animDuration);
        animator.start();
    }

    @OnClick({R.id.view_article_menu_bar_bookmark,
            R.id.view_article_menu_bar_share,
            R.id.view_article_menu_bar_navigate,

            R.id.view_article_menu_bar_write_review,
            R.id.view_article_menu_bar_see_reviews,
            R.id.view_article_menu_bar_edit,

            R.id.view_article_menu_bar_add_event,
            R.id.view_article_menu_bar_select_person,
            R.id.view_article_menu_bar_add_food,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_article_menu_bar_bookmark:
                view.setActivated(!view.isActivated());
                callback.onBookmarkClick(view.isActivated());
                break;
            case R.id.view_article_menu_bar_share:
                callback.onShareClick();
                break;
            case R.id.view_article_menu_bar_navigate:
                callback.onNavigateClick();
                break;

            case R.id.view_article_menu_bar_write_review:
                callback.onWriteReviewClick();
                break;
            case R.id.view_article_menu_bar_see_reviews:
                callback.onSeeReviewsClick();
                break;
            case R.id.view_article_menu_bar_edit:
                callback.onEditClick();
                break;

            case R.id.view_article_menu_bar_add_event:
                callback.onAddEventClick();
                break;
            case R.id.view_article_menu_bar_select_person:
                callback.onSelectPersonClick();
                break;
            case R.id.view_article_menu_bar_add_food:
                callback.onAddFoodClick();
                break;

            default:
                L.w("Unknown id=" + StringUtil.intToHexStr(view.getId()));
                break;
        }
    }

    @OnLongClick({R.id.view_article_menu_bar_bookmark,
            R.id.view_article_menu_bar_share,
            R.id.view_article_menu_bar_navigate,

            R.id.view_article_menu_bar_write_review,
            R.id.view_article_menu_bar_see_reviews,
            R.id.view_article_menu_bar_edit,

            R.id.view_article_menu_bar_add_event,
            R.id.view_article_menu_bar_select_person,
            R.id.view_article_menu_bar_add_food,

    })
    public boolean onLongClick(View view) {
        if (!TextUtils.isEmpty(view.getContentDescription())) {
            FeedbackUtil.showToolbarButtonToast(view);
            return true;
        }
        return false;
    }

    private void init() {
        inflate();
        bind();
    }

    private void inflate() {
        inflate(getContext(), R.layout.view_article_menu_bar, this);
    }

    private void bind() {
        ButterKnife.bind(this);
    }
}
