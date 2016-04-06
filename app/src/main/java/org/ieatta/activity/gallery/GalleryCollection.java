package org.ieatta.activity.gallery;

import org.ieatta.activity.PageTitle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GalleryCollection {
    private static final int MIN_IMAGE_SIZE = 64;

    private final List<GalleryItem> itemList;

    public List<GalleryItem> getItemList() {
        return itemList;
    }

    public GalleryCollection() {
        this.itemList = new LinkedList<>();
    }

    public GalleryCollection(List<GalleryItem> itemList) {
        this.itemList = itemList;
    }
}
