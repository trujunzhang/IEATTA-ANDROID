package org.ieatta.activity.gallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.facebook.drawee.view.SimpleDraweeView;

import org.ieatta.R;
import org.wikipedia.views.ViewUtil;

public class GalleryThumbnailScrollView extends RecyclerView {
    @NonNull
    private final Context mContext;
    @NonNull
    private final Animation mPressAnimation;
    @NonNull
    private final Animation mReleaseAnimation;
    @Nullable
    private GalleryViewListener mListener;

    public GalleryThumbnailScrollView(Context context) {
        this(context, null);
    }

    public GalleryThumbnailScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GalleryThumbnailScrollView(@NonNull Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        mPressAnimation = AnimationUtils.loadAnimation(context, R.anim.thumbnail_item_press);
        mReleaseAnimation = AnimationUtils.loadAnimation(context, R.anim.thumbnail_item_release);
    }

    public interface GalleryViewListener {
        void onGalleryItemClicked(String imageUUID);

        void onGalleryAddPhotoItemClicked();
    }

    public void setGalleryViewListener(@Nullable GalleryViewListener listener) {
        mListener = listener;
    }

    public void setGalleryCollection(@NonNull GalleryCollection collection) {
        setAdapter(new GalleryViewAdapter(collection));
    }

    private class AddPhotoHolder extends ViewHolder implements OnClickListener {

        public AddPhotoHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onGalleryAddPhotoItemClicked();
            }
        }
    }

    private class GalleryItemHolder extends ViewHolder implements OnClickListener, OnTouchListener {
        private final SimpleDraweeView mImageView;
        private GalleryItem mGalleryItem;

        GalleryItemHolder(View itemView) {
            super(itemView);
            mImageView = (SimpleDraweeView) itemView.findViewById(R.id.gallery_thumbnail_image);
        }

        public void bindItem(GalleryItem item) {
            mGalleryItem = item;
            mImageView.setOnClickListener(this);
            mImageView.setOnTouchListener(this);
            ViewUtil.loadImageUrlInto(mImageView, mGalleryItem.getThumbUrl());
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onGalleryItemClicked(mGalleryItem.getUUID());
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.startAnimation(mPressAnimation);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.startAnimation(mReleaseAnimation);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    private final class GalleryViewAdapter extends RecyclerView.Adapter<ViewHolder> {
        @NonNull
        private final GalleryCollection mCollection;

        GalleryViewAdapter(@NonNull GalleryCollection collection) {
            mCollection = collection;
        }

        @Override
        public int getItemCount() {
            return mCollection.getItemList().size() + 1;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int pos) {
            if (pos == 0) {
                View view = LayoutInflater.from(mContext)
                        .inflate(R.layout.add_photo_cell, parent, false);
                return new AddPhotoHolder(view);
            }
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_gallery_thumbnail, parent, false);
            return new GalleryItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int pos) {
            if (holder instanceof GalleryItemHolder)
                ((GalleryItemHolder) holder).bindItem(mCollection.getItemList().get(pos-1));
        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0)
                return 0;
            return 1;
        }
    }
}
