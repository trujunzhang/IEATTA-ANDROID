package org.ieatta.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;

import com.facebook.drawee.view.SimpleDraweeView;

import org.ieatta.R;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.server.cache.ThumbnailImageUtil;
import org.ieatta.tasks.OrderedRecipesTask;
import org.wikipedia.util.log.L;
import org.wikipedia.views.ViewUtil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import io.realm.Realm;

public class AvatarView extends SimpleDraweeView {
    private boolean measureHeight;

    public AvatarView(Context context) {
        super(context);
        init(context, null);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        if (attrs != null) {
            TypedArray gdhAttrs = context.obtainStyledAttributes(attrs, R.styleable.AvatarView);
            this.measureHeight = gdhAttrs.getBoolean(R.styleable.AvatarView_measureHeight, false);

            // Don't forget to release some memory
            gdhAttrs.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int avatarHeight = heightMeasureSpec;
        if (this.measureHeight)
            avatarHeight = widthMeasureSpec;

        super.onMeasure(widthMeasureSpec, avatarHeight);
    }

    public void loadLeadImage(String uuid, OrderedRecipesTask orderedRecipesTask) {
        List<Realm> realmList = new LinkedList<>();
        LocalDatabaseQuery.getPhoto(uuid, false, realmList).onSuccess(new Continuation<DBPhoto, Void>() {
            @Override
            public Void then(Task<DBPhoto> task) throws Exception {
                DBPhoto photo = task.getResult();
                if (photo != null) {
                    String originalUrl = photo.getOriginalUrl();

                }
                return null;
            }
        });
    }

    public void loadNewPhotoByModel(String uuid) {
        ThumbnailImageUtil.sharedInstance.getImagesListTask(uuid).onSuccess(new Continuation<List<File>, Void>() {
            @Override
            public Void then(Task<List<File>> task) throws Exception {
                List<File> files = task.getResult();
                if (files.size() > 0) {
                    File first = files.get(0);
                    String path = first.getAbsolutePath();
                    L.d("cached path of the photo: " + path);
                    String url = String.format("file://%s", path);
                    ViewUtil.loadImageUrlInto(AvatarView.this, url);
                }
                return null;
            }
        });
    }

    public void loadImageUrl(String url) {
        ViewUtil.loadImageUrlInto(AvatarView.this, url);
    }

    private void configureAvatar(int placeHolder) {
//        ViewUtil.loadImageUrlInto(this, mGalleryItem.getThumbUrl());
    }
}
