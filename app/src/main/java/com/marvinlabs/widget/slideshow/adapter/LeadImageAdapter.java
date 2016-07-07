package com.marvinlabs.widget.slideshow.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

import org.ieatta.activity.LeadImage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LeadImageAdapter extends BitmapAdapter {
    // URLs of the images to load
    private List<LeadImage> items;

    /**
     * Constructor
     *
     * @param context The context that will hold the views
     */
    public LeadImageAdapter(Context context, Collection<LeadImage> items) {
        super(context);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
