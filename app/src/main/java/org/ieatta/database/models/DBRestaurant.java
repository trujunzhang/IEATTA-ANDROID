package com.ieatta.com.parse.engine.realm.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by djzhang on 12/20/15.
 */
public class DBRestaurant extends RealmObject {
    @PrimaryKey
    private String UUID = "";
    private Date objectCreatedDate = new Date();
    private String displayName = "";

    private double latitude = 0.0;
    private double longitude = 0.0;

    // Variables invoked by viewController
    private String googleMapAddress = "";


    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Date getObjectCreatedDate() {
        return objectCreatedDate;
    }

    public void setObjectCreatedDate(Date objectCreatedDate) {
        this.objectCreatedDate = objectCreatedDate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public String getGoogleMapAddress() {
        return googleMapAddress;
    }

    public void setGoogleMapAddress(String googleMapAddress) {
        this.googleMapAddress = googleMapAddress;
    }
}
