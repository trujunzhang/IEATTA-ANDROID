package org.ieatta.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.ieatta.database.models.DBPhoto;
import org.ieatta.database.models.DBRestaurant;
import org.ieatta.database.query.LocalDatabaseQuery;
import org.ieatta.server.cache.ThumbnailImageUtil;
import org.wikipedia.util.log.L;
import org.wikipedia.views.ViewUtil;

import java.io.File;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

public class AvatarView extends SimpleDraweeView {
    public AvatarView(Context context) {
        super(context);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void loadNewPhotoByModel(DBRestaurant model, int placeHolder) {
        this.configureAvatar(placeHolder);

        List<File> files = ThumbnailImageUtil.sharedInstance.getImagesList(model.getUUID());
        if (files.size() > 0) {
            File first = files.get(0);
            String path = first.getAbsolutePath();
            L.d("cached path of the photo: " + path);
            String url = String.format("file://%s", path);
            ViewUtil.loadImageUrlInto(AvatarView.this, url);
        }

//        LocalDatabaseQuery.getPhoto(model.getUUID()).onSuccess(new Continuation<DBPhoto, Object>() {
//            @Override
//            public Object then(Task<DBPhoto> task) throws Exception {
//                DBPhoto photo = task.getResult();
//                String path = ThumbnailImageUtil.sharedInstance.getCacheImageUrl(photo).getAbsolutePath();
//                L.d("cached path of the photo: " + path);
//                String url = String.format("file://%s", path);
//                ViewUtil.loadImageUrlInto(AvatarView.this, url);
//                return null;
//            }
//        });
    }

    private void configureAvatar(int placeHolder) {
//        ViewUtil.loadImageUrlInto(this, mGalleryItem.getThumbUrl());
    }
}
