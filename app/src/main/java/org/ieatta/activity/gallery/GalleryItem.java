package org.ieatta.activity.gallery;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GalleryItem {
    private final String uuid;

    private String url;

    public String getUrl() {
        return url;
    }

    private final String mimeType;

    public String getMimeType() {
        return mimeType;
    }

    private final HashMap<String, String> metadata;

    public Map<String, String> getMetadata() {
        return metadata;
    }

    private final String thumbUrl;

    public String getThumbUrl() {
        return thumbUrl;
    }

    private final int width;

    public int getWidth() {
        return width;
    }

    private final int height;

    public int getHeight() {
        return height;
    }

    public GalleryItem(String uuid) {
        this.uuid = uuid;
        this.url = null;
        this.mimeType = "*/*";
        this.thumbUrl = null;
        this.metadata = null;
        this.width = 0;
        this.height = 0;
    }

    public GalleryItem(String uuid, String thumbUrl) {
        this.uuid = uuid;
        this.url = null;
        this.mimeType = "*/*";
        this.thumbUrl = thumbUrl;
        this.metadata = null;
        this.width = 0;
        this.height = 0;
    }

    public String getUUID() {
        return uuid;
    }
}
