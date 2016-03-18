package org.ieatta.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.ieatta.database.models.DBRestaurant;
import org.wikipedia.views.ViewUtil;

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



//        new DBPhoto().queryPhotosByModel(model)
//                .onSuccess(new Continuation<List<ParseModelAbstract>, Void>() {
//                    @Override
//                    public Void then(Task<List<ParseModelAbstract>> task) throws Exception {
//                        List<ParseModelAbstract> taskResult = task.getResult();
//                        ParseModelAbstract first = taskResult.get(0);
//                        if (first != null) {
//                            self.loadNewPhotoByPhoto((Photo) first, placeHolder);
//                        }
//                        return null;
//                    }
//                });
    }

    private void configureAvatar(int placeHolder) {
//        ViewUtil.loadImageUrlInto(this, mGalleryItem.getThumbUrl());
    }
}
