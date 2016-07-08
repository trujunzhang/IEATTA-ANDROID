package org.ieatta.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.facebook.drawee.view.SimpleDraweeView;

import org.ieatta.R;
import org.ieatta.activity.LeadImage;
import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.server.cache.ThumbnailImageUtil;
import org.ieatta.tasks.OrderedRecipesTask;
import org.wikipedia.views.ViewUtil;

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

    public void loadLeadImage(final String uuid, final OrderedRecipesTask orderedRecipesTask) {
        LeadImage leadImage = orderedRecipesTask.getLeadImage(uuid);
        if (leadImage != null) {
            ViewUtil.loadMultiImageUrlInto(AvatarView.this, leadImage.getLocalUrl(), leadImage.getOnlineUrl());
        } else {
            final List<Realm> realmList = new LinkedList<>();
            LocalDatabaseQuery.getPhoto(uuid, false, realmList).onSuccess(new Continuation<DBPhoto, Void>() {
                @Override
                public Void then(Task<DBPhoto> task) throws Exception {
                    DBPhoto photo = task.getResult();
                    if (photo != null) {
                        String originalUrl = photo.getOriginalUrl();
                        String localUrl = ThumbnailImageUtil.sharedInstance.getFirstImageAbstractPath(uuid);
                        LeadImage leadImage = new LeadImage(localUrl, originalUrl);
                        orderedRecipesTask.setLeadImage(uuid, leadImage);
                        ViewUtil.loadMultiImageUrlInto(AvatarView.this, localUrl, originalUrl);
                    }
                    return null;
                }
            }).continueWith(new Continuation<Void, Void>() {
                @Override
                public Void then(Task<Void> task) throws Exception {
                    LocalDatabaseQuery.closeRealmList(realmList);
                    return null;
                }
            });
        }
    }

    public void loadNewPhotoByModel(String uuid) {
        String localUrl = ThumbnailImageUtil.sharedInstance.getFirstImageAbstractPath(uuid);
        ViewUtil.loadImageUrlInto(AvatarView.this, localUrl);
    }

    public void loadImageUrl(String url) {
        ViewUtil.loadImageUrlInto(AvatarView.this, url);
    }

    private void configureAvatar(int placeHolder) {
//        ViewUtil.loadImageUrlInto(this, mGalleryItem.getThumbUrl());
    }
}
