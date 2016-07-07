package com.marvinlabs.widget.slideshow.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.ieatta.activity.LeadImage;
import org.ieatta.activity.leadimages.ImageViewWithFace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LeadImageAdapter extends BitmapAdapter {
    // URLs of the images to load
    private List<LeadImage> items;

    // The context in which the adapter was created
    private Context context;

    /**
     * Get the main context
     *
     * @return The context in which the adapter was created
     */
    public Context getContext() {
        return context;
    }

    /**
     * Constructor
     *
     * @param context The context that will hold the views
     */
    public LeadImageAdapter(Context context, Collection<LeadImage> items) {
        super(context);
        this.context = context;
        this.items = new ArrayList<LeadImage>(items);
    }

    //==============================================================================================
    // BITMAP LOADING
    //==

    @Override
    protected void loadBitmap(int position) {

    }

    //==============================================================================================
    // INTERFACE IMPLEMENTATION: Adapter
    //==

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public LeadImage getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    protected ImageViewWithFace newImageViewInstance() {
//        <org.ieatta.activity.leadimages.ImageViewWithFace
//        android:id="@+id/view_article_header_image_image"
//        android:layout_width="match_parent"
//        android:layout_height="match_parent"
//        app:actualImageScaleType="focusCrop"
//        android:contentDescription="@null"
//        android:background="@android:color/transparent" />

        ImageViewWithFace iv = new ImageViewWithFace(context);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // SlideShowView is a subclass of RelativeLayout. Set the layout parameters accordingly
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        iv.setLayoutParams(lp);

        return iv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageViewWithFace iv;

        if (convertView == null) {
            iv = newImageViewInstance();
        } else {
            iv = (ImageViewWithFace) convertView;
        }

        LeadImage current = this.getItem(position);

        iv.loadMultiImage(current);

        return iv;
    }

    //==============================================================================================
    // INTERFACE IMPLEMENTATION: SlideShowAdapter
    //==

    @Override
    public void prepareSlide(int position) {

    }

    @Override
    public void discardSlide(int position) {

    }

    @Override
    public SlideStatus getSlideStatus(int position) {
        return SlideStatus.READY;
    }
}
