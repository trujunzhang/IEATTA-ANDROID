package org.ieatta.activity.leadimages;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.marvinlabs.widget.slideshow.SlideShowAdapter;
import com.marvinlabs.widget.slideshow.SlideShowView;
import com.marvinlabs.widget.slideshow.adapter.LeadImageAdapter;
import com.marvinlabs.widget.slideshow.adapter.ResourceBitmapAdapter;

import org.ieatta.IEAApp;
import org.ieatta.R;
import org.ieatta.activity.LeadImage;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArticleHeaderImageView extends FrameLayout {
    @Bind(R.id.view_article_header_image_image)
    SlideShowView image;

    public ArticleHeaderImageView(Context context) {
        super(context);
        init();
    }

    public ArticleHeaderImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArticleHeaderImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ArticleHeaderImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setLoadListener(@Nullable ImageViewWithFace.OnImageLoadListener listener) {
//        image.setOnImageLoadListener(listener);
    }


    public void load(@Nullable List<LeadImage> leadImages) {
        boolean noLeadImages = (leadImages == null) || (leadImages.size() == 0);
        if (noLeadImages) {
            setVisibility(GONE);
            image.stop();
        } else {
            setVisibility(VISIBLE);
            this.startSlideShow(this.createResourceAdapter(leadImages));
        }
    }

    private SlideShowAdapter createResourceAdapter(List<LeadImage> leadImages) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;
        return new LeadImageAdapter(IEAApp.getInstance(), leadImages);
    }

    private void startSlideShow(SlideShowAdapter adapter) {
        // Create an adapter
        image.setAdapter(adapter);

        // Optional customisation follows
//         slideShowView.setTransitionFactory(new ZoomTransitionFactory());
        // slideShowView.setPlaylist(new RandomPlayList());

        // Some listeners if needed
//        image.setOnSlideShowEventListener(slideShowListener);
//        image.setOnSlideClickListener(slideClickListener);

        // Then attach the adapter
        image.play();
    }

    public void load(@Nullable String url, Boolean local) {
        if (url == null) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
//            if (local) {
//                image.loadOfflineImage(url);
//            } else {
//                image.loadImage(url);
//            }
        }
    }

    public void setAnimationPaused(boolean paused) {
//        if (image.getController() != null && image.getController().getAnimatable() != null) {
//            if (paused) {
//                image.getController().getAnimatable().stop();
//            } else {
//                image.getController().getAnimatable().start();
//            }
//        }
    }

    public boolean hasImage() {
        return getVisibility() != GONE;
    }

    public ImageViewWithFace getImage() {
//        return image;
        return null;
    }

    public void setFocusOffset(float verticalOffset) {
        final float centerHorizontal = 0.5f;
//        image.getHierarchy().setActualImageFocusPoint(new PointF(centerHorizontal, verticalOffset));
    }

    private void init() {
        setVisibility(GONE);

        // Clip the Ken Burns zoom animation applied to the image.
        setClipChildren(true);

        inflate();
        bind();
    }

    private void inflate() {
        inflate(getContext(), R.layout.view_article_header_image, this);
    }

    private void bind() {
        ButterKnife.bind(this);
    }
}