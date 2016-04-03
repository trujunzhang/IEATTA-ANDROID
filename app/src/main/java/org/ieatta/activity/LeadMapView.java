package org.ieatta.activity;

import android.text.TextUtils;

import org.ieatta.database.query.OnlineDatabaseQuery;
import org.ieatta.server.cache.CacheImageUtil;

import java.io.File;

import bolts.Continuation;
import bolts.Task;

public class LeadMapView {
    private double latitude;
    private double longitude;

    private String title;
    private String snippet;

    public LeadMapView(double latitude, double longitude, String title, String snippet) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.snippet = snippet;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
}
