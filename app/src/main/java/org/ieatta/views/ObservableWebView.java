package org.ieatta.views;


import android.content.Context;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.graphics.Canvas;

import org.ieatta.IEAApp;
import org.ieatta.analytics.ObservableWebViewFunnel;
import org.ieatta.events.WebViewInvalidateEvent;

import java.util.ArrayList;
import java.util.List;

public class ObservableWebView extends RecyclerView {
    private static final WebViewInvalidateEvent INVALIDATE_EVENT = new WebViewInvalidateEvent();

    private List<OnClickListener> onClickListeners;
    private List<OnScrollChangeListener> onScrollChangeListeners;
    private List<OnDownMotionEventListener> onDownMotionEventListeners;
    private List<OnUpOrCancelMotionEventListener> onUpOrCancelMotionEventListeners;
    private List<OnContentHeightChangedListener> onContentHeightChangedListeners;
    private OnFastScrollListener onFastScrollListener;

    private int contentHeight = 0;
    private float touchStartX;
    private float touchStartY;
    private int touchSlop;

    private long lastScrollTime;
    private int totalAmountScrolled;

    private int lastTop;

    /**
     * Threshold (in pixels) of continuous scrolling, to be considered "fast" scrolling.
     */
    private static final int FAST_SCROLL_THRESHOLD = (int) (1000 * IEAApp.getInstance().getScreenDensity());

    /**
     * Maximum single scroll amount (in pixels) to be considered a "human" scroll.
     * Otherwise it's probably a programmatic scroll, which we won't count.
     */
    private static final int MAX_HUMAN_SCROLL = (int) (500 * IEAApp.getInstance().getScreenDensity());

    /**
     * Maximum amount of time that needs to elapse before the previous scroll amount
     * is "forgotten." That is, if the user scrolls once, then scrolls again within this
     * time, then the two scroll actions will be added together as one, and counted towards
     * a possible "fast" scroll.
     */
    private static final int MAX_MILLIS_BETWEEN_SCROLLS = 500;

    public void addOnClickListener(OnClickListener onClickListener) {
        onClickListeners.add(onClickListener);
    }

    public void addOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        onScrollChangeListeners.add(onScrollChangeListener);
    }

    public void addOnDownMotionEventListener(OnDownMotionEventListener onDownMotionEventListener) {
        onDownMotionEventListeners.add(onDownMotionEventListener);
    }

    public void addOnUpOrCancelMotionEventListener(OnUpOrCancelMotionEventListener onUpOrCancelMotionEventListener) {
        onUpOrCancelMotionEventListeners.add(onUpOrCancelMotionEventListener);
    }

    public void addOnContentHeightChangedListener(OnContentHeightChangedListener onContentHeightChangedListener) {
        onContentHeightChangedListeners.add(onContentHeightChangedListener);
    }

    public void setOnFastScrollListener(OnFastScrollListener onFastScrollListener) {
        this.onFastScrollListener = onFastScrollListener;
    }

    public interface OnClickListener {
        boolean onClick(float x, float y);
    }

    public interface OnScrollChangeListener {
        void onScrollChanged(int oldScrollY, int scrollY, boolean isHumanScroll);
    }

    public interface OnDownMotionEventListener {
        void onDownMotionEvent();
    }

    public interface OnUpOrCancelMotionEventListener {
        void onUpOrCancelMotionEvent();
    }

    public interface OnContentHeightChangedListener {
        void onContentHeightChanged(int contentHeight);
    }

    public interface OnFastScrollListener {
        void onFastScroll();
    }

    public ObservableWebView(Context context) {
        super(context);
        init();
    }

    public ObservableWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ObservableWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        onClickListeners = new ArrayList<>();
        onScrollChangeListeners = new ArrayList<>();
        onDownMotionEventListeners = new ArrayList<>();
        onUpOrCancelMotionEventListeners = new ArrayList<>();
        onContentHeightChangedListeners = new ArrayList<>();
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        this.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int top = ObservableWebView.this.lastTop + dy;
                ObservableWebView.this.setOnScrollChanged(0, top, 0, ObservableWebView.this.lastTop);
            }
        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        this.setHasFixedSize(true);
    }

    private void setOnScrollChanged(int left, int top, int oldLeft, int oldTop) {
        this.lastTop = top;
        boolean isHumanScroll = Math.abs(top - oldTop) < MAX_HUMAN_SCROLL;
        for (OnScrollChangeListener listener : onScrollChangeListeners) {
            listener.onScrollChanged(oldTop, top, isHumanScroll);
        }
        new ObservableWebViewFunnel().logOnScrollChanged(oldTop, top, isHumanScroll);
        if (!isHumanScroll) {
            return;
        }
        totalAmountScrolled += (top - oldTop);
        if (Math.abs(totalAmountScrolled) > FAST_SCROLL_THRESHOLD
                && onFastScrollListener != null) {
            onFastScrollListener.onFastScroll();
            totalAmountScrolled = 0;
        }
        lastScrollTime = System.currentTimeMillis();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                for (OnDownMotionEventListener listener : onDownMotionEventListeners) {
                    listener.onDownMotionEvent();
                }
                if (System.currentTimeMillis() - lastScrollTime > MAX_MILLIS_BETWEEN_SCROLLS) {
                    totalAmountScrolled = 0;
                }
                touchStartX = event.getX();
                touchStartY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(event.getX() - touchStartX) <= touchSlop
                        && Math.abs(event.getY() - touchStartY) <= touchSlop) {
                    for (OnClickListener listener : onClickListeners) {
                        if (listener.onClick(event.getX(), event.getY())) {
                            return true;
                        }
                    }
                }
            case MotionEvent.ACTION_CANCEL:
                for (OnUpOrCancelMotionEventListener listener : onUpOrCancelMotionEventListeners) {
                    listener.onUpOrCancelMotionEvent();
                }
                break;
            default:
                // Do nothing for all the other things
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()) {
            return;
        }

        if (contentHeight != getContentHeight()) {
            contentHeight = getContentHeight();
            for (OnContentHeightChangedListener listener : onContentHeightChangedListeners) {
                listener.onContentHeightChanged(contentHeight);
            }
        }
        // new ObservableWebViewFunnel().logOnDraw(contentHeight);
    }

    public int getContentWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    public int getContentHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    public int getLastTop() {
        return this.lastTop;
    }

    public void setLastTop(int lastTop) {
        this.lastTop = lastTop;
    }

    public void scrollToLastTop(int lastTop) {
        // Here, we must reset the lastTop to zero.
        this.lastTop = 0;
        this.smoothScrollBy(0, lastTop);
    }
}
